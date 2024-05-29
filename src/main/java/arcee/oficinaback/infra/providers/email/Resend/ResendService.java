package arcee.oficinaback.infra.providers.email.Resend;

import arcee.oficinaback.configs.ResendConfig;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ResendService {

    @Autowired
    ResendConfig resendConfig;


    public boolean SendPasswordEmail(String to, String code){

        Resend resend = new Resend(resendConfig.getApiKey());

        try {
            String template = loadRecoveryPasswordTemplate();

            template = template.replace("#{code}", code);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(resendConfig.getFromEmail())
                    .to(to)
                    .subject("Recuperação de Senha")
                    .html(template)
                    .build();

            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data);
            return true;
        } catch (ResendException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadRecoveryPasswordTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("template-email.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

}
