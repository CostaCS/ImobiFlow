package com.example.crud.controllers;

import com.example.crud.requests.RequestCliente;
import com.example.crud.services.ClienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import com.example.crud.domain.entitys.Cliente;
import org.springframework.ui.Model;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //ROTAS JSON (API)

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

    //ROTAS HTML

    @GetMapping
    public String listarClientes(
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 6);
        Page<Cliente> clientesPage;

        if (busca != null && !busca.isEmpty()) {
            clientesPage = clienteService.buscarPorNomeOuEmail(busca, pageable);
        } else {
            clientesPage = clienteService.listarTodos(pageable);
        }

        // 🚨 Se página solicitada for maior que o total, redireciona para a última página válida
        if (page >= clientesPage.getTotalPages() && clientesPage.getTotalPages() > 0) {
            return "redirect:/clientes?page=" + Math.max(0, clientesPage.getTotalPages() - 1) +
                    (busca != null && !busca.isEmpty() ? "&busca=" + busca : "");
        }

        model.addAttribute("clientes", clientesPage);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", clientesPage.getTotalPages());
        model.addAttribute("busca", busca);
        model.addAttribute("cliente", new Cliente());

        return "clientes";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteService.salvar(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }
        return "redirect:/clientes";
    }


    @GetMapping("/deletar/{id}")
    public String deletarCliente(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        clienteService.deletar(id);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
        return "redirect:/clientes";
    }
}
