package club.extendz.spring.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/***
 * Spring Data rest need a separate configuration to enable CORS.
 * 
 * @author Randika Hapugoda
 *
 */
@Configuration
public class GlobalRepositoryRestConfigurer extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.getCorsRegistry().addMapping("/api/**").allowedOrigins("*");
	}

}