package com.didispace.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "topic")
public class Topic {
	@Id
	@GeneratedValue
	private Integer id;
	private String title;
	private String content;
	private String description;

	// private String teacherId;
	// private String studentId;
	private String secretaryId;
	private Integer valid;

	private Integer status;
	private Integer isOpen;
	private Integer isFinished;
	private Integer attachment;
	private Integer proposal;
	private Integer mediumDefense;

	private Integer thesis;
	private Integer code;
	private String record;
	private String time;

	@OneToOne(cascade = CascadeType.ALL) // People是关系的维护端，当删除 people，会级联删除 address
	@JoinColumn(name = "teacher_id", referencedColumnName = "id") // people中的address_id字段参考address表中的id字段
	private Teacher teacher;

	@OneToOne
	@JoinColumn(name = "studentId", referencedColumnName = "id")
	private Student student;
}
