package com.team.backend.service.impl.user.account;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyuncs.utils.IOUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.RegisterExcelService;
import com.team.backend.utils.common.excelType.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.team.backend.utils.common.consts.roleConst.*;

@Service
public class RegisterExcelServiceImpl implements RegisterExcelService {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class RegisterUser implements Serializable {
        private String username;
        private String studentNo;
        private String tel;
        private String email;
        private String cardNo;
        private String teamNo;
        private String teamName;
        private String role;
        private String failReason;
    }
    @Autowired
    UserMapper userMapper;
    @Autowired
    TeamInfoMapper teamInfoMapper;
    ResultCodeEnum resultCodeEnum = ResultCodeEnum.SUCCESS;
    int totalCnt=0;
    @Override
    public Result registerExcel(MultipartFile file) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();
        if(adminUser.getRole()!=ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        List<RegisterUser> correctUsers= new ArrayList<>();
        List<RegisterUser> wrongUsers= new ArrayList<>();
        InputStream inputStream =null;

        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, UserType.class, new PageReadListener<UserType>(dataList -> {
                if(dataList==null){
                    resultCodeEnum=ResultCodeEnum.FILE_WRONG_EMPTY;
                }
                totalCnt = dataList.size();
                Set<String> usernameSet = new HashSet<>();
                Set<String> userroleSet = new HashSet<>(); //有组长的话，把小组编号放进去
                for (UserType userdata : dataList) {
                    RegisterUser user = new RegisterUser();
                    user.setUsername(userdata.getUsername());
                    user.setStudentNo(userdata.getStudentNo());
                    user.setTel(userdata.getTel());
                    user.setEmail(userdata.getEmail());
                    user.setCardNo(userdata.getCardNo());
                    user.setTeamNo(userdata.getTeamNo());
                    user.setTeamName(userdata.getTeamName());
                    user.setRole(userdata.getRole());

                    if(usernameSet.contains(userdata.getStudentNo())){
                        user.failReason=ResultCodeEnum.FILE_WRONG_STUDENTNO_REPEAT.getMessage();
                        wrongUsers.add(user);
                        continue;
                    }

                    ResultCodeEnum checkUserInfoInput = checkUserInfoInput(userdata,adminUser.getStudentNo());
                    if (checkUserInfoInput!=ResultCodeEnum.SUCCESS){
                        user.failReason=checkUserInfoInput.getMessage();
                        wrongUsers.add(user);
                        continue;
                    }
                    usernameSet.add(userdata.getStudentNo());
                    if(LEADERFLAG.equals(userdata.getRole())){
                        if(userroleSet.contains(userdata.getTeamNo())){
                            user.failReason=ResultCodeEnum.FILE_WRONG_LEADER_REPEAT.getMessage();
                            wrongUsers.add(user);
                            continue;
                        }
                        userroleSet.add(userdata.getTeamNo());
                    }
                    correctUsers.add(user);
                }
            })).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }

        Map<String,Object> res = new HashMap<>();
        res.put("totalCnt",totalCnt);
        res.put("corrcetCnt",correctUsers.size());
        res.put("wrongCnt",wrongUsers.size());

        res.put("correctUsers",correctUsers);
        res.put("wrongUsers",wrongUsers);

        return Result.build(res,resultCodeEnum);
    }

    public ResultCodeEnum checkUserInfoInput(UserType userdata,String adminNo){
        if(!(userdata.getTeamNo()== null &&userdata.getTeamName()== null&&userdata.getRole()== null||
                (userdata.getTeamNo()!= null &&userdata.getTeamName()!= null&&userdata.getRole()!= null))){
            return ResultCodeEnum.INPUT_VAR_WRONG;
        }

        if(userdata.getStudentNo()==null){
            return ResultCodeEnum.INPUT_STUDENTNO_IS_NULL;
        }

        if(userdata.getUsername()==null){
            return ResultCodeEnum.INPUT_USERNAME_IS_NULL;
        }

        String usernameRegex = "^([\\u4e00-\\u9fa5]{2,20}|[a-zA-Z.\\s]{2,20})$";
        Pattern usernamePatten = Pattern.compile(usernameRegex);
        Matcher usernameMatcher = usernamePatten.matcher(userdata.getUsername());
        if(!usernameMatcher.matches()){
            return ResultCodeEnum.INPUT_USRNAME_PARAM_WRONG;
        }

        String studentNoRegex = "\\d{8}";
        Pattern studentNoPatten = Pattern.compile(studentNoRegex);
        Matcher studentNoMatcher = studentNoPatten.matcher(userdata.getStudentNo());
        if(!studentNoMatcher.matches()){
            return ResultCodeEnum.INPUT_STUDENTNO_PARAM_WRONG;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",userdata.getStudentNo());
        User userFind = userMapper.selectOne(queryWrapper);
        if(userFind!=null){
            return ResultCodeEnum.USER_NAME_ALREADY_EXIST;
        }

        if(userdata.getTel()!=null){
            String telRegex = "^[1][3,4,5,7,8][0-9]{9}$";
            Pattern telPatten = Pattern.compile(telRegex);
            Matcher telMatcher = telPatten.matcher(userdata.getTel());
            if(!telMatcher.matches()){
                return ResultCodeEnum.INPUT_TEL_PARAM_WRONG;
            }
        }

        if(userdata.getEmail()!=null){
            String emailRegex = "^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$";
            Pattern emailPatten = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPatten.matcher(userdata.getEmail());
            if(!emailMatcher.matches()){
                return ResultCodeEnum.INPUT_EMAIL_PARAM_WRONG;
            }
        }

        if(userdata.getCardNo()!=null){
            String cardnoRegex = "^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])\\d{3}([0-9]|X)$";
            Pattern cardnoPatten = Pattern.compile(cardnoRegex);
            Matcher cardnoMatcher = cardnoPatten.matcher(userdata.getCardNo());
            if(!cardnoMatcher.matches()){
                return ResultCodeEnum.INPUT_CARDNO_PARAM_WRONG;
            }
        }

        //如果有分配小组部分
        if(userdata.getTeamNo()!=null &&userdata.getTeamName()!=null&&userdata.getRole()!=null){
            //角色信息是否正确
            if(!LEADERFLAG.equals(userdata.getRole()) && !MEMBERFLAG.equals(userdata.getRole())){
                return ResultCodeEnum.INPUT_ROLE_WRONG;
            }
            //小组编号是否存在
            QueryWrapper<TeamInfo> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("no",userdata.getTeamNo()).eq("admin_no",adminNo);
            TeamInfo teamInfoFind = teamInfoMapper.selectOne(queryWrapper1);
            if(teamInfoFind==null){
                return ResultCodeEnum.INPUT_TEAM_NO_NOT_EXIST;
            }
            //小组编号和组名是否对应
            if(!Objects.equals(teamInfoFind.getTeamname(), userdata.getTeamName())){
                return ResultCodeEnum.INPUT_TEAM_NAME_CANT_MATCH;
            }
            //角色是否为组长 为组长则需检测excel中是否有组长（在前面） 以及 数据库中该小组是否有组长
            if(Objects.equals(userdata.getRole(), LEADERFLAG)){
                if (teamInfoFind.getLeaderNo()!=null){
                    return ResultCodeEnum.INPUT_LEADER_ALREAY_EXIST;
                }
            }
        }
        return ResultCodeEnum.SUCCESS;
    }
}
