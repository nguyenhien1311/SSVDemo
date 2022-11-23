package com.example.demo.response.student;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Student;
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
public class PageStudentResponse extends ResponseBody{
	private Page<Student> pages;
}
