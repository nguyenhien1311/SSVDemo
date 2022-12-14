package com.example.demo.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.exception.CustomException;
import com.example.demo.response.RootResponse;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(value = CustomException.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<RootResponse> exceptionHandling(CustomException ex){
		RootResponse build = RootResponse.builder().code(200).message(ex.getMessage()).build();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(build);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<RootResponse> validExceptionHandling(MethodArgumentNotValidException ex,
			BindingResult bd) {
		StringBuilder builder = new StringBuilder();
		List<FieldError> errors = bd.getFieldErrors();
		for (FieldError error : errors) {
			builder.append(error.getField() + " : " + error.getDefaultMessage() + " \n ");
		}
		RootResponse build = RootResponse.builder().code(200).message(builder.toString()).build();
		return ResponseEntity.status(HttpStatus.OK).body(build);
	}

	@ExceptionHandler(value = SQLException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<RootResponse> validExceptionHandling(SQLException ex) {
		String[] split = ex.getMessage().split("=");
		RootResponse build = RootResponse.builder().code(200).message(split[1]).build();
		return ResponseEntity.status(HttpStatus.OK).body(build);
	}

	@ExceptionHandler(value = ParseException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<RootResponse> parseExceptionHandler(ParseException ex) {
		RootResponse build = RootResponse.builder().code(200).message(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.OK).body(build);
	}

	@ExceptionHandler(value = ExpiredJwtException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<RootResponse> expiredJwtHandle(ExpiredJwtException ex) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(RootResponse.builder().code(401).message(ex.getMessage()).build());
	}

	@ExceptionHandler(value = IOException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<RootResponse> fileExHandle(IOException ex) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(RootResponse.builder().code(401).message(ex.getMessage()).build());
	}

}
