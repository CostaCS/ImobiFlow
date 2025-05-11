package com.example.crud.controllers;

import com.example.crud.requests.RequestNegociacao;
import com.example.crud.services.ClienteService;
import com.example.crud.services.ImobiliariaService;
import com.example.crud.services.ImovelService;
import com.example.crud.services.NegociacaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.example.crud.domain.entitys.Negociacao;

import java.util.UUID;

@Controller
@RequestMapping("/negociacao")
public class NegociacaoController {

    @Autowired
    private NegociacaoService negociacaoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ImovelService imovelService;

    @Autowired
    private ImobiliariaService imobiliariaService;

    @GetMapping
    @ResponseBody
    public ResponseEntity getAllNegociacoes() {
        var allNegociacoes = negociacaoService.getAllNegociacoes();
        return ResponseEntity.ok(allNegociacoes);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity registerNegociacao(@RequestBody @Valid RequestNegociacao data){
        negociacaoService.registerNegociacao(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @ResponseBody
    public ResponseEntity updateNegociacao(@PathVariable UUID id, @RequestBody @Valid RequestNegociacao data) {
        var negociacaoAtualizada = negociacaoService.updateNegociacao(id, data);
        return ResponseEntity.ok(negociacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseBody
    public ResponseEntity deleteNegociacao(@PathVariable UUID id){
        negociacaoService.deleteNegociacao(id);
        return ResponseEntity.noContent().build();
    }

    //Métodos html

    @GetMapping("/pagina")
    public String listarNegociacoes(Model model) {
        model.addAttribute("negociacoes", negociacaoService.getAllNegociacoes());
        model.addAttribute("negociacao", new Negociacao()); // para o formulário
        model.addAttribute("clientes", clienteService.getAllClientes());
        model.addAttribute("imoveis", imovelService.getAllImoveis());
        model.addAttribute("imobiliarias", imobiliariaService.getAllImobiliarias());
        return "negociacoes";
    }

    @PostMapping("/salvar")
    public String salvarNegociacao(@ModelAttribute Negociacao negociacao) {
        negociacaoService.salvarViaHTML(negociacao);
        return "redirect:/negociacao/pagina";
    }

    @GetMapping("/deletar/{id}")
    public String deletarNegociacao(@PathVariable UUID id) {
        negociacaoService.deleteNegociacao(id);
        return "redirect:/negociacao/pagina";
    }


}
