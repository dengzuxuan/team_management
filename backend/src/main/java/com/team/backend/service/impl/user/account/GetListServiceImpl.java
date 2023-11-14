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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                break;
            case "leader":
                queryWrapper.eq("admin_no",adminUser.getStudentNo()).eq("role",2).select(
                        User.class,info->!info.getColumn().equals("password_real")
                                && !info.getColumn().equals("password")
                );
                rowPage = userMapper.selectPage(page, queryWrapper);
                break;
            case "all":
                queryWrapper.eq("admin_no",adminUser.getStudentNo()).select(
                        User.class,info->!info.getColumn().equals("password_real")
                                && !info.getColumn().equals("password")
                );
                rowPage = userMapper.selectPage(page, queryWrapper);
                break;
            default:
                return Result.build(null,ResultCodeEnum.INPUT_PARAM_WRONG);
        }

        List<ExtendUser> extendUserList = new ArrayList<>();

        List<User> userList = rowPage.getRecords();
        for(User userInfo:userList){
            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("student_no",userInfo.getAdminNo()).select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            );
            User adminInfo = userMapper.selectOne(queryWrapper1);

            QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("student_no",userInfo.getLeaderNo()).select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            );
            User leaderInfo = userMapper.selectOne(queryWrapper2);


            ExtendUser extendUser = new ExtendUser(
                    userInfo,
                    leaderInfo,
                    adminInfo
            );

            extendUserList.add(extendUser);
        }

        Map<String,Object> res = new HashMap<>();
        res.put("userlist",extendUserList);
        res.put("total",rowPage.getTotal());
        res.put("size",rowPage.getSize());
        res.put("current",rowPage.getCurrent());
        return Result.success(res);
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class ExtendUser implements Serializable {
        private User userInfo;
        private User leaderInfo;
        private User adminInfo;

    }
}
