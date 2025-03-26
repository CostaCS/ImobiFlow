package com.example.crud.services;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositorys.UsuarioRepository;
import com.example.crud.requests.RequestUsuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    public void registerUsuario(RequestUsuario data) {
        Usuario newUsuario = new Usuario(data);
        repository.save(newUsuario);
    }

    @Transactional
    public Usuario updateUsuario(RequestUsuario data) {
        Optional<Usuario> optionalUsuario = repository.findById(data.id());
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setNome(data.nome());
            usuario.setEmail(data.email());
            usuario.setSenha(data.senha());
            usuario.setTelefone(data.telefone());
            usuario.setData_cadastro(data.data_cadastro());
            return usuario;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public void deleteUsuario(UUID id) {
        Optional<Usuario> optionalUsuario = repository.findById(id);
        if (optionalUsuario.isPresent()) {
            repository.delete(optionalUsuario.get());
        } else {
            throw new EntityNotFoundException();
        }
    }
}

