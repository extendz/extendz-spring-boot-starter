/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package club.extendz.spring.example.modules.hr.master.department;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Email;

import club.extendz.spring.example.modules.hr.master.employee.Employee;
import club.extendz.spring.modelMeta.annotations.Extendz;
import lombok.Getter;
import lombok.Setter;

/***
 * 
 * 
 * @author Asitha Niranjan (asitha93@live.com)
 * @author Randika Hapugoda
 */
@Entity
@Getter
@Setter
@Audited
public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -625725310374841893L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Extendz(title = true)
	@Column(unique = true, nullable = false)
	private String name;

	@Email
	private String email;

//	@OneToOne
//	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
//	private Employee manager;
//
//	@ManyToMany
//	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
//	private Set<Employee> employees;

}// class