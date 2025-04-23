package com.example.crud.config;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositorys.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = authentication.getName(); // username do login
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);
        }

        response.sendRedirect("/dashboard");
    }
}
