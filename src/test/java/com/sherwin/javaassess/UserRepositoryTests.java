package com.sherwin.javaassess;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.sherwin.javaassess.models.Role;
import com.sherwin.javaassess.models.RoleRepository;
import com.sherwin.javaassess.models.User;
import com.sherwin.javaassess.models.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
 
    @Autowired
    private TestEntityManager entityManager;
     
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;
     
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("ravikumar@gmail.com");
        user.setPassword("ravi2020");
        user.setFirstName("Ravi");
        user.setLastName("Kumar");
         
        User savedUser = userRepo.save(user);
         
        User existUser = entityManager.find(User.class, savedUser.getId());
         
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
         
    }
    
    @Test
    public void testFindUserByEmail() {
    	String email = "sherwinbongabong@user.com";
    	User user = userRepo.findByEmail(email);
    	
    	assertThat(user).isNotNull();
    }
    
    @Test
    public void testAddRoleToNewUser() {
    	User user = new User();
        user.setEmail("tommy@gmail.com");
        user.setPassword("tom2020");
        user.setFirstName("Tommy");
        user.setLastName("Howard");
        
        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        
        User savedUser = userRepo.save(user);
        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    	
    }
    
    @Test
    public void testAddRolesToExistingUser() {
    	User user = userRepo.findById(1L).get();
    	
    	Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        
        Role roleAdmin = new Role(2L);
        user.addRole(roleAdmin);
        
        User savedUser = userRepo.save(user);
        assertThat(savedUser.getRoles().size()).isEqualTo(2);
            	
    }
    
    
    
    
}
