package com.sherwin.javaassess.controllers;


import java.security.Principal;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.sherwin.javaassess.UserService;
import com.sherwin.javaassess.models.Role;
import com.sherwin.javaassess.models.User;






@Controller
public class AppWebController {
	@Autowired
	private UserService service;
	

	@GetMapping("")
	public String home() {		
		return "index";
	}
	
	@RequestMapping("user")
	@ResponseBody
	public Principal user(Principal principal) {		
		return principal;
	}
		
	@GetMapping("/showProfile")
	public String showProfile(Model m) {
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			  username = ((UserDetails)principal).getUsername();
			} else {
			  username = principal.toString();
			}		
		
		User user = service.get(username);
		System.out.println(username);
		List<Role> listRoles = service.getRoles();
		m.addAttribute("user", user);
		m.addAttribute("listRoles", listRoles);
		return "profile";
	}
	
	@GetMapping("/register")
	public String showRegistrationPage(Model m) {
		m.addAttribute("user", new User());
		return "registrationForm";
		
	}
	
	@PostMapping("/processRegister")
	public String processRegister(Model m, User user) {
		service.saveUserWithDefaultRole(user);
		m.addAttribute("firstName", user.getFirstName());
		m.addAttribute("lastName", user.getLastName());
	    return "successful_register";
	}
	
	@GetMapping("/listUsers")
	public String listUsers(Model m) {
		List<User> listUsers = service.listAll();
		m.addAttribute("listUsers",listUsers);
		return "users";
	}

	@GetMapping("/updateUser")
	public String listUsersForEdit(Model m) {
		List<User> listUsers = service.listAll();
		m.addAttribute("listUsers",listUsers);
		return "usersEdit";
	}
	
	@GetMapping("/users/edit/{id}")
	public String updateUser(@PathVariable("id") Long id, Model m) {
		User user = service.get(id);
		List<Role> listRoles = service.getRoles();
		m.addAttribute("user", user);
		m.addAttribute("listRoles", listRoles);
		return "userFormEdit";
	}

	@PostMapping("/users/save")
	public String updateUserSave(User user) {
		service.updateUser(user);
		return "redirect:/updateUser";		
	}
		

	
	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
	@GetMapping("/deleteUser")
	public String listUsersForDelete(Model m) {
		List<User> listUsers = service.listAll();
		m.addAttribute("listUsers",listUsers);
		return "usersDelete";
	}
	
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id, Model m) {
		User user = service.get(id);
		List<Role> listRoles = service.getRoles();
		m.addAttribute("user", user);
		m.addAttribute("listRoles", listRoles);
		return "userFormDelete";
	}

	
	
	@RequestMapping("/users/delete")
	public String deleteUserSave(User user ) {
		service.deleteUser(user);
		return "redirect:/deleteUser";
		
	}
		
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/doLogin")
	public String doLogin() {
		return "index";
	}
	
}



