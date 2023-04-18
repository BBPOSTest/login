package com.kelvin.application;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.kelvin.application.filter.AuthorizationCheckFilter;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplicationTests {	
	private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
	
    @Autowired  
    private WebApplicationContext webApplicationContext; 
    
    @Test
    public void shouldAddUserRecordviaSignUp() throws Exception {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("email", "abc@test.com");
    	map.put("userAccount", "test2");
    	map.put("userPassword", "test2");
    	
    	MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
    			                       .contentType(MediaType.APPLICATION_JSON)
    			                       .content(toJson(map))).andReturn();
    	
    	MockHttpServletResponse response = result.getResponse();
    	String str = response.getContentAsString();
    	
    	System.out.println("Test shouldAddUserRecordviaSignUp");
    	System.out.println("=====================================");
    	System.out.println(str);
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(str, "test2 created");
    	Assert.assertEquals(200,response.getStatus());
    }
	
    @Test
    public void shouldAbletoLoginwithJWTTokenReturnOnvalidUser() throws Exception {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("email", "test456@456.ccc");
    	map.put("userAccount", "test1");
    	map.put("userPassword", "test1");
    	
    	
    	MockHttpServletResponse response = getJWTToken(toJson(map));
    	String str = response.getContentAsString();
    	
    	System.out.println("Test shouldAbletoLoginwithJWTTokenReturnOnvalidUser");
    	System.out.println("=====================================");
    	System.out.println(str);
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(200,response.getStatus());
    }
    
    @Test
    public void shouldbeFailureToLoginwithNullMessageStringReturnWhenOneparameterIsEmpty() throws Exception {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("email", "");
    	map.put("userAccount", "test2");
    	map.put("userPassword", "test2");
    	
    	MockHttpServletResponse response = getJWTToken(toJson(map));
    	String str = response.getContentAsString();
    	
    	System.out.println("Test shouldbeFailureToLoginwithNullMessageStringReturnWhenOneparameterIsEmpty");
    	System.out.println("=====================================");
    	System.out.println(str);
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(str, "null data");
    	Assert.assertEquals(200,response.getStatus());
    }
    
    @Test
    public void shouldbeFailureToLoginwithErrorCodeReturnWhenVerificationInValid() throws Exception {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("email", "abc@test.com");
    	map.put("userAccount", "test2");
    	map.put("userPassword", "test3");
    	
    	MockHttpServletResponse response = getJWTToken(toJson(map));
    	String str = response.getContentAsString();
    	
    	System.out.println("Test shouldbeFailureToLoginwithErrorCodeReturnWhenVerificationInValid");
    	System.out.println("=====================================");
    	System.out.println(str);
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(str, "0003");
    	Assert.assertEquals(200,response.getStatus());
    }
    
    @Test
    public void shouldbeFailureToRetrieveAllDBDataInAbsentofJWTToken() throws Exception {
    	MockHttpServletResponse response = retrieveMongoDBData("/api/test/all", null, null);
    	
    	System.out.println("Test shouldbeFailureToRetrieveAllDBDataInAbsentofJWTToken");
    	System.out.println("=====================================");
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(401,response.getStatus());
    }
    
    @Test
    public void shouldbeFailureToRetrieveADataInAbsentofJWTToken() throws Exception {
    	MockHttpServletResponse response = retrieveMongoDBData("/api/test/user", "test456@456.ccc", null);
    	System.out.println("Test shouldbeFailureToRetrieveADataInAbsentofJWTToken");
    	System.out.println("=====================================");
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(401,response.getStatus());
    }
    
    @Test
    public void shouldbeSuccessToRetrieveAllDBDataWhenJWTTokenValid() throws Exception {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("email", "test456@456.ccc");
    	map.put("userAccount", "test1");
    	map.put("userPassword", "test1");
    	
    	MockHttpServletResponse loginAuthResponse = getJWTToken(toJson(map));
    	String jwtToken = loginAuthResponse.getContentAsString();
    	
    	MockHttpServletResponse response = retrieveMongoDBData("/api/test/all", null, jwtToken);
    	String str = response.getContentAsString();
    	
    	System.out.println("Test shouldbeSuccessToRetrieveAllDBDataWhenJWTTokenValid");
    	System.out.println("=====================================");
    	System.out.println(str);
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(200,response.getStatus());
    }
    
    @Test
    public void shouldbeSuccessToRetrieveADBDataWhenJWTTokenValid() throws Exception {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("email", "test456@456.ccc");
    	map.put("userAccount", "test1");
    	map.put("userPassword", "test1");
    	
    	MockHttpServletResponse loginAuthResponse = getJWTToken(toJson(map));
    	String jwtToken = loginAuthResponse.getContentAsString();
    	
    	MockHttpServletResponse response = retrieveMongoDBData("/api/test/user", "abc@test.com", jwtToken);
    	String str = response.getContentAsString();
    	
    	System.out.println("Test shouldbeSuccessToRetrieveADBDataWhenJWTTokenValid");
    	System.out.println("=====================================");
    	System.out.println(str);
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(str, "test2,abc@test.com,0");
    	Assert.assertEquals(200,response.getStatus());
    }
    
    
    @Test
    public void shouldbeFailureToRetrieveAllDBDataBecauseofJWTTokenExpire() throws Exception {
    	String expireToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNAdGVzdC5jb20iLCJleHAiOjE2ODE3ODgyNjZ9.HTswbpC9oLM2ZA8M8IQLekG5p449TQxRiWQHOkBipaxojsoK5BYNAqSijv5eJdHJTxuKR1BvFx5fuN7H1CFhXA";
    	MockHttpServletResponse response = retrieveMongoDBData("/api/test/all", null, expireToken);
    	
    	System.out.println("Test shouldbeFailureToRetrieveAllDBDataInAbsentofJWTTokenExpire");
    	System.out.println("=====================================");
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	
    	Assert.assertEquals(403,response.getStatus());
    }
    
    @Test
    public void shouldbeFailureToRetrieveADataBecauseofJWTTokenExpires() throws Exception {
    	String expireToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNAdGVzdC5jb20iLCJleHAiOjE2ODE3ODgyNjZ9.HTswbpC9oLM2ZA8M8IQLekG5p449TQxRiWQHOkBipaxojsoK5BYNAqSijv5eJdHJTxuKR1BvFx5fuN7H1CFhXA";
    	MockHttpServletResponse response = retrieveMongoDBData("/api/test/user", "abc@test.com", expireToken);
    	
    	System.out.println("Test shouldbeFailureToRetrieveADataInAbsentofJWTTokenExpires");
    	System.out.println("=====================================");
    	System.out.println(response.getStatus());
    	System.out.println("=====================================");
    	
    	Assert.assertEquals(403,response.getStatus());
    }
    
    private MockHttpServletResponse retrieveMongoDBData(String url, String param, String jwtToken) throws Exception{
    	MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(new AuthorizationCheckFilter()).build();
    	MvcResult result = null;
    	
    	if (jwtToken == null) {
    		if(param == null)
        		result = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        	else		                       
        		result = mvc.perform(MockMvcRequestBuilders.get(url).param("email", param)).andReturn();
    	}
    	else {
    		if(param == null)
        		result = mvc.perform(MockMvcRequestBuilders.get(url).header(HEADER_STRING, TOKEN_PREFIX + jwtToken)).andReturn();
        	else		                       
        		result = mvc.perform(MockMvcRequestBuilders.get(url).param("email", param).header(HEADER_STRING, "Bearer " + jwtToken)).andReturn();
    	}
    	
    			
    	return result.getResponse();
    }
    
    
    
    private MockHttpServletResponse getJWTToken(String jsonStr) throws Exception{
    	MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
    			                       .contentType(MediaType.APPLICATION_JSON)
    			                       .content(jsonStr)).andReturn();
    	
    	return result.getResponse();
    }
    
    
    private String toJson(Map<String, String> map) throws Exception{
    	JSONObject json = new JSONObject(map);
    	return json.toString();
    }
}
