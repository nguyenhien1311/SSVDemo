package com.example.demo.response.student;

import java.util.List;

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
public class ListStudentResponse extends ResponseBody{
	private List<Student> list;
}
