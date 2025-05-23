package com.example.crud.controllers;

import com.example.crud.requests.RequestCliente;
import com.example.crud.services.ClienteService;
import com.example.crud.services.ImobiliariaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import com.example.crud.domain.entitys.Cliente;
import org.springframework.ui.Model;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositorys.UsuarioRepository;




@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImobiliariaService imobiliariaService;



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
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) UUID imobiliariaId,

            Model model,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(page, 6);

        // Obtém o usuário logado
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


        Page<Cliente> clientesPage;

        clientesPage = clienteService.buscarComFiltros(busca, telefone, endereco, imobiliariaId, usuario, pageable);


        // Redireciona para a última página válida se a atual for inválida
        if (page >= clientesPage.getTotalPages() && clientesPage.getTotalPages() > 0) {
            return "redirect:/clientes?page=" + Math.max(0, clientesPage.getTotalPages() - 1) +
                    (busca != null && !busca.isEmpty() ? "&busca=" + busca : "");
        }

        model.addAttribute("clientes", clientesPage);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", clientesPage.getTotalPages());
        model.addAttribute("busca", busca);
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("imobiliarias", imobiliariaService.findByUsuario(usuario));
        model.addAttribute("totalClientes", clienteService.contarTodos(usuario));
        model.addAttribute("clientesSemImobiliaria", clienteService.contarSemImobiliaria(usuario));
        model.addAttribute("imobiliariaMaisClientes", clienteService.buscarImobiliariaComMaisClientes(usuario));
        model.addAttribute("telefone", telefone);
        model.addAttribute("endereco", endereco);
        model.addAttribute("imobiliariaId", imobiliariaId);



        return "clientes";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente,
                                @AuthenticationPrincipal Usuario usuarioLogado,
                                RedirectAttributes redirectAttributes) {
        try {
            clienteService.salvar(cliente, usuarioLogado); // corrigido
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
