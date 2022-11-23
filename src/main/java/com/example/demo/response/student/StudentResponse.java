package com.example.demo.response.student;

import com.example.demo.response.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class StudentResponse extends ResponseBody{
	private int id;
	private String name;
	private String email;
	private String phone;
	private boolean status;
	private boolean inClass;
}
