package club.extendz.spring.keycloak;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import club.extendz.spring.keycloak.dto.UserInfo;
import club.extendz.spring.keycloak.exceptions.UserAlreadyExistsException;
import club.extendz.spring.keycloak.exceptions.UserCreationFailedException;
import club.extendz.spring.keycloak.exceptions.UserDeletionFailedException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix="keycloak", name="auth-server-url")
public class KeycloakAdminService {

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;
	
	 @Value("${extendz.keycloak.admin}")
	 private String adminUserName;
	
	 @Value("${extendz.keycloak.password}")
	 private String adminPassword;

	private Keycloak getKeyCloak() {
		return KeycloakBuilder.builder().serverUrl(authServerUrl).realm("master").username(adminUserName).password(adminPassword)
				.clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
				.build();
	} // getKeyCloak()

	private UsersResource getKeycloakUserResource() {
		return getRealmResource().users();
	} // getKeycloakUserResource()

	private RealmResource getRealmResource() {
		return getKeyCloak().realm(realm);
	}

	public UserInfo getUserByUserName(String userName) {
		UsersResource userRessource = getKeycloakUserResource();
		List<UserRepresentation> search = userRessource.search(userName);
		return search.stream().findFirst().map(rep -> new UserRepresentationDto().toUserInfo(rep)).get();
	}

	public Page<UserInfo> getUsers(String userName, Pageable pageable) {
		UsersResource userRessource = getKeycloakUserResource();
		List<UserRepresentation> search = userRessource.search(userName, pageable.getPageNumber(),
				pageable.getPageSize());
		List<UserInfo> userInfos = search.stream().map(rep -> new UserRepresentationDto().toUserInfo(rep))
				.collect(Collectors.toList());
		return new PageImpl<UserInfo>(userInfos, pageable, userInfos.size());
	}

	public UserInfo createUser(UserInfo userInfo) throws Exception {
		UsersResource userRessource = getKeycloakUserResource();
		UserRepresentation user = new UserRepresentationDto().getUserRepresentation(userInfo);
		// Create user
		Response result = userRessource.create(user);
		int statusId = result.getStatus();

		// Created status
		if (statusId == 201) {
			String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
			userInfo.setSub(userId);
			return userInfo;
		} else if (statusId == 409) {
			throw new UserAlreadyExistsException();
		} else {
			throw new UserCreationFailedException();
		}
	}// createUser()

	public UserInfo getUser(String id) {
		UsersResource userRessource = getKeycloakUserResource();
		UserRepresentation representation = userRessource.get(id).toRepresentation();
		return new UserRepresentationDto().toUserInfo(representation);
	} // getUser

	public String deleteUser(String id) throws UserDeletionFailedException {
		UsersResource userRessource = getKeycloakUserResource();
		Response deleteResponse = userRessource.delete(id);
		int statusId = deleteResponse.getStatus();
		if (statusId != 204) {
			throw new UserDeletionFailedException();
		} else {
			return id;
		}
	} // deleteUser()

	public UserInfo putUser(String id, UserRepresentation userRepresentation) {
		UsersResource userRessource = getKeycloakUserResource();
		userRessource.get(id).update(userRepresentation);
		return new UserRepresentationDto().toUserInfo(userRepresentation);
	}// putUser()

}
