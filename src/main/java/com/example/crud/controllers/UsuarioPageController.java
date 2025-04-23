package com.example.crud.controllers;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.crud.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.util.UUID;


@Controller
@RequestMapping("/usuario")
public class UsuarioPageController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String salvarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setData_cadastro(LocalDate.now());
        usuarioService.salvar(usuario);
        return "redirect:/login";
    }

    @GetMapping("/recuperar-senha")
    public String mostrarRecuperarSenha() {
        System.out.println("Exibindo tela de recuperação de senha");
        return "recuperarSenha";
    }


    @PostMapping("/recuperar-senha")
    public String processarRecuperacaoSenha(@RequestParam String email,Model model ,RedirectAttributes redirectAttributes){
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null){
            redirectAttributes.addFlashAttribute("erro","E-mail não encontrado.");
            return "redirect:/usuario/recuperar-senha";
        }

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacaoSenha(token);
        usuarioService.salvar(usuario);

        emailService.enviarEmailRecuperacao(usuario.getEmail(), token);

        model.addAttribute("mensagem", "Um link de recuperação foi enviado para seu e-mail.");

        return "redirect:/usuario/recuperar-senha";
    }

    @GetMapping("/resetar-senha")
    public String mostrarTelaReset(@RequestParam String token, Model model){
        model.addAttribute("token", token);
        return "resetarSenha";
    }

    @PostMapping("/resetar-senha")
    public String processarResetSenha(
            @RequestParam String token,
            @RequestParam String novaSenha,
            @RequestParam String confirmarSenha,
            RedirectAttributes redirectAttributes) {


        if (!novaSenha.equals(confirmarSenha)){
            redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem.");
            return "redirect:/usuario/resetar-senha?token=" + token;
        }

        Usuario usuario = usuarioService.buscarUsuarioPorToken(token);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("erro", "Token inválido ou expirado");
            return "redirect:/usuario/recuperar-senha";

        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setTokenRecuperacaoSenha(null); // Limpa token
        usuarioService.salvar(usuario);


        redirectAttributes.addFlashAttribute("mensagem", "Senha redefinida com sucesso!");
        return "redirect:/login";

    }

}
