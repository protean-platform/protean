package com.proteanplatform.web.core.domain.system;


import org.springframework.data.repository.CrudRepository;

public interface ClientDao extends CrudRepository<Client, Long> {

	public <T extends Client> T findByEmail(String email);
}
