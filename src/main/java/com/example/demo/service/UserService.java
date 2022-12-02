package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.User;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.TokenCheckRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.request.user.UserUpdateRequest;
import com.example.demo.response.paper.PaperResponse;
import com.example.demo.response.user.LoginResponse;
import com.example.demo.response.user.RefreshTokenResponse;
import com.example.demo.response.user.UserResponse;

public interface UserService {
	Page<User> getAll(int skip , int limit,String key , int role);
	
	UserResponse getById(int id);
	List<PaperResponse> getListPaper(int id);
	
	boolean addUser(UserCreateRequest request);
	boolean updateUser(int id,UserUpdateRequest request);
	
	boolean deleteUSer(int roleId, int userId);
	
	LoginResponse login(LoginRequest request);
	
	void logout(HttpServletRequest request , HttpServletResponse response);
	
	RefreshTokenResponse refreshToken(TokenCheckRequest request);

	Integer checkExpireDate(@Valid TokenCheckRequest request);

	void importFile(MultipartFile file,int num) throws IOException;
}
