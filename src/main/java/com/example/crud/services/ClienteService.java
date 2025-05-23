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
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.PersistenceContext;
    import jakarta.persistence.criteria.*;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageImpl;
    import org.springframework.data.domain.Pageable;


    import java.util.ArrayList;
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

            if (existente.isPresent() && !existente.get().getId().equals(cliente.getId())) {
                throw new IllegalArgumentException("Já existe um cliente com este e-mail.");
            }

            // Se não for selecionada nenhuma imobiliária, define como null
            if (cliente.getImobiliaria() != null && cliente.getImobiliaria().getId() == null) {
                cliente.setImobiliaria(null);
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

        public long contarTodos(Usuario usuario) {
            return repository.countByUsuario(usuario);
        }

        public long contarSemImobiliaria(Usuario usuario) {
            return repository.countByUsuarioAndImobiliariaIsNull(usuario);
        }

        public String buscarImobiliariaComMaisClientes(Usuario usuario) {
            return repository.buscarNomeImobiliariaComMaisClientes(usuario).stream().findFirst().orElse(null);
        }

        @PersistenceContext
        private EntityManager entityManager;

        public Page<Cliente> buscarComFiltros(String busca, String telefone, String endereco, UUID imobiliariaId, Usuario usuario, Pageable pageable) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // ------------------ Query principal ------------------
            CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
            Root<Cliente> cliente = cq.from(Cliente.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(cliente.get("usuario"), usuario));

            if (busca != null && !busca.isBlank()) {
                Predicate nomePredicate = cb.like(cb.lower(cliente.get("nome")), "%" + busca.toLowerCase() + "%");
                Predicate emailPredicate = cb.like(cb.lower(cliente.get("email")), "%" + busca.toLowerCase() + "%");
                predicates.add(cb.or(nomePredicate, emailPredicate));
            }

            if (telefone != null && !telefone.isBlank()) {
                predicates.add(cb.like(cb.lower(cliente.get("telefone")), "%" + telefone.toLowerCase() + "%"));
            }

            if (endereco != null && !endereco.isBlank()) {
                predicates.add(cb.like(cb.lower(cliente.get("endereco")), "%" + endereco.toLowerCase() + "%"));
            }

            if (imobiliariaId != null) {
                predicates.add(cb.equal(cliente.get("imobiliaria").get("id"), imobiliariaId));
            }

            cq.where(predicates.toArray(new Predicate[0]));

            // Paginação e execução da query
            List<Cliente> resultados = entityManager.createQuery(cq)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            // ------------------ Query de contagem ------------------
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Cliente> countRoot = countQuery.from(Cliente.class);

            List<Predicate> countPredicates = new ArrayList<>();
            countPredicates.add(cb.equal(countRoot.get("usuario"), usuario));

            if (busca != null && !busca.isBlank()) {
                Predicate nomePredicate = cb.like(cb.lower(countRoot.get("nome")), "%" + busca.toLowerCase() + "%");
                Predicate emailPredicate = cb.like(cb.lower(countRoot.get("email")), "%" + busca.toLowerCase() + "%");
                countPredicates.add(cb.or(nomePredicate, emailPredicate));
            }

            if (telefone != null && !telefone.isBlank()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("telefone")), "%" + telefone.toLowerCase() + "%"));
            }

            if (endereco != null && !endereco.isBlank()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("endereco")), "%" + endereco.toLowerCase() + "%"));
            }

            if (imobiliariaId != null) {
                countPredicates.add(cb.equal(countRoot.get("imobiliaria").get("id"), imobiliariaId));
            }

            countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
            Long total = entityManager.createQuery(countQuery).getSingleResult();

            return new PageImpl<>(resultados, pageable, total);
        }


    }

