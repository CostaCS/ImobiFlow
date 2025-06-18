package com.example.crud.service;

import com.example.crud.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    private String destinatario;
    private String token;

    @BeforeEach
    void setUp() {
        destinatario = "usuario@teste.com";
        token = "abc123";
    }

    @Test
    void testEnviarEmailRecuperacao() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.enviarEmailRecuperacao(destinatario, token);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage mensagemEnviada = captor.getValue();
        String corpoEsperado = "Olá!\n\nClique no link abaixo para redefinir sua senha:\n\n" +
                "http://localhost:8080/usuario/resetar-senha?token=" + token +
                "\n\nSe você não solicitou isso, ignore este e-mail.";

        // Verificações
        assertEquals(destinatario, mensagemEnviada.getTo()[0]);
        assertEquals("Recuperação de Senha - ImobiFlow", mensagemEnviada.getSubject());
        assertEquals(corpoEsperado, mensagemEnviada.getText());
    }
}
