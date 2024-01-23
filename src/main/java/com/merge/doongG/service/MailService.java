package com.merge.doongG.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender; // 프레임워크에서 제공하는 메일 발송 sender

    // 이메일, 제목, 내용 받아서 메일 발송
    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        emailSender.send(emailForm);
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}