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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.extendz.spring.keycloak.dto.UserInfo;
import club.extendz.spring.keycloak.exceptions.UserAlreadyExistsException;
import club.extendz.spring.keycloak.exceptions.UserDeletionFailedException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.data.rest.base-path}/keycloak/")
@ConditionalOnProperty(prefix = "keycloak", name = "auth-server-url")
public class KeycloakAdminController {

	private final KeycloakAdminService keycloakAdminService;
	private final UserRepresentationResourceAssembler assembler;

	@PostMapping("users")
	public ResponseEntity<?> createUser(@RequestBody UserInfo userDto) {
		try {
			UserInfo userRepresentation = keycloakAdminService.createUser(userDto);
			return ResponseEntity.ok(this.assembler.toResource(userRepresentation));
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	} // createUser()

	@GetMapping("users")
	public PagedResources<UserInfo> getUsers(
			@RequestParam(required = false, name = "userName", defaultValue = "") String userName, Pageable pageable,
			PagedResourcesAssembler pagedAssembler) {
		Page<UserInfo> users = keycloakAdminService.getUsers(userName, pageable);
		return pagedAssembler.toResource(users, assembler);
	} // getUsers()

	@GetMapping("users/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") String id) {
		UserInfo userInfo = keycloakAdminService.getUser(id);
		return ResponseEntity.ok(this.assembler.toResource(userInfo));
	} // getUser()

	@PutMapping("users/{id}")
	public ResponseEntity<?> updateUser(@RequestBody Resource<UserRepresentation> user) {
		String href = user.getId().getHref();
		String id = href.substring(href.lastIndexOf("/") + 1);
		UserInfo userInfo = keycloakAdminService.putUser(id, user.getContent());
		return ResponseEntity.ok(this.assembler.toResource(userInfo));
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