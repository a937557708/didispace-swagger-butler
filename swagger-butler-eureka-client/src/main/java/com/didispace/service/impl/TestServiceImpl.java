package com.didispace.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.didispace.Entity.Student;
import com.didispace.Entity.Topic;

@Service
public class TestServiceImpl {

	@Autowired
	EntityManager entityManager;

	// 具体的实现代码
	public Specification<Topic> listAdvanceSpec() {
		return (Root<Topic> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			// 完成子查询
			Subquery subQuery = query.subquery(String.class);
			Root from = subQuery.from(Student.class);
			subQuery.select(from.get("id")).where(builder.equal(from.get("name"), "222"));
			return builder.and((root.get("studentId")).in(subQuery));
		};
	}

}
