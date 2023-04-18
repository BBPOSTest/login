package com.kelvin.application.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kelvin.application.db.User;
import com.kelvin.application.db.UserRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class UserService {
	private final UserRepo userRepo;
    
    public UserService(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}
	public List<User> getAllUsers(){
        return userRepo.findAll();
    }
    public Optional<User> getOneUser(String email){
        return userRepo.findUserByEmail(email);
    }
    public void addUser(User user){
    	userRepo.insert(user);
    }
    
    public String verifyUser(String email,String userAcct , String userPasswd){
        /*
         JWT token login success
         0001 wrong email
         0002 wrong useracct
         0003 wrong passwd
         0004 acct locked
         9999 unknown error
         */
        Optional<User> user=userRepo.findUserByEmail(email);
        String result="9999";
        if(user.isPresent()){
            if(user.get().getUserAccount().equals(userAcct)){
                if(user.get().getUserPassword().equals(userPasswd)){
                	//Set 30 minutes expire
                	Date expireDate = new Date(System.currentTimeMillis()+ 30 * 60 * 1000);
                            String jwtToken = Jwts.builder()
                            .setSubject(email) 
                            .setExpiration(expireDate)
                            .signWith(SignatureAlgorithm.HS512,"MySecret")
                            .compact();
                	
                	result=jwtToken;
                }else{
                    result="0003";
                }
            }else{
                result="0002";
            }
        }else{
            result="0001";
        }
        return result;        
    }
}
