package club.extendz.spring.keycloak;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import club.extendz.spring.keycloak.dto.UserRepresentationResource;

@Component
public class UserRepresentationResourceAssembler
		extends ResourceAssemblerSupport<UserRepresentation, UserRepresentationResource> {

	RepositoryRestMvcConfiguration repositoryRestMvcConfiguration;

	public UserRepresentationResourceAssembler(RepositoryRestMvcConfiguration repositoryRestMvcConfiguration) {
		super(KeycloakAdminController.class, UserRepresentationResource.class);
		this.repositoryRestMvcConfiguration = repositoryRestMvcConfiguration;
	}

	@Override
	public UserRepresentationResource toResource(UserRepresentation userRepresentation) {
		UserRepresentationResource userRepresentationResource = new UserRepresentationResource();

		Link link = linkTo(methodOn(KeycloakAdminController.class).getUser(userRepresentation.getId())).withSelfRel();

		String replaceAll = link.getHref().replaceAll("/\\$\\{spring\\.data\\.rest\\.base-path\\}",
				this.repositoryRestMvcConfiguration.baseUri().getUri().toString());

		Link updated = new Link(replaceAll).withSelfRel();

		userRepresentationResource.add(updated);

		userRepresentationResource.setUsername(userRepresentation.getUsername());
		userRepresentationResource.setEmail(userRepresentation.getEmail());
		userRepresentationResource.setEnabled(userRepresentation.isEnabled());
		return userRepresentationResource;
	}

}
