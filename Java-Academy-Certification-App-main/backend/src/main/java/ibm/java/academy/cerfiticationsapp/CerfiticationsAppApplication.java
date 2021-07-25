package ibm.java.academy.cerfiticationsapp;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@SpringBootApplication
@Log
@EnableSwagger2
@Import({ springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class })
public class CerfiticationsAppApplication extends SpringBootServletInitializer
		implements WebMvcConfigurer, RepositoryRestConfigurer {
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CerfiticationsAppApplication.class, args);
		// log.info(ctx.getBean(VoucherService.class).assignVoucherToUser(10020L,
		// 10011L).toString());
		// ctx.getBean(VoucherService.class).deleteVoucher(10020L);
		// ctx.getBean(CertificationService.class).updateAndSave(10004L, null,
		// Arrays.asList(20003L));
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@Autowired
	private EntityManager entityManager;

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(e -> e.getJavaType())
				.collect(Collectors.toList()).toArray(new Class[0]));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH",
				"OPTIONS");
	}
}