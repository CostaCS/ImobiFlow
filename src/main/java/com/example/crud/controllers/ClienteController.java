package com.example.crud.controllers;

import com.example.crud.requests.RequestCliente;
import com.example.crud.services.ClienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import com.example.crud.domain.entitys.Cliente;
import org.springframework.ui.Model;

import java.util.UUID;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // 🔹 ROTAS API REST (JSON) -------------------------

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<?> getAllClientes() {
        var allClientes = clienteService.getAllClientes();
        return ResponseEntity.ok(allClientes);
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> registerCliente(@RequestBody @Valid RequestCliente data){
        clienteService.registerCliente(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> updateCliente(@RequestBody @Valid RequestCliente data){
        var clienteAtualizado = clienteService.updateCliente(data);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> deleteCliente(@PathVariable UUID id){
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 ROTAS HTML (Thymeleaf) -------------------------

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("cliente", new Cliente());
        return "clientes";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente) {
        clienteService.salvar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/deletar/{id}")
    public String deletarCliente(@PathVariable UUID id) {
        clienteService.deletar(id);
        return "redirect:/clientes";
    }
}
