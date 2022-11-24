package com.example.demo.request.student;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.constant.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentCreateRequest {
	
	@NotBlank(message = "Name must not be blank")
	@Size(min = 5 , max =35 , message = "Name length at least 5 and max 35 characters!")
	private String name;
	
	@Email(message =Messages.MSG_015, regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	private String email;
	
	@Size(min = 10 , max = 10 , message = "Phone number need 10 numbers")
	private String phone;
	
}
