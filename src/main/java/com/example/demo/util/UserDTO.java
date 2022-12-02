package com.example.demo.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.response.user.UserResponse;

public class UserDTO {
	
	@Autowired
	private static PasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A);
	
	public static User fromCreateRequest(UserCreateRequest request,List<Role> roles) {
		return User.builder()
				.username(request.getUsername())
				.password(encoder.encode(request.getPassword()))
				.roles(roles)
				.email(request.getEmail())
				.phone(request.getPhone())
				.fullName(request.getName())
				.status(true)
				.build();
	}
	
	public static UserResponse getResponse(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.email(user.getEmail())
				.fullName(user.getFullName())
				.phone(user.getPhone())
				.username(user.getUsername())
				.password("")
				.build();
	}
}
