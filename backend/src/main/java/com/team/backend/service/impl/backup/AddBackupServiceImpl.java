package com.team.backend.service.impl.backup;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.BackupRemarkType;
import com.team.backend.dto.resp.BackupRecordType;
import com.team.backend.mapper.BackupRecordMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.BackupRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.backup.ManagementBackupService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.utils.ExecRemoteDocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.team.backend.utils.common.consts.roleConst.*;

/**
 * <p>
 *  备份
 * </p>
 *
 * @author Colin
 * @since 2023/12/1
 */
@Service
public class AddBackupServiceImpl implements ManagementBackupService {
    @Autowired
    BackupRecordMapper backupRecordMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Result addBackup(BackupRemarkType backupRemarkinfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=ADMINROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        ResultCodeEnum codeEnum = backup(user.getId(), backupRemarkinfo.getRemark());
        if(codeEnum != ResultCodeEnum.SUCCESS){
            return Result.build(null,codeEnum);
        }
        return Result.success(null);
    }

    @Override
    public Result addRecover(String version) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if (user.getRole() != ADMINROLE) {
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<BackupRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version", version);
        BackupRecord backupRecord = backupRecordMapper.selectOne(queryWrapper);
        if (backupRecord == null) {
            return Result.build(null, ResultCodeEnum.RECOVER_FILE_NOT_EXIT);
        }

        ResultCodeEnum codeEnum = ExecRemoteDocker.recover(version);
        if (codeEnum != ResultCodeEnum.SUCCESS) {
            return Result.build(null, codeEnum);
        }
        return Result.success(null);
    }

    @Override
    public Result getBackup() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if (user.getRole() != ADMINROLE) {
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        List<BackupRecordType> backupRecordTypeList = new ArrayList<>();

        List<BackupRecord> backupRecords = backupRecordMapper.selectList(null);
        for (BackupRecord backupRecord:backupRecords){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("id",(backupRecord.getId()));
            User user1 = userMapper.selectOne(queryWrapper);
            BackupRecordType backupRecordType = new BackupRecordType(
                    backupRecord.getId(),
                    backupRecord.getStudentId(),
                    user1,
                    backupRecord.getVersion(),
                    backupRecord.getRemark(),
                    backupRecord.getCreateTime(),
                    backupRecord.getUpdateTime()
            );
            backupRecordTypeList.add(backupRecordType);
        }

        return Result.success(backupRecordTypeList);
    }
    @Override
    public ResultCodeEnum backup(Integer userId, String remark){


        String timestamp = String.valueOf(System.currentTimeMillis());

        ResultCodeEnum codeEnum = ExecRemoteDocker.backup(timestamp);
        if(codeEnum!=ResultCodeEnum.SUCCESS){
            return codeEnum;
        }

        Date now = new Date();
        BackupRecord backupRecord = new BackupRecord(
                null,
                userId,
                timestamp,
                remark,
                now,
                now
        );
        backupRecordMapper.insert(backupRecord);
        return ResultCodeEnum.SUCCESS;
    }
}
