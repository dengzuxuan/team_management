package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetListServiceImpl implements GetListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getList(String range, int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();
        if(adminUser.getRole()!=1){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }


        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> rowPage = new Page<>();

        switch (range){

            case "member":
                queryWrapper.eq("admin_no",adminUser.getStudentNo()).eq("role",3).select(
                        User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
                );
                rowPage = userMapper.selectPage(page, queryWrapper);
                return Result.success(rowPage);
            case "leader":
                queryWrapper.eq("admin_no",adminUser.getStudentNo()).eq("role",2).select(
                        User.class,info->!info.getColumn().equals("password_real")
                                && !info.getColumn().equals("password")
                );
                rowPage = userMapper.selectPage(page, queryWrapper);
                return Result.success(rowPage);
            case "all":
                queryWrapper.eq("admin_no",adminUser.getStudentNo()).select(
                        User.class,info->!info.getColumn().equals("password_real")
                                && !info.getColumn().equals("password")
                );
                rowPage = userMapper.selectPage(page, queryWrapper);
                return Result.success(rowPage);
        }
        return Result.build(null,ResultCodeEnum.INPUT_PARAM_WRONG);
    }
}
