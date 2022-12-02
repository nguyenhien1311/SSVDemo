package com.example.demo.util;

import java.text.ParseException;
import java.util.List;

import com.example.demo.constant.Messages;
import com.example.demo.entities.ClassEntity;
import com.example.demo.enums.ClassStatus;
import com.example.demo.exception.CustomException;
import com.example.demo.request.classentity.ClassCreateRequest;
import com.example.demo.request.classentity.ClassUpdateRequest;
import com.example.demo.response.classes.ClassResponse;

public class ClassEntityDTO {
	
	public static ClassResponse getRespone(ClassEntity e) throws ParseException {

		return ClassResponse.builder().id(e.getId()).className(e.getClassName()).status(e.getStatus())
				.startDate(DatetimeUtil.fromStringtoDate(e.getStartDate()))
				.endDate(DatetimeUtil.fromStringtoDate(e.getEndDate()))
				.subjectId(e.getSubjectId()).build();
	}

	public static ClassEntity fromCreateRequest(ClassCreateRequest e) {
		return ClassEntity.builder().className(e.getClassName())
				.startDate(DatetimeUtil.fromDatetoString(e.getStartDate()))
				.endDate(DatetimeUtil.fromDatetoString(e.getEndDate()))
				.status(ClassStatus.CREATED).subjectId(e.getSubjectId())
				.build();
	}

	public static ClassEntity fromUpdateRequest(ClassUpdateRequest e, ClassEntity entity) {
		entity.setClassName(e.getClassName());
		entity.setStartDate(DatetimeUtil.fromDatetoString(e.getStartDate()));
		entity.setEndDate(DatetimeUtil.fromDatetoString(e.getEndDate()));
		return entity;
	}

	public static List<ClassResponse> getListResponse(List<ClassEntity> list){
		return list.stream().map(t -> {
			try {
				return ClassResponse.builder().id(t.getId())
						.startDate(DatetimeUtil.fromStringtoDate(t.getStartDate()))
						.endDate(DatetimeUtil.fromStringtoDate(t.getEndDate()))
						.className(t.getClassName()).build();
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new CustomException(Messages.MSG_029);
			}
		}).toList();
	}


}
