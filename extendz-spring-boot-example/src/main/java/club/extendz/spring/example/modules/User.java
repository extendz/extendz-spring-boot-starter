package club.extendz.spring.example.modules;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	private Long id;

	private String email;

}
