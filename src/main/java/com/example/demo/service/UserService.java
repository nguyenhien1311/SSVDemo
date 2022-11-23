package com.example.demo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.entities.User;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.response.user.LoginResponse;

public interface UserService {
	List<User> getAll();
	
	User getById(int id);
	
	boolean addUser(UserCreateRequest request);
	
	LoginResponse login(LoginRequest request);
	
	void logout(HttpServletRequest request , HttpServletResponse response);
}
