package br.com.mundim.CarRent;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "jwt", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
		info = @Info(
				title = "Car Rent API",
				version = "1.0.0",
				description = "Bem-vindo à documentação da API do Aluguel de Carros! Esta documentação fornece informações detalhadas sobre os endpoints, parâmetros, solicitações e respostas da API para ajudá-lo a integrar e utilizar os recursos do Car Rent em seu aplicativo.",
				license = @License(name = "MIT License"),
				contact = @Contact(name = "Bruno Mundim", email = "brunomundimfranco@gmail.com")
		),
		servers = {@Server(url = "/", description = "Default Server URL")}
)
public class CarRentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentApplication.class, args);
	}

}
