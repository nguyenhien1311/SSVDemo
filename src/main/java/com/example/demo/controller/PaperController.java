package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Messages;
import com.example.demo.request.paper.PaperCreateRequest;
import com.example.demo.request.paper.PaperUpdateRequest;
import com.example.demo.service.PaperService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/paper")
public class PaperController {
	private final PaperService service;
	
	@PutMapping("/{id}")
	public ResponseEntity<?> savePaper(@PathVariable(name = "id")int id,
			@RequestBody PaperUpdateRequest request){
		boolean savePaper = service.savePaper(id, request.getMark());
		String result = savePaper ? "Successed" : "Failed";
		return new ResponseEntity<>(Messages.MSG_003 + result , HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createPaper(@RequestBody PaperCreateRequest request){
		String result = service.createPaper(request) ? "Successed" : "Failed";
		return new ResponseEntity<>(Messages.MSG_003  + result,HttpStatus.OK);
	}

}
