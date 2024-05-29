package arcee.oficinaback.configs;

import arcee.oficinaback.configs.security.JWTConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableConfigurationProperties({FileBrowserConfig.class,S3Config.class, ResendConfig.class, JavaMailConfig.class, JWTConfig.class})
public class AppConfig {


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public CorsConfig corsConfig(){return new CorsConfig();};
}
