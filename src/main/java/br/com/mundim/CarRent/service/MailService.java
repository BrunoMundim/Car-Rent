package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.model.entity.Mail;
import br.com.mundim.CarRent.repository.MailRepository;
import br.com.mundim.CarRent.security.AuthenticationService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MailRepository mailRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine, MailRepository mailRepository, AuthenticationService authenticationService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.mailRepository = mailRepository;
        this.authenticationService = authenticationService;
    }

    public void sendEmailWithTemplate(String subject, String templateName, Object data) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            String to = authenticationService.findUserByBearer().getEmail();
            helper.setTo(to);

            Context context = new Context();
            context.setVariable("data", data);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            saveMailInDatabase(to, subject, htmlContent);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void saveMailInDatabase(String to, String subject, String message) {
        Mail mail = new Mail(to, subject, message);
        mailRepository.save(mail);
    }

}
