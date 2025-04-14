package com.example.crud.controllers;

import com.example.crud.requests.RequestNegociacao;
import com.example.crud.services.NegociacaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/negociacao")
public class NegociacaoController {

    @Autowired
    private NegociacaoService negociacaoService;

    @GetMapping
    public ResponseEntity getAllNegociacoes() {
        var allNegociacoes = negociacaoService.getAllNegociacoes();
        return ResponseEntity.ok(allNegociacoes);
    }

    @PostMapping
    public ResponseEntity registerNegociacao(@RequestBody @Valid RequestNegociacao data){
        negociacaoService.registerNegociacao(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateNegociacao(@PathVariable UUID id, @RequestBody @Valid RequestNegociacao data) {
        var negociacaoAtualizada = negociacaoService.updateNegociacao(id, data);
        return ResponseEntity.ok(negociacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteNegociacao(@PathVariable UUID id){
        negociacaoService.deleteNegociacao(id);
        return ResponseEntity.noContent().build();
    }


}
