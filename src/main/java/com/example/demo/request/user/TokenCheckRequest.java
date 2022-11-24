package com.example.demo.request.user;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenCheckRequest {
	@NotBlank(message = "Token can not be null")
	private String token;
}
