package com.proteanplatform.web.core.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proteanplatform.web.core.domain.system.ClientDao;

@Controller
public class ClientController {

	@Autowired
	ClientDao clientDao;
	
	@RequestMapping("/client")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String accountHome(ModelMap modelMap) {
		
		modelMap.put("client", clientDao.findByEmail("test"));
		
		return "client/client";
	}
}
