package com.team.backend.service.impl.backup;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.BackupRemarkType;
import com.team.backend.mapper.BackupRecordMapper;
import com.team.backend.pojo.BackupRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.backup.ManagementBackupService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.utils.ExecRemoteDocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Override
    public Result addBackup(BackupRemarkType backupRemarkinfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=ADMINROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        String timestamp = String.valueOf(System.currentTimeMillis());

        ResultCodeEnum codeEnum = ExecRemoteDocker.backup(timestamp);
        if(codeEnum!=ResultCodeEnum.SUCCESS){
            return Result.build(null,codeEnum);
        }

        Date now = new Date();
        BackupRecord backupRecord = new BackupRecord(
                null,
                user.getId(),
                timestamp,
                backupRemarkinfo.getRemark(),
                now,
                now
        );
        backupRecordMapper.insert(backupRecord);
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
}
