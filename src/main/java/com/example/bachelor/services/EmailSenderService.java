package com.example.bachelor.services;

import com.example.bachelor.data.dto.ConfirmationTokenDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.entities.ConfirmationTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Service()
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    @Autowired
    Environment environment;

    @Autowired
    public EmailSenderService (JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Async
    public void sendMessageToAdmins(UserDto userDto, String uri) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        mailMessage.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        mailMessage.setSubject("Neue Anmeldung!");
        mailMessage.setText("Es gab eine neue Anmeldung von Nutzer " + userDto.getFirstName() + " "
                + userDto.getLastName() + " (" + userDto.getEmail() + ") vom Ortsverband " + userDto.getLocalbranch() + ". " +
                "Bitte prüfen und aktivieren! " + uri);

        sendEmail(mailMessage);
    }

    @Async
    public void sendConfirmationToken(UserDto userDto, ConfirmationTokenEntity confirmationToken, String uri) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userDto.getEmail());
        mailMessage.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        mailMessage.setSubject("Complete Registration!");

        String url = uri + "/confirm?token=" + confirmationToken.getConfirmationToken();
        mailMessage.setText("Um ihren Account zu bestätigen, klicken sie bitte hier: " + url + " (Wenn das" +
                " nicht funktioniert, kopieren sie den Link bitte in ihren Browser.)");

        sendEmail(mailMessage);
    }

    @Async
    public void sendActivationNotice(UserDto userDto, String uri) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userDto.getEmail());
        mailMessage.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        mailMessage.setSubject("Sie wurden freigeschaltet!");
        mailMessage.setText("Ihr Account wurde freigeschaltet. Sie können sich jetzt anmelden auf " + uri + " .");

        sendEmail(mailMessage);
    }
}
