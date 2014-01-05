package com.proteanplatform.web.core.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.proteanplatform.web.core.config.CoreConfig;
import com.proteanplatform.web.core.domain.system.Client;
import com.proteanplatform.web.core.domain.system.ClientDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfig.class)
public class ClientTest {

	@Autowired
	ClientDao clientDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
//	@Test
	public void test() {
	
		Client client = new Client();
		client.setEmail("test");
		client.setPassword(passwordEncoder.encode("test"));

		clientDao.save(client);
	}
	
	@Test
	public void update() {
		Client client = clientDao.findByEmail("test");
		client.setExternalId("test");
		clientDao.save(client);
	}

}
