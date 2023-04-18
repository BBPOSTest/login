package com.kelvin.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.kelvin.application.db.UserRepo;
import com.kelvin.application.db.User;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses=UserRepo.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    CommandLineRunner runner(UserRepo userDao){
        return args -> {
            String email ="test456@456.ccc";
            
            User user = new User(email,"test1","test1",0);
            
            userDao.findUserByEmail(email)
                .ifPresentOrElse(users ->{
                    
                    System.out.println("already exists");
                    System.out.println(users);
        
                }, ()->{
                    //沒有的話就新增
                    userDao.insert(user);   
                    System.out.println("add user");
                });
        };
    }
}
