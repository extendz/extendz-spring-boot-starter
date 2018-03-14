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
package club.extendz.spring.example.modules.hr.master.employee;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import club.extendz.spring.example.modules.hr.master.enums.BloodGroup;
import club.extendz.spring.example.modules.hr.master.enums.Gender;
import club.extendz.spring.modelMeta.annotations.Extendz;
import club.extendz.spring.modelMeta.annotations.enums.InputType;
import lombok.Getter;
import lombok.Setter;

/***
 * @author Asitha Niranjan (asitha93@live.com)
 * @author Randika Hapugoda
 */
@Entity
@Getter
@Setter
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Extendz(title = true)
	@Column(nullable = false)
	private String number;

	@Temporal(TemporalType.DATE)
	private Date joinedDate;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	private String nationality;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private BloodGroup bloodGroup;

	private String passportNumber;

	private String residencePermitStatus;

	// salary payment mode
	private String RPNumber;

	private String bankAccountNumber;

	// IBAN
	private String bankName;

	@Transient
	@Extendz(type = InputType.FILE)
	private String profileImage;

	// beneficiary information
	private String address;

	// ******qualifications & experience

	// education details
	// previous experience history
	// skills
	// language proficiency
	private Boolean trainingAttended;

	// *****documents

}
