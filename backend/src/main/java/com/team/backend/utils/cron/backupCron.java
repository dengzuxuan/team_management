package com.team.backend.utils.cron;

import com.team.backend.dto.req.BackupRemarkType;
import com.team.backend.service.backup.ManagementBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Configuration
@EnableScheduling
public class backupCron {
    @Autowired
    ManagementBackupService backupService;
    @Scheduled(cron = "0 0 12 * * ?")
    public void backupDaily() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy年MM月dd");
        Date date = new Date(System.currentTimeMillis());
        String dateFormatted = formatter.format(date) + "12点 [自动备份]";
        backupService.backup(null,dateFormatted);
    }
}
