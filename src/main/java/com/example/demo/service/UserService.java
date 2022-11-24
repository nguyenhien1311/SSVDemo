package com.example.demo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.demo.entities.User;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.TokenCheckRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.response.user.LoginResponse;
import com.example.demo.response.user.RefreshTokenResponse;

public interface UserService {
	List<User> getAll();
	
	User getById(int id);
	
	boolean addUser(UserCreateRequest request);
	
	LoginResponse login(LoginRequest request);
	
	void logout(HttpServletRequest request , HttpServletResponse response);
	
	RefreshTokenResponse refreshToken(TokenCheckRequest request);

	Integer checkExpireDate(@Valid TokenCheckRequest request);
}
