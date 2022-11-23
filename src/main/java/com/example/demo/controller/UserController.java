package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Messages;
import com.example.demo.entities.User;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.response.user.LoginResponse;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController extends AppController {
	private final UserService service;
		
	
	@GetMapping("/")
	public ResponseEntity<?> getAll(){
		List<User> list = service.getAll();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id")int id){
		User list = service.getById(id);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> addUser(@RequestBody UserCreateRequest request){
		boolean addUser = service.addUser(request);
		String result = addUser?"Successed":"Failed";
		return new ResponseEntity<>(Messages.MSG_004 + result,HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public RootResponse login(@RequestBody LoginRequest request){
		 LoginResponse response = service.login(request);
		
		return sucess(response);
		
	}
	
	
}
