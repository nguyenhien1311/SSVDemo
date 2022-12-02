package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Messages;
import com.example.demo.constant.ResponseCode;
import com.example.demo.request.paper.PaperCreateRequest;
import com.example.demo.request.paper.PaperUpdateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.service.PaperService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/paper")
public class PaperController extends AppController{
	private final PaperService service;
	@Secured({"ROLE_SUPERVISOR","ROLE_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<RootResponse> savePaper(@PathVariable(name = "id")int id,
			@RequestBody PaperUpdateRequest request){
		service.savePaper(id, request);
		RootResponse response = RootResponse.builder().code(ResponseCode.OK).message(Messages.MSG_033).build();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response);
	}
	@Secured({"ROLE_SUPERVISOR","ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/")
	public ResponseEntity<RootResponse> createPaper(@RequestBody PaperCreateRequest request){
		service.createPaper(request);
		RootResponse response = RootResponse.builder().code(ResponseCode.OK).message(Messages.MSG_034).build();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response);
	}

}
