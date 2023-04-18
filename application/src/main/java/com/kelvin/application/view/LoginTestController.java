package com.kelvin.application.view;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelvin.application.db.User;
import com.kelvin.application.db.UserRepo;
import com.kelvin.application.service.UserService;

@RestController
public class LoginTestController {
	@Autowired
    UserRepo userRepo;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/api/test/all")
	public ResponseEntity<String> testAll() {
		
		List<User> users =  userService.getAllUsers();
		
		String out ="";
		for (User user : users) {
			out += user.getUserAccount() + "," + user.getEmail() + "," + user.getStatus() + "\r\n";
		}
		
		return ResponseEntity.ok(out);
	}
	
	@GetMapping("/api/test/user")
	public ResponseEntity<String> testUser(@RequestParam String email) {
		Optional<User> user = userService.getOneUser(email);
		User out = user.get();
		
		if (user.isPresent() == true) return ResponseEntity.ok(out.getUserAccount() + "," + out.getEmail() + "," + out.getStatus());
		else return ResponseEntity.ok("No record found");
	}
}
