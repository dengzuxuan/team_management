package com.team.backend;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.team.backend.service.backup.ManagementBackupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.text.SimpleDateFormat;

@SpringBootTest
class BackendApplicationTests {
	@Autowired
	ManagementBackupService backupService;
	@Test
	void contextLoads() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy年MM月dd日");
		String command = "cd /amax/home/chendian/team_management && mkdir test.txt";
//		Date date = new Date(System.currentTimeMillis());
//		String dateFormatted = formatter.format(date) + "12点 [自动备份]";
//		backupService.backup(null,dateFormatted);
//		String timestamp = String.valueOf(System.currentTimeMillis());
//		System.out.println("time:"+timestamp);
//		ExecRemoteDockerUtils.backup(timestamp);
		//ExecRemoteDockerUtils.recover("1701432493062");
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession("chendian", "10.126.56.98", 22);
			session.setPassword("chendian123.com");

			// 配置连接
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			// 建立连接
			session.connect();

			// 执行远程命令
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand(command);

			InputStream commandOutput = channelExec.getInputStream();
			channelExec.connect();
			// 等待命令执行完成
			while (!channelExec.isClosed()) {
				Thread.sleep(1000);
			}

			// 关闭连接
			channelExec.disconnect();
			session.disconnect();

			System.out.println("复原完成");
			System.out.println(commandOutput);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
