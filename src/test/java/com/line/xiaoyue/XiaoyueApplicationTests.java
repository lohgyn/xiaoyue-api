package com.line.xiaoyue;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.line.xiaoyue.controller.AngularPathController;

@SpringBootTest
class XiaoyueApplicationTests {

	@Autowired
	private AngularPathController angularPathController;

	@Test
	void contextLoads() {

		assertNotNull(angularPathController);
	}

}
