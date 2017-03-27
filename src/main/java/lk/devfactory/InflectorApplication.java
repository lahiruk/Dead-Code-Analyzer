package lk.devfactory;

import io.swagger.inflector.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class InflectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(InflectorApplication.class, args);
	}


    @Bean
    Configuration configuration(ApplicationContext applicationContext)
    {
        Configuration configuration = Configuration.read();
        configuration.setControllerFactory((cls, operation) -> applicationContext.getBean(cls));
        return configuration;
    }

    /**
     * Since we're using both Actuator and Jersey, we need to use Springs
     * <a href="http://docs.spring.io/spring/docs/current/spring-framework-reference/html/cors.html#_filter_based_cors_support">Filter based CORS support</a>
     *
     * @return corsFilter
     */
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		//config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}





}
