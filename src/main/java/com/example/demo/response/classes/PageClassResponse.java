package com.example.demo.response.classes;

import org.springframework.data.domain.Page;

import com.example.demo.entities.ClassEntity;
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
public class PageClassResponse extends ResponseBody{
	private Page<ClassEntity> pages;
}
