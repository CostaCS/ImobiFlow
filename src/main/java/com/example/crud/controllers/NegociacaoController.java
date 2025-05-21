package com.example.crud.controllers;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.requests.RequestNegociacao;
import com.example.crud.services.ClienteService;
import com.example.crud.services.ImobiliariaService;
import com.example.crud.services.ImovelService;
import com.example.crud.services.NegociacaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.example.crud.domain.entitys.Negociacao;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("/negociacao")
public class NegociacaoController {

    @Autowired
    private NegociacaoService negociacaoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ImovelService imovelService;

    @Autowired
    private ImobiliariaService imobiliariaService;

    //ROTAS JSON (API)

    @GetMapping
    @ResponseBody
    public ResponseEntity getAllNegociacoes() {
        var allNegociacoes = negociacaoService.getAllNegociacoes();
        return ResponseEntity.ok(allNegociacoes);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity registerNegociacao(@RequestBody @Valid RequestNegociacao data){
        negociacaoService.registerNegociacao(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @ResponseBody
    public ResponseEntity updateNegociacao(@PathVariable UUID id, @RequestBody @Valid RequestNegociacao data) {
        var negociacaoAtualizada = negociacaoService.updateNegociacao(id, data);
        return ResponseEntity.ok(negociacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseBody
    public ResponseEntity deleteNegociacao(@PathVariable UUID id){
        negociacaoService.deleteNegociacao(id);
        return ResponseEntity.noContent().build();
    }

    //ROTAS HTML

    @GetMapping("/pagina")
    public String listarNegociacoes(@RequestParam(defaultValue = "") String busca,
                                    @RequestParam(defaultValue = "0") int page,
                                    @AuthenticationPrincipal Usuario usuarioLogado,
                                    Model model) {

        Pageable pageable = PageRequest.of(page, 6); // define paginação

        Page<Negociacao> negociacoes;

        if (busca != null && !busca.isEmpty()) {
            negociacoes = negociacaoService.buscarPorUsuarioComBusca(busca, usuarioLogado, pageable);
        } else {
            negociacoes = negociacaoService.buscarPorUsuario(usuarioLogado, pageable);
        }

        model.addAttribute("negociacoes", negociacoes);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", negociacoes.getTotalPages());
        model.addAttribute("busca", busca);
        model.addAttribute("negociacao", new Negociacao());
        model.addAttribute("clientes", clienteService.listarTodosPorUsuario(usuarioLogado, pageable));
        model.addAttribute("imoveis", imovelService.findByUsuario(usuarioLogado));
        model.addAttribute("imobiliarias", imobiliariaService.findByUsuario(usuarioLogado));

        return "negociacoes";
    }


    @PostMapping("/salvar")
    public String salvarNegociacao(@ModelAttribute Negociacao negociacao,
                                   @AuthenticationPrincipal Usuario usuarioLogado,
                                   RedirectAttributes redirectAttributes)
    {
        negociacaoService.salvarViaHTML(negociacao, usuarioLogado);
        redirectAttributes.addFlashAttribute("mensagem", "Negociação salva com sucesso!");
        return "redirect:/negociacao/pagina";
    }

    @GetMapping("/deletar/{id}")
    public String deletarNegociacao(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        negociacaoService.deleteNegociacao(id);
        redirectAttributes.addFlashAttribute("mensagem", "Negociação excluída com sucesso!");
        return "redirect:/negociacao/pagina";
    }

}
