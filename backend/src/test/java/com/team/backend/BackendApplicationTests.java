package com.team.backend;

import cn.hutool.core.util.RandomUtil;
import com.team.backend.service.backup.ManagementBackupService;
import com.team.backend.utils.ExecRemoteDocker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class BackendApplicationTests {
	@Autowired
	ManagementBackupService backupService;
	@Test
	void contextLoads() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy年MM月dd");
		Date date = new Date(System.currentTimeMillis());
		String dateFormatted = formatter.format(date);
		backupService.backup(null,dateFormatted);
//		String timestamp = String.valueOf(System.currentTimeMillis());
//		System.out.println("time:"+timestamp);
//		ExecRemoteDocker.backup(timestamp);
		//ExecRemoteDocker.recover("1701432493062");
	}

}
