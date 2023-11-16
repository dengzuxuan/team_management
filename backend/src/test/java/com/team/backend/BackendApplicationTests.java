package com.team.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
		String test = "测试abc";
		System.out.println("test.length() = " + test.length());
	}

}
