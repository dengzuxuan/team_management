package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.GetListService;
import com.team.backend.service.user.management.GetNoneListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetNoneListServiceImpl implements GetNoneListService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Result getNoneList() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("student_no","username").eq("leader_no",0).eq("role",3);

        return Result.success(userMapper.selectMaps(queryWrapper));
    }
}
