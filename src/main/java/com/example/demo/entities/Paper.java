package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "Paper")
public class Paper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "class_id")
	private int classId;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "theory")
	private Double theory;
	@Column(name = "practice")
	private Double practice;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usere", referencedColumnName = "id")
	@JsonBackReference
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "class", referencedColumnName = "id")
	@JsonBackReference
	private ClassEntity classEntity;
}
