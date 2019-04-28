package testtask.exptracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import java.lang.annotation.Annotation;

@SpringBootApplication
public class ExptrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExptrackerApplication.class, args);
	}
}
