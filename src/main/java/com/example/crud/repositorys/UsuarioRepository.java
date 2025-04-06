package com.example.crud.repositorys;
//DAO
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    List<Usuario> findAllByActiveTrue();
}
