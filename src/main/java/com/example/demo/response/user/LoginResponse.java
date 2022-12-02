package com.example.demo.response.user;

import com.example.demo.response.ResponseBody;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class LoginResponse extends ResponseBody {
	private String userName;
	private int userId;
	private String token;
	private String authorities;
}
