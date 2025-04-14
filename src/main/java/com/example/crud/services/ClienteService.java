package com.example.crud.services;

import com.example.crud.domain.entitys.Cliente;
import com.example.crud.repositorys.ClienteRepository;
import com.example.crud.requests.RequestCliente;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> getAllClientes() {
        return repository.findAll();
    }

    public void registerCliente(RequestCliente data) {
        Cliente newCliente = new Cliente(data);
        repository.save(newCliente);
    }

    @Transactional
    public Cliente updateCliente(RequestCliente data) {
        Optional<Cliente> optionalCliente = repository.findById(data.id());
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            cliente.setNome(data.nome());
            cliente.setEmail(data.email());
            cliente.setTelefone(data.telefone());
            cliente.setEndereco(data.endereco());
            return cliente;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public void deleteCliente(UUID id) {
        Optional<Cliente> optionalCliente = repository.findById(id);
        if (optionalCliente.isPresent()) {
            repository.delete(optionalCliente.get());
        } else {
            throw new EntityNotFoundException();
        }
    }
}

