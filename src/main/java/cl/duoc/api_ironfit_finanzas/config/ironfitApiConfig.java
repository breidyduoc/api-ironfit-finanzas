package cl.duoc.api_ironfit_finanzas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ironfitApiConfig {
    @Bean
    public OpenAPI ironfitOpenAPi(){
        return new OpenAPI()
                .info(new Info()
                        .title ("API ironfit(finanzas) - Servicio de ironfit")
                        .description ("Gestiona el acceso de las finanzas - CRUD Completo")
                        .version ("3.0")
                        .contact(new Contact()
                                .name("DoucUC")
                                .email("soporte@duoc.cl"))
                        .license(new License()
                                .name("Uso academico"))
                );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
