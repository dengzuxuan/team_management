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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UpdateInfoServiceImpl implements UpdateInfoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result updateInfo(String email, String photo, String phone) {
        String phoneRegex = "^[1][3,4,5,7,8][0-9]{9}$";
        Pattern phonePatten = Pattern.compile(phoneRegex);
        Matcher phoneMatcher = phonePatten.matcher(phone);
        if(!phone.equals("") && !phoneMatcher.matches()){
            return Result.build(null, ResultCodeEnum.INPUT_PHONE_PARAM_WRONG);
        }


        String emailRegex = "^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$";
        Pattern emailPatten = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPatten.matcher(email);
        if(!email.equals("") && !emailMatcher.matches()){
            return Result.build(null, ResultCodeEnum.INPUT_EMAIL_PARAM_WRONG);
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
