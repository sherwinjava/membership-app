package com.sherwin.javaassess;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.sherwin.javaassess.models.Role;
import com.sherwin.javaassess.models.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	
	@Autowired
	RoleRepository repo;
	
	@Test
	public void testCreateRole() {
		Role user = new Role ("User");
		Role admin = new Role("Admin");
		Role customer = new Role ("Customer");
		
		repo.saveAll(List.of(user,admin,customer));
		List<Role> listRoles = repo.findAll();
		assertThat(listRoles.size()).isEqualTo(3);		
	}
	
	@Test
	public void testListRoles() {
		List<Role> listRoles = repo.findAll();
		assertThat(listRoles.size()).isGreaterThan(0);
		
		listRoles.forEach(System.out::println);
	}
	
	
	

}