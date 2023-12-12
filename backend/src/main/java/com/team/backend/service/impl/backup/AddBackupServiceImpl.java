package com.team.backend.service.impl.backup;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.RemoteConfig;
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
import com.team.backend.utils.remote.ExecRemoteDockerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    @Resource
    RemoteConfig remoteConfig;
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

        ResultCodeEnum codeEnum = ExecRemoteDockerUtils.recover(remoteConfig,version);
        if (codeEnum != ResultCodeEnum.SUCCESS) {
            return Result.build(null, codeEnum);
        }
        return Result.success(null);
    }

    @Override
    public Result getBackup(int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if (user.getRole() != ADMINROLE) {
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        Page<BackupRecord> page = new Page<>(pageNum,pageSize);
        Page<BackupRecord> rowPages = new Page<>();
        QueryWrapper<BackupRecord> queryWrapperrecord = new QueryWrapper<>();
        queryWrapperrecord.orderByDesc("create_time");
        rowPages = backupRecordMapper.selectPage(page,queryWrapperrecord);

        List<BackupRecordType> backupRecordTypeList = new ArrayList<>();

        for (BackupRecord backupRecord:rowPages.getRecords()){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("id",(backupRecord.getStudentId()));
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
        Map<String,Object> res = new HashMap<>();

        res.put("backupRecords",backupRecordTypeList);
        res.put("total",rowPages.getTotal());
        res.put("size",rowPages.getSize());
        res.put("current",rowPages.getCurrent());
        return Result.success(res);
    }
    @Override
    public ResultCodeEnum backup(Integer userId, String remark){
        String timestamp = String.valueOf(System.currentTimeMillis());
        ResultCodeEnum codeEnum = ExecRemoteDockerUtils.backup(remoteConfig,timestamp);
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
