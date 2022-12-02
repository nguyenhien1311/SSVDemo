package com.example.demo.response.user;

import com.example.demo.response.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class UserResponse extends ResponseBody{

	private String fullName;
	

	private String email;
	

	private String phone;
	

	private int id;
	
	private String username;

	private String password;

}
