package com.example.demo.util;

import java.text.ParseException;
import java.util.List;

import com.example.demo.entities.ClassEntity;
import com.example.demo.enums.ClassStatus;
import com.example.demo.request.classentity.ClassCreateRequest;
import com.example.demo.request.classentity.ClassUpdateRequest;
import com.example.demo.response.classes.ClassResponse;

public class ClassEntityDTO {
	public static ClassResponse getRespone(ClassEntity e) throws ParseException {

		return ClassResponse.builder()
				.id(e.getId())
				.code(e.getCode())
				.className(e.getClassName())
				.status(e.getStatus())
				.subjectId(e.getSubjectId())
				.build();
	}

	public static ClassEntity fromCreateRequest(ClassCreateRequest e) {
		return ClassEntity.builder()
				.code(e.getCode())
				.className(e.getClassName())
				.status(ClassStatus.CREATED)
				.subjectId(e.getSubjectId())
				.build();
	}

	public static ClassEntity fromUpdateRequest(ClassUpdateRequest e, int id) {
		return ClassEntity.builder()
				.id(id)
				.code(e.getCode())
				.className(e.getClassName())
				.status(ClassStatus.CREATED)
				.subjectId(e.getSubjectId())
				.build();
	}

	public static List<ClassResponse> getListResponse(List<ClassEntity> list) {
		return list.stream().map(t -> {
			return ClassResponse.builder()
					.id(t.getId())
					.className(t.getClassName())
					.code(t.getCode())
					.build();
		}).toList();
	}


//	public static List<ClassEntity> updateStatusListClass(Page<ClassEntity> data) {
//		return data.toList().stream().map(t -> {
//			return ClassEntity.builder()
//					.id(t.getId())
//					.code(t.getCode())
//					.className(t.getClassName())
//					.status(updateStatus(t))
//					.build();
//		}).toList();
//	}

//	public static ClassStatus updateStatus(ClassEntity c) {
//		ClassStatus result = null;
//		if (!c.getStatus().equals(ClassStatus.ENDED)) {
//			try {
//				Date endDate = DatetimeUtil.fromStringtoDate(c.getEndDate());
//				Date startDate = DatetimeUtil.fromStringtoDate(c.getStartDate());
//				if (DatetimeUtil.NOW.after(endDate)) {
//					result = ClassStatus.ENDED;
//				} else if (DatetimeUtil.NOW.after(startDate) && DatetimeUtil.NOW.before(endDate)) {
//					result = ClassStatus.ONGOING;
//				} else {
//					result = ClassStatus.CREATED;
//				}
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return result;
//		}
//		return ClassStatus.ENDED;
//	}
}
