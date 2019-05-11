package testtask.exptracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Collections;


@SpringBootApplication
@EnableSwagger2
public class ExptrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExptrackerApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select().apis(RequestHandlerSelectors
						.basePackage("testtask.exptracker.controller.rest")).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"Expenses tracker web application REST API Documentation",
				"This API allows you to manage expenses and users",
				"v1",
				"Terms of service",
				new Contact("Igor Chayun", "", "igor.chayun@gmail.com"),
				"License of API", "", Collections.emptyList());
	}
}
