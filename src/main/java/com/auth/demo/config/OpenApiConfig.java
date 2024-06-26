package com.auth.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Authentication Demo",
                version = "${api.version}",
                contact = @Contact(
                        name = "Ahmet Can Cicek", email = "", url = "https://ahmetcanicek.dev"
                ),
                license = @License(
                        name = "", url = ""
                ),
                description = "${api.description}"
        ),
        servers = @Server(
                url = "${api.server.url}",
                description = "Production"
        )
)
@Configuration
public class OpenApiConfig {


}
