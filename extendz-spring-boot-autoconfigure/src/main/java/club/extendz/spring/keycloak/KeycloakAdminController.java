package club.extendz.spring.keycloak;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.extendz.spring.keycloak.dto.UserRepresentationResource;
import club.extendz.spring.keycloak.exceptions.UserDeletionFailedException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.data.rest.base-path}/keycloak/")
@ConditionalOnProperty(prefix = "keycloak", name = "auth-server-url")
public class KeycloakAdminController {

	private final KeycloakAdminService keycloakAdminService;
	private final UserRepresentationResourceAssembler assembler;

	@GetMapping("users")
	public PagedResources<UserRepresentation> getUsers(
			@RequestParam(required = false, name = "userName", defaultValue = "") String userName, Pageable pageable,
			PagedResourcesAssembler pagedAssembler) {
		Page<UserRepresentation> allUsers = keycloakAdminService.getAllUsers(userName, pageable);
		return pagedAssembler.toResource(allUsers, assembler);
	} // getUsers()

	@GetMapping("users/{id}")
	public UserRepresentationResource getUser(@PathVariable("id") String id) {
		UserRepresentation userRepresentation = keycloakAdminService.getUser(id);
		return this.assembler.toResource(userRepresentation);
	} // getUser()

	@PutMapping("users/{id}")
	public UserRepresentationResource updateUser(@RequestBody Resource<UserRepresentation> user) {
		String href = user.getId().getHref();
		String id = href.substring(href.lastIndexOf("/") + 1);
		UserRepresentation userRepresentation = keycloakAdminService.putUser(id, user.getContent());
		return this.assembler.toResource(userRepresentation);
	}// putUser()

	@DeleteMapping("users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
		try {
			keycloakAdminService.deleteUser(id);
			return ResponseEntity.status(204).build();
		} catch (UserDeletionFailedException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	} // deleteUser()

}// class