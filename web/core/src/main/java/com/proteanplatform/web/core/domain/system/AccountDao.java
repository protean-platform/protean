package com.proteanplatform.web.core.domain.system;


import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account, Long> {

	public <T extends Account> T findByEmail(String email);
}
