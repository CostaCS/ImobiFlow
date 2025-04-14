package com.example.crud.controllers;

import com.example.crud.domain.entitys.Cliente;
import com.example.crud.requests.RequestCliente;
import com.example.crud.services.ClienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity getAllClientes() {
        var allClientes = clienteService.getAllClientes();
        return ResponseEntity.ok(allClientes);
    }

    @PostMapping
    public ResponseEntity registerCliente(@RequestBody @Valid RequestCliente data){
        clienteService.registerCliente(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateCliente(@RequestBody @Valid RequestCliente data){
        var clienteAtualizado = clienteService.updateCliente(data);
        return ResponseEntity.ok(clienteAtualizado);
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteCliente(@PathVariable UUID id){
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }


}

