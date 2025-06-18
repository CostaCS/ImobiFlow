package com.example.crud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("erro", "Usuário ou senha inválidos.");
        }
        return "login";
    }

    @GetMapping("/cadastro")
    public String MostrarCadastro() {
        return "cadastro";
    }

    @GetMapping("/recuperarSenha")
    public String MostrarRecuperarSenha() {
        return "recuperarSenha";
    }

}
