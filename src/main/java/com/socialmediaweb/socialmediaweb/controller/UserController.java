package com.socialmediaweb.socialmediaweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.service.AuthenticationService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}, allowedHeaders = "Content-Type")
public class UserController {
	@Autowired
	AuthenticationService service;
	
	@PostMapping("/createuser")
	public ResponseEntity<String> createUser(@RequestBody Users user) {
		boolean isUsernameExists = service.isUsernameExists(user.getUsername());
		boolean isEmailExists = service.isEmailExists(user.getEmail());
		if (isUsernameExists) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already in use.");
		}
		
		if (isEmailExists) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use.");
		}
		
		service.saveUser(user);
		return ResponseEntity.ok("User has registered successfully!");
	} 
	
	@PostMapping("/createusers")
	public List<Users> createUsers(@RequestBody List<Users> users) {
		return service.saveUsers(users);
	}
	
	@GetMapping("/users")
	public List<Users> getUsers() {
		return service.getUsers();
	}
	
	@GetMapping("/user/{user_id}")
	public Users findUserById(@PathVariable("user_id") Integer user_id) {
		return service.getUsersById(user_id);
	}
	
	@PutMapping("/updateuser")
	public Users updateUser(@RequestBody Users user) {
		return service.updateUser(user);
	}
	
	@DeleteMapping("/deleteuser/{user_id}")
	public String deleteUser(@PathVariable("user_id") Integer user_id) {
		return service.deleteUser(user_id);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Users> login(@RequestParam("username") String username, @RequestParam("password") String password) {
	    Users user = service.authenticateUser(username, password);
	    if (user != null) {
	        return ResponseEntity.ok(user);
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
	}

}