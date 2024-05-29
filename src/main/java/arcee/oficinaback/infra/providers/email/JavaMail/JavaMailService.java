package arcee.oficinaback.infra.providers.email.JavaMail;

import arcee.oficinaback.configs.JavaMailConfig;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class JavaMailService {

    @Autowired
    JavaMailConfig javaMailConfig;


    public boolean SendPasswordConfirmationEmail(String to, String code){
        try{
            MimeMessage message = javaMailConfig.getJavaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setFrom(javaMailConfig.getFromEmail());
            helper.setSubject("Recuperação de Senha");
            helper.setTo(to);

            String template  = loadRecoveryPasswordTemplate();

            template = template.replace("#{code}", code);
            helper.setText(template, true);

            javaMailConfig.getJavaMailSender().send(message);

            return true;
        }
        catch (Exception exception){
            System.out.println("Falha ao Enviar o email");
            System.out.println(exception);

            return false;
        }
    }

    private String loadRecoveryPasswordTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("template-email.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

}
