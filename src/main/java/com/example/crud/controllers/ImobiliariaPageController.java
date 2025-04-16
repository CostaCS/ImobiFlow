package com.example.crud.controllers;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.requests.RequestImobiliaria;
import com.example.crud.services.ImobiliariaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/imobiliarias")
public class ImobiliariaPageController {

    @Autowired
    private ImobiliariaService imobiliariaService;

    @GetMapping
    public String listarImobiliarias(Model model) {
        model.addAttribute("imobiliarias", imobiliariaService.getAllImobiliarias());
        model.addAttribute("imobiliaria", new Imobiliaria());
        model.addAttribute("modoEdicao", false);
        return "imobiliarias";
    }

    @PostMapping("/salvar")
    public String salvarImobiliaria(@ModelAttribute("imobiliaria") Imobiliaria i,  RedirectAttributes redirectAttributes) {
        imobiliariaService.salvarOuAtualizar(i);
        redirectAttributes.addFlashAttribute("mensagem", "Imobiliária cadastrada com sucesso!");
        return "redirect:/imobiliarias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable UUID id, Model model) {
        Imobiliaria im = imobiliariaService.getById(id);
        model.addAttribute("imobiliaria", im);
        model.addAttribute("abrirModal", true);
        model.addAttribute("modoEdicao", true);
        model.addAttribute("imobiliarias", imobiliariaService.getAllImobiliarias());
        return "imobiliarias";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable UUID id) {
        imobiliariaService.deleteImobiliaria(id);
        return "redirect:/imobiliarias";
    }
    @GetMapping("/novo")
    public String novaImobiliaria(Model model) {
        model.addAttribute("imobiliaria", new Imobiliaria());
        model.addAttribute("imobiliarias", imobiliariaService.getAllImobiliarias());
        model.addAttribute("modoEdicao", false);
        model.addAttribute("abrirModal", true);
        return "imobiliarias";
    }

}

