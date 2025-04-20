package com.example.crud.controllers;

import com.example.crud.requests.RequestUsuario;
import com.example.crud.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

        @Autowired
        private UsuarioService usuarioService;

        @GetMapping
        public ResponseEntity getAllUsuarios() {
            var allUsuarios = usuarioService.getAllUsuarios();
            return ResponseEntity.ok(allUsuarios);
        }

        @PostMapping
        public ResponseEntity registerUsuario(@RequestBody @Valid RequestUsuario data){
            usuarioService.registerUsuario(data);
            return ResponseEntity.ok().build();
        }

        @PutMapping
        @Transactional
        public ResponseEntity updateUsuario(@RequestBody @Valid RequestUsuario data){
            var usuarioAtualizado = usuarioService.updateUsuario(data);
            return ResponseEntity.ok(usuarioAtualizado);
        }

        @DeleteMapping("/{id}")
        @Transactional
        public ResponseEntity deleteProduct(@PathVariable UUID id){
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        }


}
