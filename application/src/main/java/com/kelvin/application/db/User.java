package com.kelvin.application.db;

import com.mongodb.lang.NonNull;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
 

@Document  
public class User {
    @Id
    private String id;  
    @Indexed(unique = true)
    @NonNull
    private String email;
    @Indexed(unique = true)
    @NonNull
    private String userAccount;
    @NonNull
    private String userPassword;
 
    @NonNull
    private Integer status;
	public User(String email, String userAccount, String userPassword,
			Integer status) {
		super();
		
		this.email = email;
		this.userAccount = userAccount;
		this.userPassword = userPassword;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
