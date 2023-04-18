package com.kelvin.application.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationCheckFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
        if(!request.getServletPath().equals("/api/auth/signup") && !request.getServletPath().equals("/api/auth/login")){
            String authorHeader =  request.getHeader(AUTHORIZATION);
            String bearer ="Bearer ";
          
            if(authorHeader!= null && authorHeader.startsWith(bearer)){
                try{
                String token = authorHeader.substring(bearer.length());
                Claims claims = Jwts.parser().setSigningKey("MySecret").parseClaimsJws(token).getBody();

                System.out.println("JWT payload:"+claims.toString());

                filterChain.doFilter(request, response);
                
                }catch(Exception e){
                    System.err.println("Error : "+e);
                    response.setStatus(FORBIDDEN.value());
                    
                    Map<String, String> err = new HashMap<>();
                    err.put("jwt_err", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), err);
                }
            }else{
                response.setStatus(UNAUTHORIZED.value());
            }
        }else{
        	filterChain.doFilter(request, response);
        }
		
		
		
		
	}

}
