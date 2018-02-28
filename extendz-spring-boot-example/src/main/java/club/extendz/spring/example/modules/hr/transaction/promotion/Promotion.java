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
package club.extendz.spring.example.modules.hr.transaction.promotion;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import club.extendz.spring.example.modules.hr.master.attendanceSetting.AttendanceSetting;
import club.extendz.spring.example.modules.hr.master.department.Department;
import club.extendz.spring.example.modules.hr.master.leaveSetting.LeaveSetting;
import club.extendz.spring.example.modules.hr.master.payCategory.PayCategory;
import lombok.Getter;
import lombok.Setter;

/***
 * @author Asitha Niranjan (asitha93@live.com)
 */
@Entity
@Getter
@Setter
public class Promotion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long voucherNumber;
	private Long emplyeeNumber;
	private Date entryDate;

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(joinColumns = {
			@JoinColumn(name = "promotion_id", referencedColumnName = "id", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "payCategory_id", referencedColumnName = "id", nullable = true) })
	private Set<PayCategory> payCategorys;

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(joinColumns = {
			@JoinColumn(name = "promotion_id", referencedColumnName = "id", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "department_id", referencedColumnName = "id", nullable = true) })
	private Set<Department> departments;
	// grade
	private String designation;
	@OneToOne
	private LeaveSetting leaveSetting;
	@OneToOne
	private AttendanceSetting attendanceSetting;
	private Integer numberOfannualAirTicketsEligible;
	private Integer numberOfannualAirTicketsEligibleForFamily;
	private Boolean companyOccommodation;
	private Boolean companyCar;
	private String remarks;
	private Date effectiveDate;

}
