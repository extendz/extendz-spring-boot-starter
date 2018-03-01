package club.extendz.spring.example.modules.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;

public class ProfileUtils {

	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

	public static void setAsDev(SpringApplication app) {
		Map<String, Object> defProperties = new HashMap<>();
		/*
		 * The default profile to use when no other profiles are defined This cannot be
		 * set in the <code>application.yml</code> file. See
		 * https://github.com/spring-projects/spring-boot/issues/1219
		 */
		defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
		app.setDefaultProperties(defProperties);
	}// End ()
}
