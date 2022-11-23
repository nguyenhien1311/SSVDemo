package com.example.demo.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email",unique = true)
	private String email;
	
	@Column(name="phone",unique = true)
	private String phone;
	
	@Column(name = "status")
	private boolean status;	
	
	@Column(name = "in_class")
	private boolean inClass;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
	private List<Paper> papers;
}
