package com.example.crud.domain.entitys;

import com.example.crud.requests.RequestUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Table(name = "usuario")
@Entity(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate data_cadastro;

    @Column(name = "token_recuperacao_senha")
    private String tokenRecuperacaoSenha;

    private boolean admin = false;


    public Usuario(RequestUsuario requestUsuario) {
        this.nome = requestUsuario.nome();
        this.email = requestUsuario.email();
        this.senha = requestUsuario.senha();
        this.telefone = requestUsuario.telefone();
        this.data_cadastro = requestUsuario.data_cadastro();
        this.admin = false;
    }

    // Métodos obrigatórios do UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // sem perfis/roles por enquanto
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

