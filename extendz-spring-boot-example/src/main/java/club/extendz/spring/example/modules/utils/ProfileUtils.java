/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package club.extendz.spring.example.modules.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;

/***
 * @author Asitha Niranjan (asitha93@live.com)
 */
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
