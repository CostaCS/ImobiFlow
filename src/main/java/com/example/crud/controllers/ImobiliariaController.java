package com.example.crud.controllers;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.requests.RequestImobiliaria;
import com.example.crud.services.ImobiliariaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/imobiliaria")
public class ImobiliariaController {

    @Autowired
    private ImobiliariaService imobiliariaService;

    @GetMapping
    public ResponseEntity getAllImobiliarias() {
        var allImobiliarias = imobiliariaService.getAllImobiliarias();
        return ResponseEntity.ok(allImobiliarias);
    }

    @PostMapping
    public ResponseEntity registerImobiliaria(@RequestBody @Valid RequestImobiliaria data){
        imobiliariaService.registerImobiliaria(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateImobiliaria(@RequestBody @Valid RequestImobiliaria data){
        var imobiliariaAtualizada = imobiliariaService.updateImobiliaria(data);
        return ResponseEntity.ok(imobiliariaAtualizada);
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteImobiliaria(@PathVariable UUID id){
        imobiliariaService.deleteImobiliaria(id);
        return ResponseEntity.noContent().build();
    }


}
