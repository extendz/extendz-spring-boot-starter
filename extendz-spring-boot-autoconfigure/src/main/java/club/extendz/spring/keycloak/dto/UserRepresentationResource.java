package club.extendz.spring.keycloak.dto;

import org.keycloak.representations.account.UserRepresentation;
import org.springframework.hateoas.Resources;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRepresentationResource extends Resources<UserRepresentation> {

	private String username;
	private String email;
	private Boolean enabled;

}
