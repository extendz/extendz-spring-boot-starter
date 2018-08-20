package club.extendz.spring.keycloak;

import java.util.Arrays;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import club.extendz.spring.keycloak.dto.UserInfo;

public class UserRepresentationDto {

	public UserRepresentation getUserRepresentation(UserInfo userInfo) {
		UserRepresentation user = new UserRepresentation();
		user.setUsername(userInfo.getPreferred_username());
		user.setEmail(userInfo.getEmail());
		user.setEnabled(true);

		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
		credentialRepresentation.setValue(userInfo.getPassword());
		user.setCredentials(Arrays.asList(credentialRepresentation));

		return user;
	} // getUserRepresentation()

	public UserInfo toUserInfo(UserRepresentation representation) {
		UserInfo userInfo = new UserInfo();
		userInfo.setSub(representation.getId());
		userInfo.setEmail(representation.getEmail());
		userInfo.setPreferred_username(representation.getUsername());
		userInfo.setEnabled(representation.isEnabled());
		return userInfo;
	} // toUserInfo()

}
