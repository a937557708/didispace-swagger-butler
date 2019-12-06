package com.didispace.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "student")
@Table(name = "student")
public class Student {
	@Id
	@GeneratedValue
	private Integer id;

	private String password;
	private String name;
	private String gender;

	private String email;
	private String phone;
	private String major;

	private String dept;
	private String status;
	private String lastLoginTime;

}
