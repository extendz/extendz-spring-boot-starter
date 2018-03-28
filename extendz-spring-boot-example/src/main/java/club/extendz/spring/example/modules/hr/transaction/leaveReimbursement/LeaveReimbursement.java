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
package club.extendz.spring.example.modules.hr.transaction.leaveReimbursement;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import club.extendz.spring.example.modules.hr.master.employee.Employee;
import lombok.Getter;
import lombok.Setter;

/***
 * @author Asitha Niranjan (asitha93@live.com)
 */
@Entity
@Getter
@Setter
public class LeaveReimbursement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long voucherNumber;

	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private Employee employee;

	@Temporal(TemporalType.DATE)
	private Date date;
	private String remarks;
	@Temporal(TemporalType.DATE)
	private Date effectiveDate;
}
