package club.extendz.spring.keycloak.dto;

import org.springframework.hateoas.Resources;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResource extends Resources<UserInfo> {

	private String sub;
	private String preferred_username;
	private String email;
	private String profile;
	private Boolean enabled;

	public UserInfoResource(UserInfo userInfo) {
		this.sub = userInfo.getSub();
		this.preferred_username = userInfo.getPreferred_username();
		this.email = userInfo.getEmail();
		this.profile = userInfo.getProfile();
		this.enabled = userInfo.getEnabled();
	}

}
