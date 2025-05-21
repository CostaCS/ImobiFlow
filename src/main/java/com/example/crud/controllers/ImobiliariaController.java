package com.example.crud.controllers;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.services.ImobiliariaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositorys.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Controller
@RequestMapping("/imobiliarias")
public class ImobiliariaController {

    @Autowired
    private ImobiliariaService imobiliariaService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    /* ROTAS API REST (JSON)

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Imobiliaria>> getAllImobiliariasAPI() {
        return ResponseEntity.ok(imobiliariaService.getAllImobiliarias());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Void> registerImobiliariaAPI(@RequestBody @Valid RequestImobiliaria data) {
        imobiliariaService.registerImobiliaria(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api")
    @ResponseBody
    @Transactional
    public ResponseEntity<Imobiliaria> updateImobiliariaAPI(@RequestBody @Valid RequestImobiliaria data) {
        return ResponseEntity.ok(imobiliariaService.updateImobiliaria(data));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Void> deleteImobiliariaAPI(@PathVariable UUID id) {
        imobiliariaService.deleteImobiliaria(id);
        return ResponseEntity.noContent().build();
    }

    */

    @GetMapping
    public String listarImobiliarias(
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(page, 6);

        // Obtém o usuário logado
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Page<Imobiliaria> imobiliariasPage;

        if (busca != null && !busca.isEmpty()) {
            imobiliariasPage = imobiliariaService.buscarPorNomeOuCnpjEUsuario(busca, usuario, pageable);
        } else {
            imobiliariasPage = imobiliariaService.getAllByUsuario(usuario, pageable);
        }

        model.addAttribute("imobiliarias", imobiliariasPage);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", imobiliariasPage.getTotalPages());
        model.addAttribute("busca", busca);
        model.addAttribute("imobiliaria", new Imobiliaria());
        model.addAttribute("modoEdicao", false);

        // Contadores baseados no usuário
        model.addAttribute("totalImobiliarias", imobiliariaService.contarTotalImobiliariasPorUsuario(usuario));
        model.addAttribute("imobiliariasComNegociacoes", imobiliariaService.contarImobiliariasComNegociacoesPorUsuario(usuario));
        model.addAttribute("imobiliariasSemNegociacoes", imobiliariaService.contarImobiliariasSemNegociacoesPorUsuario(usuario));

        return "imobiliarias";
    }



    @PostMapping("/salvar")
    public String salvarImobiliaria(@ModelAttribute("imobiliaria") Imobiliaria i,
                                    @AuthenticationPrincipal Usuario usuarioLogado,
                                    RedirectAttributes redirectAttributes) {
        try {
            imobiliariaService.salvarOuAtualizar(i, usuarioLogado);
            redirectAttributes.addFlashAttribute("mensagem", "Imobiliária cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            redirectAttributes.addFlashAttribute("abrirModal", true);
            redirectAttributes.addFlashAttribute("imobiliaria", i);
        }
        return "redirect:/imobiliarias";
    }


    @GetMapping("/editar/{id}")
    public String editarImobiliaria(@PathVariable UUID id, Model model) {
        Pageable pageable = PageRequest.of(0, 6); // página 0, 6 por página
        Page<Imobiliaria> imobiliarias = imobiliariaService.getAllImobiliarias(pageable);
        Imobiliaria im = imobiliariaService.getById(id);

        model.addAttribute("imobiliaria", im);
        model.addAttribute("modoEdicao", true);
        model.addAttribute("abrirModal", true);
        model.addAttribute("imobiliarias", imobiliarias);
        return "imobiliarias";
    }

    @GetMapping("/excluir/{id}")
    public String excluirImobiliaria(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        imobiliariaService.deleteImobiliaria(id);
        redirectAttributes.addFlashAttribute("mensagem", "Imobiliária excluída com sucesso!");
        return "redirect:/imobiliarias";
    }

    @GetMapping("/novo")
    public String novaImobiliaria(Model model) {
        Pageable pageable = PageRequest.of(0, 6); // página 0, 6 por página
        Page<Imobiliaria> imobiliarias = imobiliariaService.getAllImobiliarias(pageable);

        model.addAttribute("imobiliaria", new Imobiliaria());
        model.addAttribute("imobiliarias", imobiliarias);
        model.addAttribute("modoEdicao", false);
        model.addAttribute("abrirModal", true);
        return "imobiliarias";
    }
}
