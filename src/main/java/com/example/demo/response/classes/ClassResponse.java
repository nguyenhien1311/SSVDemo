package com.example.demo.response.classes;

import com.example.demo.enums.ClassStatus;
import com.example.demo.response.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class ClassResponse extends ResponseBody{
	private int id;
	
	private String code;
	
	private String className;
	
	private ClassStatus status;
	
	private int subjectId;
}
