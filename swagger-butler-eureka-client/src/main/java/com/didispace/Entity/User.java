package com.didispace.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "user_name", columnDefinition = " varchar(255) default '' comment '用户名' ")
	private String userName;
	@Column(name = "pass_word", columnDefinition = " varchar(255) default '' comment '密码' ")
	private String passWord;
	@Column(name = "age", columnDefinition = " int default null comment '年龄' ")
	private Integer age;
	@Column(name = "sex", columnDefinition = " varchar(2) default '0' comment '性别' ")
	private String sex;

	private Integer fileId;

	@Column(name = "name", columnDefinition = "  varchar(255) default '' comment '姓名' ")
	private Integer name;

}
