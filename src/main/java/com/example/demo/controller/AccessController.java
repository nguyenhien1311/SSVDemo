package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Messages;
import com.example.demo.constant.ResponseCode;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.response.user.LoginResponse;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class AccessController extends AppController {
	private final UserService service;

	@PostMapping("/signin")
	public ResponseEntity<?> addUser(@RequestBody UserCreateRequest request) {
		boolean addUser = service.addUser(request);
		String result = addUser ? "Successed" : "Failed";
		return new ResponseEntity<>(Messages.MSG_004 + result, HttpStatus.OK);
	}

	@PostMapping("/login")
	public RootResponse login(@RequestBody LoginRequest request) {
		LoginResponse response = service.login(request);

		return sucess(response);

	}
	@GetMapping("/logout")
	public RootResponse logout(HttpServletRequest request,HttpServletResponse response) {
		
		service.logout(request, response);
		return RootResponse.builder()
				.code(ResponseCode.OK)
				.message(Messages.MSG_024)
				.build();
	}
	
	
	
	
	@GetMapping("/refresh_token")
	public RootResponse refreshToken(HttpServletRequest request,HttpServletResponse response) {
		
		service.logout(request, response);
		return RootResponse.builder()
				.code(ResponseCode.OK)
				.message(Messages.MSG_024)
				.build();
	}
}
