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
package club.extendz.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/***
 * Configurations properties for extendz
 * 
 * @author Randika Hapugoda
 *
 */
@Data
@ConfigurationProperties(prefix = ExtendzProperties.EXTENDZ_PREFIX)
public class ExtendzProperties {

	public static final String EXTENDZ_PREFIX = "extendz";

	private boolean enabled = true;

	public String modelMetaEndpoint;

	/***
	 * Directory to save the data
	 */
	public String localDataDir;

	/*** Entity name for the maintaining the user profile */
	public String userProfileUrl;
}
