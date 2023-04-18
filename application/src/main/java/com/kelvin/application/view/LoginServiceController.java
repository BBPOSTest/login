package com.kelvin.application.view;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kelvin.application.db.User;
import com.kelvin.application.db.UserRepo;
import com.kelvin.application.service.UserService;


@EnableMongoRepositories
@RestController
public class LoginServiceController {
	@Autowired
    UserRepo userRepo;
		
	@PostMapping("/api/auth/signup")
	public ResponseEntity<String> signup(@RequestBody Map<String,String> map) {
		UserService userService = new UserService(userRepo);
		
		String email= map.get("email");
        String userAccount= map.get("userAccount");
        String userPassword= map.get("userPassword");
        Integer status = Integer.valueOf(0);
        
        userService.getOneUser(email)
        .ifPresentOrElse(users ->{
            
            System.out.println("already exists");
            System.out.println(users);

        }, ()->{
            
        	userService.addUser(new User(email,userAccount, userPassword, status));   
            System.out.println("add user");
        });
        
 		return ResponseEntity.ok(userAccount + " created");
	}
	
	@PostMapping("/api/auth/login")
	public ResponseEntity<String> userLogin(@RequestBody Map<String,String> map){
		UserService userService = new UserService(userRepo);
		
        String email= map.get("email");
        String userAccount= map.get("userAccount");
        String userPassword= map.get("userPassword");
        String result="null data";
        if(email.isBlank()||userAccount.isBlank()||userPassword.isBlank()){
            return ResponseEntity.ok(result);
        }else{
            result=userService.verifyUser(email, userAccount, userPassword);
            return ResponseEntity.ok(result);  
        }
    }   
}
