package com.example.crud.repositories;
//DAO

import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTokenRecuperacaoSenha(String tokenRecuperacaoSenha);

    boolean existsByEmail(String email);


}
