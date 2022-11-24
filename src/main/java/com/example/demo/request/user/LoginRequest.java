package com.example.demo.request.user;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
	@NotBlank(message = "Username must no tbe empty")
	private String username;
	@NotBlank(message = "Password must no tbe empty")
	private String password;
}
