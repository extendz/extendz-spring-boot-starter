package club.extendz.spring.keycloak;

import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import club.extendz.spring.keycloak.dto.BasicSignupUserDto;
import club.extendz.spring.keycloak.exceptions.UserAlreadyExistsException;
import club.extendz.spring.keycloak.exceptions.UserCreationFailedException;

@Service
@ConditionalOnProperty(prefix = "keycloak", name = "keycloak.auth-server-url")
public class KeycloakAdminService {

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;

	private Keycloak getKeyCloak() {
		return KeycloakBuilder.builder().serverUrl(authServerUrl).realm("master").username("admin").password("admin")
				.clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
				.build();
	} // getKeyCloak()

	private UsersResource getKeycloakUserResource() {
		return getRealmResource().users();
	} // getKeycloakUserResource()

	private RealmResource getRealmResource() {
		return getKeyCloak().realm(realm);
	}

	public void createUser(BasicSignupUserDto userDto) throws Exception {
		UsersResource userRessource = getKeycloakUserResource();
		UserRepresentation user = userDto.getUserRepresentation();

		// Create user
		Response result = userRessource.create(user);
		int statusId = result.getStatus();
		// Created status
		if (statusId == 201) {
			String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
			// set role
			RealmResource realmResource = getRealmResource();
			RoleRepresentation savedRoleRepresentation = realmResource.roles().get("USER").toRepresentation();
			realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));
		} else if (statusId == 409) {
			throw new UserAlreadyExistsException();
		} else {
			throw new UserCreationFailedException();
		}
	}// createUser()

}
