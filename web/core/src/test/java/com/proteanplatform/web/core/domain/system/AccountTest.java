package com.proteanplatform.web.core.domain.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.proteanplatform.web.core.config.CoreConfig;
import com.proteanplatform.web.core.domain.system.Account;
import com.proteanplatform.web.core.domain.system.AccountDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
public class AccountTest {

	@Autowired
	AccountDao accountDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
//	@Test
	public void test() {
	
		Account client = new Account();
		client.setEmail("test");
		client.setPassword(passwordEncoder.encode("test"));

		accountDao.save(client);
	}
	
	@Test
	public void update() {
		Account client = accountDao.findByEmail("test");
		client.setExternalId("test");
		accountDao.save(client);
	}

}
