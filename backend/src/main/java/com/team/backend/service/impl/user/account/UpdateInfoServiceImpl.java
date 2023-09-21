package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.*;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.ReportComment;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.UpdateInfoService;
import com.team.backend.utils.common.MemberChangeType;
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
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private EquipmentRecordMapper equipmentRecordMapper;
    @Autowired
    private ReportCommentMapper reportCommentMapper;
    @Autowired
    private ReportTeamWorkMapper reportTeamWorkMapper;
    @Autowired
    private TeamInfoMapper teamInfoMapper;
    @Autowired
    private WeeklyReportMapper weeklyReportMapper;

    @Override
    public Result updateInfo(String email, String photo, String phone,String username) {
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
        user.setUsername(username);

        userMapper.updateById(user);
        return Result.success(null);
    }

    @Override
    public Result updateStudentNo(MemberChangeType memberChangeInfo) {
        String oriStudentNo = memberChangeInfo.getOriginStudentNo();
        String newStudentNo = memberChangeInfo.getNewStudentNo();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",oriStudentNo);
        User originStudent = userMapper.selectOne(queryWrapper);

        if(originStudent==null){
            return Result.build(null,ResultCodeEnum.USER_NOT_EXIST);
        }

        //1.更新设备中的信息 admin_no creator recipient former_recipient

        //2.更新设备记录 student_no check_no

        //3.更新周报 student_no

        //4.更新周报团队任务 student_no

        //5.更新周报评论信息 student_no

        //6.更新团队信息 admin_no leader_no

        //7.更细用户表信息 student_no leader_no admin_no
        return null;
    }

//    private Result updateEquipement(String oriStudentNo,String newStudentNo){
//
//    }
}
