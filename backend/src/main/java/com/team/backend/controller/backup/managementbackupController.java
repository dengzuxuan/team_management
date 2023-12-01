package com.team.backend.controller.backup;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.BackupRemarkType;
import com.team.backend.service.backup.ManagementBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/1
 */
@RestController
public class managementbackupController {
    @Autowired
    ManagementBackupService backupService;
    @PostMapping(value = "/v1/backup/management/addbackup/",consumes="application/json")
    public Result addbackup(@RequestBody BackupRemarkType backupRemarkinfo){
        return backupService.addBackup(backupRemarkinfo);
    }
    @PostMapping(value = "/v1/backup/management/addrecover/")
    public Result addrecover(@RequestParam Map<String,String> map){
        String version = map.get("version");
        return backupService.addRecover(version);
    }
    @GetMapping(value = "/v1/backup/management/getbackups/")
    public Result getbackups(){
        return backupService.getBackup();
    }
}
