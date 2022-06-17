package com.sherwin.javaassess.controllers;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sherwin.javaassess.UserService;
import com.sherwin.javaassess.models.User;



@RestController
public class AppRestController {
	
	@Autowired
	UserService service;
	
	
	
	@GetMapping(path="/api/list_users")
	@RolesAllowed("ROLE_ADMIN")
	public List<User> listUsers() {
		return service.listAll();
	}
	
	@GetMapping(path="/api/view_profile", consumes= {"application/json"})
	@RolesAllowed({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
	public User viewProfile(@RequestParam(name="id") Long id) {
		return service.get(id);
	}

	@PostMapping(path="/api/register_user", consumes= {"application/json"})
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<User> registerUser(@RequestBody @Valid User user) {
		
		User savedUser = service.saveUserWithDefaultRole(user);
		URI userId = URI.create("/users/"+user.getId());

		return ResponseEntity.created(userId).body(savedUser);
	}
	
	
	@PostMapping(path="/api/update_user", consumes= {"application/json"})
	@RolesAllowed({"ROLE_ADMIN","ROLE_EDITOR"})
	public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
				
		User savedUser = service.updateUser(user);
		URI userId = URI.create("/users/"+user.getId());		
		return ResponseEntity.created(userId).body(savedUser);
	}
		
	@RequestMapping(value="/api/delete_user_by_id", method=RequestMethod.GET)
	@RolesAllowed("ROLE_ADMIN")
	public String deleteUserById(@RequestParam(name="id") Long id) {
		User deletedUser = service.get(id);
		service.deleteUser(deletedUser);
		return "Successfully Deleted User with ID: " + id;
	}
	
	@RequestMapping(value="/api/delete_user_by_email",  method=RequestMethod.GET)
	@RolesAllowed("ROLE_ADMIN")
	public String deleteUserByEmail(@RequestParam(name="email") String email) {
		User deletedUser = service.get(email);
		service.deleteUser(deletedUser);
		return "Successfully Deleted User: " + email;
	}
}
