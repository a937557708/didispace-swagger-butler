package com.didispace.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "teacher")
public class Teacher {
	@Id
	@GeneratedValue
	private Integer id;
	private String password;
	private String name;
	private String gender;
	private String lastLoginTime;

	private String email;
	private String phone;
	private String type;
}
