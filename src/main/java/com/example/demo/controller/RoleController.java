package com.example.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.response.RootResponse;
import com.example.demo.response.role.ListRoleReponse;
import com.example.demo.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController extends AppController{
	private final RoleService service;
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/")
	public RootResponse getAll() {
		ListRoleReponse build = ListRoleReponse.builder()
				.list(service.getAll())
				.build();
		
		return sucess(build);
	}
}
