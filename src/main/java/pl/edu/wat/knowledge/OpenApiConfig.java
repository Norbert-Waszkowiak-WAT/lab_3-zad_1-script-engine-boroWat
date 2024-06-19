package pl.edu.wat.knowledge;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://localhost:8080/"))
                .addServersItem(new Server().url("http://my-new-server.com")) //Tutaj podmień na swoj url
                .info(new Info().title("My API").version("1.0"));
    }
}