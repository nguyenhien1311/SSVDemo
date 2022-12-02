package com.example.demo.request.user;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.constant.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateRequest {
	@NotBlank(message = "Full name must not be empty")
	@Size(min = 5 , max =35 , message = "Name length at least 5 and max 35 characters!")
	private String name;
	
	@NotBlank(message = "Email must not be empty")
	@Email(message =Messages.MSG_015, regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	private String email;
	
	@NotBlank(message = "Phone number must not be empty")
	@Size(min = 10 , max = 10 , message = "Phone number need 10 numbers")
	private String phone;
	@NotBlank(message = "Username must not be empty")
	private String username;
	@NotBlank(message = "Password must not be empty")
	private String password;
	
	private List<Integer> roleIds;
}
