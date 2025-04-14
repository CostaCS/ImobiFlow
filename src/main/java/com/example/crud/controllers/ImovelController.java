package com.example.crud.controllers;

import com.example.crud.requests.RequestImovel;
import com.example.crud.domain.entitys.Imovel;
import com.example.crud.services.ImovelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/imovel")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @GetMapping
    public ResponseEntity<List<Imovel>> getAllImoveis() {
        List<Imovel> imoveis = imovelService.getAllImoveis();
        return ResponseEntity.ok(imoveis);
    }

    @PostMapping
    public ResponseEntity<Void> registerImovel(@RequestBody @Valid RequestImovel request) {
        imovelService.registerImovel(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imovel> updateImovel(@PathVariable UUID id, @RequestBody @Valid RequestImovel request) {
        Imovel imovelAtualizado = imovelService.updateImovel(id, request);
        return ResponseEntity.ok(imovelAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImovel(@PathVariable UUID id) {
        imovelService.deleteImovel(id);
        return ResponseEntity.noContent().build();
    }
}
