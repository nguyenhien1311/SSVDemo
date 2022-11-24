package com.example.demo.controller;

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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.exception.CustomException;
import com.example.demo.response.ExceptionResponse;
import com.example.demo.response.RootResponse;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class ExceptionController{
	
	@ExceptionHandler(value = CustomException.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ExceptionResponse> exceptionHandling(CustomException ex){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ExceptionResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<ExceptionResponse> validExceptionHandling(MethodArgumentNotValidException ex,BindingResult bd){
		StringBuilder builder = new StringBuilder();
	    List<FieldError> errors = bd.getFieldErrors();
	    for (FieldError error : errors ) {
	       builder.append(error.getField() + " : " + error.getDefaultMessage() + "\n");
	    } 
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ExceptionResponse(builder.toString()));
	}
	
	@ExceptionHandler(value = SQLException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<ExceptionResponse> validExceptionHandling(SQLException ex){
		String[] split = ex.getMessage().split("=");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ExceptionResponse(split[1]));
	}
	
	@ExceptionHandler(value = ParseException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<ExceptionResponse> parseExceptionHandler(ParseException ex){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ExceptionResponse(ex.getMessage()));
	}
	@ExceptionHandler(value = ExpiredJwtException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> oasd(ExpiredJwtException ex){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(RootResponse.builder().code(401).message(ex.getMessage()).build());
	}
	
	
}
