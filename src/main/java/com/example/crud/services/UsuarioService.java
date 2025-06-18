package com.example.crud.services;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.*;
import com.example.crud.requests.RequestUsuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private NegociacaoRepository negociacaoRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private ImovelRepository imovelRepo;

    @Autowired
    private ImobiliariaRepository imobiliariaRepo;

    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    public void registerUsuario(RequestUsuario data) {
        Usuario newUsuario = new Usuario(data);
        newUsuario.setSenha(encoder.encode(data.senha()));
        repository.save(newUsuario);
    }

    @Transactional
    public Usuario updateUsuario(RequestUsuario data) {
        Optional<Usuario> optionalUsuario = repository.findById(data.id());
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setNome(data.nome());
            usuario.setEmail(data.email());
            usuario.setSenha(encoder.encode(data.senha()));
            usuario.setTelefone(data.telefone());
            usuario.setData_cadastro(data.data_cadastro());
            return usuario;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public void deleteUsuarioComTudo(UUID usuarioId) {
        repository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        negociacaoRepo.deleteAllByUsuarioId(usuarioId);
        clienteRepo.deleteAllByUsuarioId(usuarioId);
        imovelRepo.deleteAllByUsuarioId(usuarioId);
        imobiliariaRepo.deleteAllByUsuarioId(usuarioId);


        repository.deleteById(usuarioId);
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

    public void salvar(Usuario i) {
        repository.save(i);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public Usuario findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    //TEMPORARIO
    public Usuario buscarUsuarioPorToken(String token) {
        return repository.findByTokenRecuperacaoSenha(token).orElse(null);
    }


    public boolean emailJaCadastrado(String email) {
        return repository.findByEmail(email).isPresent();
    }

    //perfil do usuário
    @Transactional
    public Usuario updateProfile(Usuario form) {
        Usuario atual = findById(form.getId());
        atual.setNome(form.getNome());
        atual.setTelefone(form.getTelefone());

        return repository.save(atual);
    }

    @Transactional
    public void updatePassword(UUID usuarioId, String novaSenha) {
        Usuario u = repository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // importante: encode aqui
        u.setSenha(encoder.encode(novaSenha));
        repository.save(u);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}

