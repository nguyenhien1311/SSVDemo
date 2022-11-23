package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Messages;
import com.example.demo.entities.Student;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.request.student.StudentCreateRequest;
import com.example.demo.request.student.StudentUpdateRequest;
import com.example.demo.response.student.StudentResponse;
import com.example.demo.service.StudentService;
import com.example.demo.util.StudentDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentRepository repository;

	@Override
	public Page<Student> getAll(int skip, int limit, String name) {
		if (name != null && name != "") {
			return repository.findAllByNameLikeIgnoreCase("%" + name + "%",
					PageRequest.of(skip, limit, Sort.by(Sort.Direction.DESC, "status")));
		}
		return repository.findAll(PageRequest.of(skip, limit, Sort.by(Sort.Direction.DESC, "status")));
	}

	@Override
	public StudentResponse addEntity(StudentCreateRequest s) {
		Student student = StudentDTO.fromCreateRequest(s);
		return StudentDTO.getResponse(repository.save(student));
	}

	@Override
	public StudentResponse updateEntity(StudentUpdateRequest s, int id) {
		Optional<Student> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + id);
		}
		Student student = StudentDTO.fromUpdateRequest(s, id,optional.get().isInClass());	
		return StudentDTO.getResponse(repository.save(student));
	}

	@Override
	public StudentResponse findById(int id) {
		Optional<Student> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + id);
		}
		StudentResponse response = StudentDTO.getResponse(optional.get());
		return response;
	}

	@Override
	public void deleteEntity(int id) {
		Optional<Student> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + id);
		}
		Student student = optional.get();
		if (student.getPapers() != null && student.getPapers().size() > 0) {
			student.setStatus(false);
			repository.save(student);
		}
		repository.delete(student);
	}

}
