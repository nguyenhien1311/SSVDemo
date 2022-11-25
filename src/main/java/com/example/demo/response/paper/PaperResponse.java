package com.example.demo.response.paper;

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
public class PaperResponse extends ResponseBody{
	private int id;
	private String studentName;
	private String className;
	private Boolean isPass;
	private Double theory;
	private Double practice;
}
