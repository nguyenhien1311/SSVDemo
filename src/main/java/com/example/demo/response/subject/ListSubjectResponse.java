package com.example.demo.response.subject;

import java.util.List;

import com.example.demo.entities.Subject;
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
public class ListSubjectResponse extends ResponseBody{
	List<Subject> list;
}
