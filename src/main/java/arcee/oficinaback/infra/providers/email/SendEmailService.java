package arcee.oficinaback.infra.providers.email;
import arcee.oficinaback.infra.providers.email.JavaMail.JavaMailService;
import arcee.oficinaback.infra.providers.email.Resend.ResendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {

    @Autowired
    JavaMailService javaMailService;

    @Autowired
    ResendService resendService;

    public boolean sendPasswordRecoveryEmail(String to, String code){

        var sendJavaMail = javaMailService.SendPasswordConfirmationEmail(to,code);

        if(!sendJavaMail){
            return resendService.SendPasswordEmail(to,code);
        }

        return true;
    }
}
