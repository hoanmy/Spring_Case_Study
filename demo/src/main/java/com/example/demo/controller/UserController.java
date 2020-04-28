package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.IUserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

//	@Qualifier("userRepository")
	@Autowired
	private IUserRepository userRepo;
	
	@Autowired
	public UserController (IUserRepository userRepository) {
		this.userRepo = userRepository;
	}
	
	@GetMapping("/list")
    public ResponseEntity<Iterable<User>> getUsers(){
        return ResponseEntity.ok(userRepo.findAll());
    }
	
	@RequestMapping(value="/id", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Optional<User>> getUserById(@RequestBody String id) {
		return ResponseEntity.ok(userRepo.findById(id));
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	@ResponseBody
	public User addUser(@RequestBody User user) {
		return userRepo.insert(user);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	@ResponseBody
	public User updateUser(@RequestBody User user) {
		return userRepo.save(user);
	}

	
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteUser(@RequestBody User user) {
		try {
			userRepo.delete(user);
			return ResponseEntity.ok("Successful!");
		} catch(Exception e) {
			return (ResponseEntity<String>) ResponseEntity.badRequest();
		}
	}

}
