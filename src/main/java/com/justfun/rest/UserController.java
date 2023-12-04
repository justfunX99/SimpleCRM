package com.justfun.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.justfun.entity.User;
import com.justfun.repository.UserRepository;

//@RestController("/")
public class UserController {
	
//	@Autowired
	private UserRepository userRepository;
	
//	@GetMapping("/user/{id}")
	public ResponseEntity<Object> hello(@PathVariable int id) {
		System.out.println("### hello");
		
		Optional<User> optUser = userRepository.findById(id);
		if(optUser.isPresent()) {
			return ResponseEntity.ok(optUser.get());
		}
		
		return ResponseEntity.badRequest().build();
	}

}
