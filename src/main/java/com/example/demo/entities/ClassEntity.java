package com.example.demo.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.enums.ClassStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Class")
public class ClassEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "class_name")
	private String className;
	
	@Column(name="status")
	private ClassStatus status;
	
	@Column(name="subject_id")
	private int subjectId;
	
	@JsonManagedReference	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "classEntity")
	private List<Paper> papers;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subject_id", referencedColumnName = "id",insertable = false,updatable = false)
	@JsonManagedReference
	private Subject subject;
}
