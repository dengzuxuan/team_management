package com.team.backend;

import cn.hutool.core.util.RandomUtil;
import com.team.backend.utils.ExecRemoteDocker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		System.out.println(formatter.format(date));
//		String timestamp = String.valueOf(System.currentTimeMillis());
//		System.out.println("time:"+timestamp);
//		ExecRemoteDocker.backup(timestamp);
		//ExecRemoteDocker.recover("1701432493062");
	}

}
