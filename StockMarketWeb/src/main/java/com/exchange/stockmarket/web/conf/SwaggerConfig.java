package com.exchange.stockmarket.web.conf;

import com.exchange.stockmarket.web.annotation.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Harsh Gupta on {2/10/18}
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket stocksApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Document.class))
                .paths(PathSelectors.any())
                .build();
    }
}
