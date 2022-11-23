package com.example.demo.request.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentUpdateRequest {
	private String name;
	private String email;
	private String phone;
	
}
