package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.constant.Messages;
import com.example.demo.entities.ClassEntity;
import com.example.demo.entities.Paper;
import com.example.demo.entities.Student;
import com.example.demo.enums.ClassStatus;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.ClassEntityRepository;
import com.example.demo.repository.PaperRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.request.paper.PaperCreateRequest;
import com.example.demo.response.paper.PaperResponse;
import com.example.demo.service.PaperService;
import com.example.demo.util.PaperDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaperServiceImpl implements PaperService {

	private final PaperRepository repository;
	private final StudentRepository studentRepository;
	private final ClassEntityRepository classRepository;

	@Override
	public List<PaperResponse> getAllByClassId(int classId) {
		List<Paper> list = repository.findAllPapersByClassId(classId);
		return list.stream().map(t -> {
			return PaperDTO.getResponse(t);
		}).toList();
	}

	@Override
	public boolean savePaper(int id, int mark) {
		Optional<Paper> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_011 + id);
		}
		boolean result = (mark >= 5) ? true : false;
		Paper paper = findById.get();
		paper.setStatus(result);
		paper.setMark(Double.valueOf(mark));
		Student student = studentRepository.findById(paper.getStudentId()).get();
		student.setInClass(false);
		studentRepository.save(student);
		return (repository.save(paper) != null) ? true : false;
	}

	@Override
	public PaperResponse getByStudentIdAndClassId(int studentId, int classId) {
		Optional<Paper> paper = repository.findPaperByStudentIdAndClassId(studentId, classId);
		if (paper.isEmpty()) {
			throw new CustomException(Messages.MSG_012);
		}
		return PaperDTO.getResponse(paper.get());
	}

	@Override
	public List<PaperResponse> getAllByStudentId(int studentId) {
		List<Paper> list = repository.findPapersByStudentId(studentId);
		return list.stream().map(t -> {
			return PaperDTO.getResponse(t);
		}).toList();
	}

	@Override
	public boolean createPaper(PaperCreateRequest request) {
		int studentId = request.getStudentId();
		int classId = request.getClassId();
		Optional<Student> studentOptional = studentRepository.findById(studentId);
		if (studentOptional.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + studentId);
		}
		Optional<ClassEntity> classOptional = classRepository.findById(classId);
		if (classOptional.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + classId);
		}
		Optional<Paper> optional = repository.findPaperByStudentIdAndClassId(studentId, classId);
		if (optional.isPresent()) {
			throw new CustomException(Messages.MSG_020);
		}
		Paper paper = Paper.builder().studentId(studentId).student(studentOptional.get()).classId(classId)
				.classEntity(classOptional.get()).build();
		Student student = studentOptional.get();
		student.setInClass(true);
		studentRepository.save(student);
		ClassEntity classEntity = classOptional.get();
		classEntity.setStatus(ClassStatus.ONGOING);
		classRepository.save(classEntity);
		return (repository.save(paper) != null) ? true : false;
	}

}
