package club.extendz.spring.keycloak;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import club.extendz.spring.ExtendzProperties;
import club.extendz.spring.keycloak.dto.UserInfo;
import club.extendz.spring.keycloak.dto.UserInfoResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableConfigurationProperties(ExtendzProperties.class)
public class UserRepresentationResourceAssembler extends ResourceAssemblerSupport<UserInfo, UserInfoResource> {

	private final ExtendzProperties extendzProperties;
	private final RepositoryRestMvcConfiguration repositoryRestMvcConfiguration;

	public UserRepresentationResourceAssembler(ExtendzProperties extendzProperties,
			RepositoryRestMvcConfiguration repositoryRestMvcConfiguration) {
		super(KeycloakAdminController.class, UserInfoResource.class);
		this.extendzProperties = extendzProperties;
		this.repositoryRestMvcConfiguration = repositoryRestMvcConfiguration;
	}

	@Override
	public UserInfoResource toResource(UserInfo userInfo) {
		UserInfoResource userInfoResource = new UserInfoResource(userInfo);

		Link link = linkTo(methodOn(KeycloakAdminController.class).getUser(userInfo.getSub())).withSelfRel();
		String replaceAll = link.getHref().replaceAll("/\\$\\{spring\\.data\\.rest\\.base-path\\}",
				this.repositoryRestMvcConfiguration.baseUri().getUri().toString());

		Link updated = new Link(replaceAll).withSelfRel();
		userInfoResource.add(updated);

		try {
			URL url = new URL(replaceAll);
			String path = extendzProperties.userProfileUrl + "/" + userInfo.getSub();
			URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), path, null, null);
			userInfoResource.setProfile(uri.toString());
		} catch (MalformedURLException | URISyntaxException e) {
			log.warn(e.getMessage());
		}

		return userInfoResource;
	} // toResource()

}
