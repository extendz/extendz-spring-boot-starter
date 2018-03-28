package club.extendz.spring.example.modules.hr.master.employee;

import java.sql.Date;

import org.springframework.data.rest.core.config.Projection;

public interface EmployeeProjections {

	@Projection(name = "dataTable", types = Employee.class)
	public interface EmployeeDataTableProjection {

		Date getDateOfBirth();
	}

}
