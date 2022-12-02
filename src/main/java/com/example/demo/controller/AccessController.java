package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.example.demo.request.user.TokenCheckRequest;
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
	public ResponseEntity<RootResponse> addUser(@Valid @RequestBody UserCreateRequest request) {
		boolean addUser = service.addUser(request);
		String result = addUser ? "Successed" : "Failed";
		RootResponse response = RootResponse.builder().code(200).message(Messages.MSG_004 + result).build();
		 return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/login")
	public RootResponse login(@Valid @RequestBody LoginRequest request) {
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
	
	@PostMapping("/expire")
	public RootResponse checkExpireDate(@Valid @RequestBody TokenCheckRequest request) {
		
		Integer code = service.checkExpireDate(request);
		return RootResponse.builder()
				.code(code)
				.build();
	}
	
	
}
