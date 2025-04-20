package com.example.crud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String MostrarLoginForm(){
        return "login";
    }

    @GetMapping("/cadastro")
    public String MostrarCadastro(){
        return "cadastro";
    }

    @GetMapping("/recuperarSenha")
    public String MostrarRecuperarSenha(){
        return "recuperarSenha";
    }

}
