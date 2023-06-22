package com.javaspringboot.DevicesManagementSystemBackend.service.Impl;

import com.javaspringboot.DevicesManagementSystemBackend.model.PasswordResetToken;
import com.javaspringboot.DevicesManagementSystemBackend.model.User;
import com.javaspringboot.DevicesManagementSystemBackend.repository.PasswordTokenRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.UserRepository;
import com.javaspringboot.DevicesManagementSystemBackend.service.SendGridMailService;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;

@Service
public class SendGridMailServiceImpl implements SendGridMailService {

    private static final String SEND_GRID_ENDPOINT_SEND_EMAIL = "mail/send";

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${send_grid.api_key}")
    private String sendGridApiKey;

    @Value("${send_grid.from_email}")
    private String sendGridFromEmail;

    @Value("${send_grid.from_name}")
    private String sendGridFromName;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendMail(String subject, Content content, String sendTo) {
        Mail mail = buildMailToSend(subject, content, sendTo);
        send(mail);
    }

    private void send(Mail mail) {
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(SEND_GRID_ENDPOINT_SEND_EMAIL);
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Mail buildMailToSend(String subject, Content content, String emailTo) {
        Mail mail = new Mail();

        Email fromEmail = new Email();
        fromEmail.setName(sendGridFromName);
        fromEmail.setEmail(sendGridFromEmail);
        mail.setFrom(fromEmail);
        mail.setSubject(subject);
        Personalization personalization = new Personalization();
        Email to = new Email();
        to.setEmail(emailTo);
        personalization.addTo(to);
        mail.addPersonalization(personalization);
        mail.addContent(content);
        return mail;
    }

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }
    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }
}
