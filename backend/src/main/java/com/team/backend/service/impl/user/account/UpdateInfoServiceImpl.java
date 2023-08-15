package com.team.backend.service.impl.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.UpdateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UpdateInfoServiceImpl implements UpdateInfoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result updateInfo(String email, String photo, String phone) {
        if(!email.contains("@") || !email.contains(".com")){
            return Result.build(null, ResultCodeEnum.INPUT_EMAIL_PARAM_WRONG);
        }
        if (phone.length()<8){
            return Result.build(null, ResultCodeEnum.INPUT_PHONE_PARAM_WRONG);
        }
        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user =  loginUser.getUser();

        user.setEmail(email);
        user.setPhoto(photo);
        user.setPhone(phone);

        userMapper.updateById(user);
        return Result.success(null);
    }
}
