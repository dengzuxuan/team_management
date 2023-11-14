package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.*;
import com.team.backend.pojo.*;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.UpdateInfoService;
import com.team.backend.dto.req.MemberChangeType;
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
        updateEquipement(oriStudentNo,newStudentNo);
        //2.更新设备记录 student_no check_no
        updateEquipmentRecord(oriStudentNo,newStudentNo);
        //3.更新周报 student_no
        updateWeeklyRecord(oriStudentNo,newStudentNo);
        //4.更新周报团队任务 student_no
        updateTeamWork(oriStudentNo,newStudentNo);
        //5.更新周报评论信息 student_no
        updateWeeklyReportComment(oriStudentNo,newStudentNo);
        //6.更新团队信息 admin_no leader_no
        updateTeamInfo(oriStudentNo,newStudentNo);
        //7.更细用户表信息 student_no leader_no admin_no
        updateUserInfo(oriStudentNo,newStudentNo);
        return Result.success(null);
    }

    private void updateEquipement(String oriStudentNo,String newStudentNo){
        UpdateWrapper<Equipment> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("admin_no",oriStudentNo).set("admin_no",newStudentNo);
        equipmentMapper.update(null,updateWrapper1);

        UpdateWrapper<Equipment> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("creator",oriStudentNo).set("creator",newStudentNo);
        equipmentMapper.update(null,updateWrapper2);

        UpdateWrapper<Equipment> updateWrapper3 = new UpdateWrapper<>();
        updateWrapper3.eq("recipient",oriStudentNo).set("recipient",newStudentNo);
        equipmentMapper.update(null,updateWrapper3);

        UpdateWrapper<Equipment> updateWrapper4 = new UpdateWrapper<>();
        updateWrapper4.eq("former_recipient",oriStudentNo).set("former_recipient",newStudentNo);
        equipmentMapper.update(null,updateWrapper4);
    }

    private void updateEquipmentRecord(String oriStudentNo,String newStudentNo){
        UpdateWrapper<EquipmentRecord> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("student_no",oriStudentNo).set("student_no",newStudentNo);
        equipmentRecordMapper.update(null,updateWrapper1);

        UpdateWrapper<EquipmentRecord> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("check_no",oriStudentNo).set("check_no",newStudentNo);
        equipmentRecordMapper.update(null,updateWrapper2);
    }

    private void updateWeeklyRecord(String oriStudentNo,String newStudentNo){
        UpdateWrapper<WeeklyReport> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("student_no",oriStudentNo).set("student_no",newStudentNo);
        weeklyReportMapper.update(null,updateWrapper1);
    }

    private void updateTeamWork(String oriStudentNo,String newStudentNo){
        UpdateWrapper<ReportTeamWork> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("student_no",oriStudentNo).set("student_no",newStudentNo);
        reportTeamWorkMapper.update(null,updateWrapper1);
    }

    private void updateWeeklyReportComment(String oriStudentNo,String newStudentNo){
        UpdateWrapper<ReportComment> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("student_no",oriStudentNo).set("student_no",newStudentNo);
        reportCommentMapper.update(null,updateWrapper1);
    }

    private void updateTeamInfo(String oriStudentNo,String newStudentNo){
        UpdateWrapper<TeamInfo> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("admin_no",oriStudentNo).set("admin_no",newStudentNo);
        teamInfoMapper.update(null,updateWrapper1);

        UpdateWrapper<TeamInfo> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("leader_no",oriStudentNo).set("leader_no",newStudentNo);
        teamInfoMapper.update(null,updateWrapper2);
    }


    private void updateUserInfo(String oriStudentNo,String newStudentNo){
        UpdateWrapper<User> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("admin_no",oriStudentNo).set("admin_no",newStudentNo);
        userMapper.update(null,updateWrapper1);

        UpdateWrapper<User> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper1.eq("leader_no",oriStudentNo).set("leader_no",newStudentNo);
        userMapper.update(null,updateWrapper2);

        UpdateWrapper<User> updateWrapper3 = new UpdateWrapper<>();
        updateWrapper3.eq("leader_no",oriStudentNo).set("leader_no",newStudentNo);
        userMapper.update(null,updateWrapper3);
    }

}
