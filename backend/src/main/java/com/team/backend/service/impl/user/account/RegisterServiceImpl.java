package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.RegisterUserType;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.RegisterService;
import com.team.backend.utils.common.excelType.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.team.backend.utils.common.consts.roleConst.*;
import static java.lang.Integer.parseInt;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeamInfoMapper teamInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegisterExcelServiceImpl registerExcelService;

    @Override
    public Result registerAdmin(RegisterUserType user) {
        if(user.getStudentNo()==null){
            return Result.build(null,ResultCodeEnum.USER_NAME_NOT_EMPTY);
        }
        if(user.getPassword()==null){
            return Result.build(null,ResultCodeEnum.PASSWORD_NOT_EMPTY);
        }

        if(user.getRole() != ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_PARAM_WRONG);
        }


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",user.getStudentNo());
        User userFind = userMapper.selectOne(queryWrapper);
        if(userFind!=null){
            return Result.build(null,ResultCodeEnum.USER_NAME_ALREADY_EXIST);
        }

        User newuser = new User();
        newuser.setStudentNo(user.getStudentNo());
        newuser.setRole(user.getRole());
        newuser.setPassword(user.getPassword());
        userMapper.insert(newuser);

        return Result.success(null);
    }

    @Override
    public Result register(UserType user) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User loginuser = loginUser.getUser();
        if(loginuser.getRole()!=ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        ResultCodeEnum codeEnum = registerExcelService.checkUserInfoInput(user,loginuser.getStudentNo());
        if(codeEnum!=ResultCodeEnum.SUCCESS){
            return Result.build(null, codeEnum);
        }
        return registerSingleUser(user,loginuser.getStudentNo());
    }

    @Override
    public Result registermore(UserType[] users) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User loginuser = loginUser.getUser();
        if(loginuser.getRole()!=ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        int failedCnt = 0;
        for (UserType user :users) {
            ResultCodeEnum codeEnum = registerExcelService.checkUserInfoInput(user,loginuser.getStudentNo());
            if(codeEnum!=ResultCodeEnum.SUCCESS){
                failedCnt++;
                continue;
            }
            Result result = registerSingleUser(user, loginuser.getStudentNo());
        }
        if (failedCnt == 0){
            return Result.success(null);
        }
        return Result.success("共有"+failedCnt+"个用户新建失败");
    }

    public Result registerSingleUser(UserType user,String adminNo){
        String tel = "",email = "",cardno = "",no="";
        Integer leaderid = null;
        TeamInfo findTeamInfo =null;
        if(user.getTeamNo()!=null){
            QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("no",user.getTeamNo()).eq("admin_no",adminNo);
            findTeamInfo = teamInfoMapper.selectOne(queryWrapper);
            if(findTeamInfo!=null){
                no=findTeamInfo.getNo();
                leaderid=findTeamInfo.getLeaderId();
            }
        }
        String password = user.getStudentNo().substring(user.getStudentNo().length() - 6);
        if(user.getTel()!=null){
            tel=user.getTel();
        }
        if(user.getEmail()!=null){
            email=user.getEmail();
        }
        if(user.getCardNo()!=null){
            cardno=user.getCardNo();
        }
        String encodedPassword = passwordEncoder.encode(password);
        Date now = new Date();
        User newuser = new User(null,no,leaderid,adminNo,user.getUsername(),encodedPassword,tel,email,MEMBERROLE,cardno,user.getStudentNo(),password,now,now);

        if(LEADERFLAG.equals(user.getRole())){
            newuser.setRole(LEADERROLE);
        }else if(MEMBERFLAG.equals(user.getRole())){
            newuser.setRole(TEAMMEMBERROLE);
        }
        userMapper.insert(newuser);

        if(LEADERFLAG.equals(user.getRole())){
            findTeamInfo.setLeaderId(newuser.getId());
            teamInfoMapper.updateById(findTeamInfo);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("team_no",user.getTeamNo()).eq("role",TEAMMEMBERROLE);
            List<User> memberUsers = userMapper.selectList(queryWrapper);
            for(User member:memberUsers){
                member.setLeaderId(newuser.getId());
                userMapper.updateById(member);
            }
        }
        return Result.success(null);
    }
}
