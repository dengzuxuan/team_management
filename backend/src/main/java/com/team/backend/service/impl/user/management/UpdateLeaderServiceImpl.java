package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.management.UpdateTeamInfoService;
import com.team.backend.service.user.management.UpdateLeaderService;
import com.team.backend.service.user.management.UpdateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 切换组长
 */
@Service
public class UpdateLeaderServiceImpl implements UpdateLeaderService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UpdateRoleService updateRoleService;

    @Autowired
    UpdateTeamInfoService updateTeamInfoService;
    @Override
    public Result updateLeader(String oldStudentNo, String newStudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();
        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapperOld = new QueryWrapper<>();
        queryWrapperOld.eq("student_no",oldStudentNo);
        User oldUser = userMapper.selectOne(queryWrapperOld);

        QueryWrapper<User> queryWrapperNew = new QueryWrapper<>();
        queryWrapperNew.eq("student_no",newStudentNo);
        User newUser = userMapper.selectOne(queryWrapperNew);

        if(oldUser==null || newUser==null ){
            return Result.build(null,ResultCodeEnum.USER_NOT_EXIST);
        }
        if(!Objects.equals(adminUser.getStudentNo(), oldUser.getAdminNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }
        if(oldUser.getRole()!=2 || newUser.getRole()!=3 || !Objects.equals(oldUser.getStudentNo(), newUser.getLeaderNo())){
            return Result.build(null,ResultCodeEnum.USER_CHANGE_WRONG);
        }

        //1.修改旧学生role到组员
        Result res = updateRoleService.updateRole(oldStudentNo,"3");
        if(res.getCode()!=200){
            return res;
        }
        //2.修改新学生的leader为空
        UpdateWrapper<User> updateWrapperNew = new UpdateWrapper<>();
        updateWrapperNew.eq("student_no",newStudentNo);
        updateWrapperNew.set("leader_no","");
        userMapper.update(null,updateWrapperNew);

        //3.修改新学生role到组长
        Result res1 = updateRoleService.updateRole(newStudentNo,"2");
        if(res1.getCode()!=200){
            return res1;
        }

        //4.修改旧学生组员们【包括新学生】的领导改为新学生
        UpdateWrapper<User> updateWrapperList = new UpdateWrapper<>();
        updateWrapperList.eq("leader_no",oldStudentNo);
        updateWrapperList.set("leader_no",newStudentNo);
        userMapper.update(null,updateWrapperList);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_no",oldStudentNo);
        updateWrapper.set("leader_no",newStudentNo);
        userMapper.update(null,updateWrapper);

        //5.修改team信息的leader no
        updateTeamInfoService.updateTeamLeader(oldStudentNo,newStudentNo);

        return Result.success(null);
    }
}
