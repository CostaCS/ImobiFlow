package com.example.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Envia o e-mail de recuperação de senha
    public void enviarEmailRecuperacao(String destinatario, String token) {
        String assunto = "Recuperação de Senha - ImobiFlow";
        String link = "http://localhost:8080/usuario/resetar-senha?token=" + token;
        String corpo = "Olá!\n\nClique no link abaixo para redefinir sua senha:\n\n" + link +
                "\n\nSe você não solicitou isso, ignore este e-mail.";

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject(assunto);
        mensagem.setText(corpo);

        mailSender.send(mensagem);
    }
}
