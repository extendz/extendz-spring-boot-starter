package club.extendz.spring.example.modules.core.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name", length = 50, unique = true, nullable = false)
	private String name;
	
	@Email
	@Column(nullable = false, unique = true)
	private String email;

}
