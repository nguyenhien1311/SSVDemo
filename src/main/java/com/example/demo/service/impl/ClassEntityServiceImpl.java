package com.example.demo.service.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Messages;
import com.example.demo.entities.ClassEntity;
import com.example.demo.enums.ClassStatus;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.ClassEntityRepository;
import com.example.demo.request.classentity.ClassCreateRequest;
import com.example.demo.request.classentity.ClassUpdateRequest;
import com.example.demo.response.classes.ClassResponse;
import com.example.demo.service.ClassEntityService;
import com.example.demo.util.ClassEntityDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassEntityServiceImpl implements ClassEntityService {

	private final ClassEntityRepository repository;

	@Override
	public Page<ClassEntity> getAll(int skip, int limit, String name, int subjectId) {
		Page<ClassEntity> data = null;
		if (name != null && name != "") {
			data = repository.findClassEntitiesByClassNameLike("%" + name + "%",
					PageRequest.of(skip, limit, Sort.by(Sort.Direction.ASC, "status")));
		} else if(subjectId != 0) {
			data = repository.findClassEntitiesBySubjectId(subjectId, PageRequest.of(skip, limit, Sort.by(Sort.Direction.ASC, "status")));
		}else{
			data = repository.findAll(PageRequest.of(skip, limit, Sort.by(Sort.Direction.ASC, "status")));
		}
		return data;
	}

	@Override
	public ClassResponse addNew(ClassCreateRequest e) {
		ClassResponse respone = null;
		ClassEntity fromCreateRequest = ClassEntityDTO.fromCreateRequest(e);
		try {
			respone = ClassEntityDTO.getRespone(repository.save(fromCreateRequest));
		} catch (ParseException e2) {
			// TODO: handle exception
		}
		return respone;
	}

	@Override
	public ClassResponse updateEntity(ClassUpdateRequest e, int id) {
		ClassResponse respone = null;
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		ClassEntity fromUpdateRequest = ClassEntityDTO.fromUpdateRequest(e, id);

		try {
			respone = ClassEntityDTO.getRespone(repository.save(fromUpdateRequest));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return respone;
	}

	@Override
	public ClassResponse findById(int id) {
		ClassResponse respone = null;
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		try {
			respone = ClassEntityDTO.getRespone(findById.get());
		} catch (ParseException ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
		return respone;
	}

	@Override
	public void deleteEntity(int id) {
//		Optional<ClassEntity> findById = repository.findById(id);
//		if (findById.isEmpty()) {
//			throw new CustomException(ExceptionStringUtil.CLASS_NOT_FOUND + id);
//		}
//		ClassEntity entity = findById.get();
//		if (!entity.getStatus().equals(ClassStatus.CREATED)) {
//			throw new CustomException(ExceptionStringUtil.DELETE_CLASS_ERROR);
//		}
//		List<Student> findAllByClassId = studentRepository.findAllByClassId(id);
//		findAllByClassId.forEach(t -> {
//			t.setClassId(null);
//			studentRepository.save(t);
//		});
//
//		repository.delete(entity);
	}

	@Override
	public List<ClassResponse> getData() {
		List<ClassEntity> all = repository.findClassEntitiesByStatusNot(ClassStatus.ENDED);
		return ClassEntityDTO.getListResponse(all);
	}

	@Override
	public void endClass(int id) {
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		ClassEntity entity = findById.get();
		entity.setStatus(ClassStatus.ENDED);
		repository.save(entity);
	}

	@Override
	public List<ClassEntity> getAllBySubjectId(int id) {
		// TODO Auto-generated method stub
		List<ClassEntity> list = repository.findAllBySubjectId(id);
		
		return list;
	}


}
