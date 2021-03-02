package org.gfm.wallet.repository;

import org.gfm.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository <User, Long>{
	@Query(value = "SELECT * FROM testschema.t_users WHERE login = ?1", nativeQuery = true)
	  User findByLogin(String login);
}
