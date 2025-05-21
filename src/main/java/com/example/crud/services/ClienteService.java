    package com.example.crud.services;

    import com.example.crud.domain.entitys.Cliente;
    import com.example.crud.repositorys.ClienteRepository;
    import com.example.crud.requests.RequestCliente;
    import jakarta.persistence.EntityNotFoundException;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import com.example.crud.domain.entitys.Usuario;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Page;
    import com.example.crud.repositorys.ClienteRepository;



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

        //Service para HTML

        public Page<Cliente> listarTodos(Pageable pageable) {
            return repository.findAll(pageable);
        }

        public void salvar(Cliente cliente, Usuario usuario) {
            Optional<Cliente> existente = repository.findByEmailIgnoreCase(cliente.getEmail());
            if (existente.isPresent()) {
                throw new IllegalArgumentException("Já existe um cliente com este e-mail.");
            }
            cliente.setUsuario(usuario); // associa o usuário logado
            repository.save(cliente);
        }



        public void deletar(UUID id) {
            repository.deleteById(id);
        }

        public Cliente buscarPorId(UUID id) {
            return repository.findById(id).orElse(null);
        }

        public Page<Cliente> buscarPorNomeOuEmail(String termo, Pageable pageable) {
            return repository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(termo, termo, pageable);
        }

        public Page<Cliente> listarTodosPorUsuario(Usuario usuario, Pageable pageable) {
            return repository.findByUsuario(usuario, pageable);
        }

        public Page<Cliente> buscarPorNomeOuEmailEUsuario(String busca, Usuario usuario, Pageable pageable) {
            return repository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseAndUsuario(busca, busca, usuario, pageable);
        }

    }

