package club.extendz.spring.keycloak.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/***
 * 
 * @author Randika Hapugoda
 * @see https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims
 *
 */
@Getter
@Setter
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1248398843634247774L;

	private String sub;

	private String preferred_username;

	private String password;

	private String email;

	private String profile;

	private Boolean enabled;

}
