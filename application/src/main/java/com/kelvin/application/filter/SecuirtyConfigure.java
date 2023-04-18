package com.kelvin.application.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;



@Configuration
public class SecuirtyConfigure {
	
	@Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.cors().and().csrf().disable()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    
	    http.addFilterBefore(new AuthorizationCheckFilter(), BasicAuthenticationFilter.class);
	    
	    return http.build();
	  }
}
