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
package club.extendz.spring.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import club.extendz.spring.example.modules.utils.ProfileUtils;

@SpringBootApplication(scanBasePackages = "club.extendz")
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "club.extendz.spring.example.modules.hr.master.department")
public class ExtendzSpringBootExampleApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ExtendzSpringBootExampleApplication.class,
		// args);
		SpringApplication app = new SpringApplication(ExtendzSpringBootExampleApplication.class);
		// Change the default profile to "dev" instead of "default".
		ProfileUtils.setAsDev(app);
		app.run(args);

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("https://extendz.github.io", "http://localhost:4200")
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedHeaders("*");
			}
		};
	} // corsConfigurer
}
