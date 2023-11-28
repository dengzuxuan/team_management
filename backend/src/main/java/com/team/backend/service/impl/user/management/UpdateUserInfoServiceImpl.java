package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.UpdateUserType;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.management.UpdateUserInfoService;
import com.team.backend.utils.common.excelType.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.team.backend.utils.common.consts.roleConst.*;

/**
 * @ClassName UpdateUserInfoServiceImpl
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/28 2:23
 * @Version 1.0
 */
@Service
public class UpdateUserInfoServiceImpl implements UpdateUserInfoService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    TeamInfoMapper teamInfoMapper;

    @Override
    public Result updateUserInfo(UpdateUserType userdata) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User loginuser = loginUser.getUser();
        if(loginuser.getRole()!=ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        User userPre = userMapper.selectById(userdata.getId());
        if(userPre==null){
            return Result.build(null,ResultCodeEnum.USER_NOT_EXIST);
        }

        if(!(userdata.getTeamNo()== null &&userdata.getTeamName()== null&&userdata.getRole()== null||
                (userdata.getTeamNo()!= null &&userdata.getTeamName()!= null&&userdata.getRole()!= null))){
            return Result.build(null,ResultCodeEnum.INPUT_VAR_WRONG);
        }

        if(userdata.getStudentNo()==null){
            return Result.build(null,ResultCodeEnum.INPUT_STUDENTNO_IS_NULL);
        }
        if(userdata.getUsername()==null){
            return Result.build(null,ResultCodeEnum.INPUT_USERNAME_IS_NULL);
        }

        String usernameRegex = "^([\\u4e00-\\u9fa5]{2,20}|[a-zA-Z.\\s]{2,20})$";
        Pattern usernamePatten = Pattern.compile(usernameRegex);
        Matcher usernameMatcher = usernamePatten.matcher(userdata.getUsername());
        if(!usernameMatcher.matches()){
            return Result.build(null,ResultCodeEnum.INPUT_USRNAME_PARAM_WRONG);
        }
        userPre.setUsername(userdata.getUsername());

        String studentNoRegex = "\\d{8}";
        Pattern studentNoPatten = Pattern.compile(studentNoRegex);
        Matcher studentNoMatcher = studentNoPatten.matcher(userdata.getStudentNo());
        if(!studentNoMatcher.matches()){
            return Result.build(null,ResultCodeEnum.INPUT_STUDENTNO_PARAM_WRONG);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",userdata.getStudentNo());
        User userFind = userMapper.selectOne(queryWrapper);
        if(userFind!=null && userFind.getId() != userdata.getId()){
            return Result.build(null,ResultCodeEnum.USER_NAME_ALREADY_EXIST);
        }
        userPre.setStudentNo(userdata.getStudentNo());

        if(userdata.getTel()!=null){
            String telRegex = "^[1][3,4,5,7,8][0-9]{9}$";
            Pattern telPatten = Pattern.compile(telRegex);
            Matcher telMatcher = telPatten.matcher(userdata.getTel());
            if(!telMatcher.matches()){
                return Result.build(null,ResultCodeEnum.INPUT_TEL_PARAM_WRONG);
            }
        }
        userPre.setPhone(userdata.getTel());

        if(userdata.getEmail()!=null){
            String emailRegex = "^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$";
            Pattern emailPatten = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPatten.matcher(userdata.getEmail());
            if(!emailMatcher.matches()){
                return Result.build(null,ResultCodeEnum.INPUT_EMAIL_PARAM_WRONG);
            }
        }
        userPre.setEmail(userdata.getEmail());

        if(userdata.getCardNo()!=null){
            String cardnoRegex = "^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])\\d{3}([0-9]|X)$";
            Pattern cardnoPatten = Pattern.compile(cardnoRegex);
            Matcher cardnoMatcher = cardnoPatten.matcher(userdata.getCardNo());
            if(!cardnoMatcher.matches()){
                return Result.build(null,ResultCodeEnum.INPUT_CARDNO_PARAM_WRONG);
            }
        }
        userPre.setCardNo(userdata.getCardNo());

        //如果有分配小组部分
        if(userdata.getTeamNo()!=null &&userdata.getTeamName()!=null&&userdata.getRole()!=null){
            //角色信息是否正确
            if(!LEADERFLAG.equals(userdata.getRole()) && !MEMBERFLAG.equals(userdata.getRole())){
                return Result.build(null,ResultCodeEnum.INPUT_ROLE_WRONG);
            }
            //小组编号是否存在
            QueryWrapper<TeamInfo> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("no",userdata.getTeamNo()).eq("admin_no",loginuser.getStudentNo());
            TeamInfo teamInfoFind = teamInfoMapper.selectOne(queryWrapper1);
            if(teamInfoFind==null){
                return Result.build(null,ResultCodeEnum.INPUT_TEAM_NO_NOT_EXIST);
            }
            //小组编号和组名是否对应
            if(!Objects.equals(teamInfoFind.getTeamname(), userdata.getTeamName())){
                return Result.build(null,ResultCodeEnum.INPUT_TEAM_NAME_CANT_MATCH);
            }
            //角色变为组长 为组长则需检测数据库中该小组是否有组长
            if(LEADERFLAG.equals(userdata.getRole())){
                //leaderid不为空
                if (teamInfoFind.getLeaderId()!=null){
                    //leaderid与当前userid不相等
                    if(!teamInfoFind.getLeaderId().equals(userPre.getId())){
                        return Result.build(null,ResultCodeEnum.INPUT_LEADER_ALREAY_EXIST);
                    }
                }else{
                    //leaderid为空 该学生置为组长
                    addLeader(userdata.getTeamNo(),loginuser.getStudentNo(),userdata.getId());
                    userPre.setRole(LEADERROLE);
                    userPre.setLeaderId(null);
                }
            //角色变为组员
            }else if(MEMBERFLAG.equals(userdata.getRole())){
                //先前是组长，需撤掉该组组长
                if(userPre.getRole()==LEADERROLE){
                    cancelLeader(userPre.getTeamNo(),loginuser.getStudentNo());
                }
                if(userdata.getTeamNo().equals(userPre.getTeamNo())){
                    //如果团队编号没变 撤为该组的组员
                    userPre.setLeaderId(null);
                }else{
                    //如果团队编号变了 需要变更leaderid
                    userPre.setLeaderId(teamInfoFind.getLeaderId());
                }
                userPre.setRole(TEAMMEMBERROLE);
            }
            userPre.setTeamNo(userdata.getTeamNo());
        }else{
            //没有分配小组部分 将该同学踢出某组
            //原先是组长 撤掉该组组长
            if(userPre.getRole() == LEADERROLE){
                cancelLeader(userPre.getTeamNo(),loginuser.getStudentNo());
            }
            userPre.setRole(MEMBERROLE);
        }
        userMapper.updateById(userPre);
        return Result.success(null);
    }

    private void cancelLeader(String teamNo,String adminNo){
        QueryWrapper<TeamInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("no",teamNo).eq("admin_no",adminNo);
        TeamInfo teamInfoFind = teamInfoMapper.selectOne(queryWrapper1);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_no",teamInfoFind.getNo()).eq("admin_no",adminNo);
        List<User> members = userMapper.selectList(queryWrapper);
        //更新组员的组长id
        for(User user:members){
            user.setLeaderId(null);
            userMapper.updateById(user);
        }
        //更新teaminfo
        teamInfoFind.setLeaderId(null);
        teamInfoMapper.updateById(teamInfoFind);
    }

    private void addLeader(String teamNo,String adminNo,int leaderId){
        QueryWrapper<TeamInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("no",teamNo).eq("admin_no",adminNo);
        TeamInfo teamInfoFind = teamInfoMapper.selectOne(queryWrapper1);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_no",teamInfoFind.getNo()).eq("admin_no",adminNo);
        List<User> members = userMapper.selectList(queryWrapper);
        //更新组员的组长id
        for(User user:members){
            if(user.getId() == leaderId){
                continue;
            }
            user.setLeaderId(leaderId);
            userMapper.updateById(user);
        }
        //更新teaminfo
        teamInfoFind.setLeaderId(leaderId);
        teamInfoMapper.updateById(teamInfoFind);
    }
}
