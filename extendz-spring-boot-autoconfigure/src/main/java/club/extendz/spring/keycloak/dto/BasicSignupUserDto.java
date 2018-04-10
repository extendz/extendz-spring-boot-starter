package club.extendz.spring.keycloak.dto;

import java.io.Serializable;
import java.util.Arrays;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicSignupUserDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1248398843634247774L;

	private String userName;

	private String password;

	private String email;
	
	public UserRepresentation getUserRepresentation() {
		UserRepresentation user = new UserRepresentation();
		user.setUsername(this.getUserName());
		user.setEmail(this.getEmail());
		user.setEnabled(true);

		CredentialRepresentation passwordCred = new CredentialRepresentation();
		passwordCred.setTemporary(false);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setValue(this.getPassword());
		user.setCredentials(Arrays.asList(passwordCred));

		return user;
	} // getUserRepresentation()

}
