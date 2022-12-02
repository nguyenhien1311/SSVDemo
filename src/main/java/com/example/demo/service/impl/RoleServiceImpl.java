package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

	private final RoleRepository repository;

	@Override
	public List<Role> getAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}
	
	

}
