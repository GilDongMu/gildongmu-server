package codeit.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .pathsToMatch("/**")
                .group("v1")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gildongmu API")
                        .description("gildongmu api docs"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")));
    }

}
