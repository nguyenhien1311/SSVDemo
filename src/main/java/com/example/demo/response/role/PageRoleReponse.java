package com.example.demo.response.role;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Role;
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
public class PageRoleReponse extends ResponseBody{
	Page<Role> pages;
}
