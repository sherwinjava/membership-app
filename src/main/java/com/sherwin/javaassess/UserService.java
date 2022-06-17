package com.sherwin.javaassess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sherwin.javaassess.models.Role;
import com.sherwin.javaassess.models.RoleRepository;
import com.sherwin.javaassess.models.User;
import com.sherwin.javaassess.models.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
//	@Autowired
//	private AuthenticationManager authManager;
	

	
	
	public User saveUserWithDefaultRole(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setEnabled(true);
		Role roleUser = roleRepo.findByName("ROLE_USER");
		user.addRole(roleUser);
		
		return userRepo.save(user);		
	}
	
	public List<User> listAll(){
		return userRepo.findAll();
	}
	
	public User get(Long id) {
		return userRepo.findById(id).get();
		
	}
	
	public User get(String email) {
		
		User user = userRepo.findById(userRepo.findByEmail(email).getId()).get();
		return user;
	}
	
	public List<Role> getRoles(){
		return roleRepo.findAll();
	}
	
	public User updateUser(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);			
		return userRepo.save(user);		
	}
	
	public void deleteUser(User user) {
		userRepo.delete(user);
		System.out.println("Delete user");		
	}
}
