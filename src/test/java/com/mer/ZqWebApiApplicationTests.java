package com.mer;

import com.mer.framework.shiro.service.TokenService;
import com.mer.project.service.SysPermissionsServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
@Slf4j
class ZqWebApiApplicationTests {
	@Autowired
	TokenService tokenService;

	@Autowired
	SysPermissionsServer sysPermissionsServer;
	@Test
	void contextLoads() {
		tokenService.getLoginUser("15701556037");
		tokenService.getLoginUser("15701556038");
		tokenService.getLoginUser("15701556039");
		tokenService.getLoginUser("15701556040");
		System.out.println();

	}

}
