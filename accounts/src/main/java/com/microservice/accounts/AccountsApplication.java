package com.microservice.accounts;

import com.microservice.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
/*@ComponentScans({ @ComponentScan("com.microservice.accounts.controller") })
@EnableJpaRepositories("com.microservice.accounts.repository")
@EntityScan("com.microservice.accounts.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "YourBank Accounts microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Zunaid Ansari",
						email = "mohdzunaidalameenansari@gmail.com",
						url = "https://www.microservice.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.microservice.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "YourBank Accounts microservice REST API Documentation",
				url = "https://www.microservice.com/swagger-ui.html"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
