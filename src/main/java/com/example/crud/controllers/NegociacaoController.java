package com.example.crud.controllers;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.NegociacaoRepository;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Controller
@RequestMapping("/negociacao")
public class NegociacaoController {

    @Autowired
    private NegociacaoRepository repository;

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
    public ResponseEntity registerNegociacao(@RequestBody @Valid RequestNegociacao data) {
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
    public ResponseEntity deleteNegociacao(@PathVariable UUID id) {
        negociacaoService.deleteNegociacao(id);
        return ResponseEntity.noContent().build();
    }

    //ROTAS HTML

    @GetMapping("/pagina")
    public String listarNegociacoes(
            @RequestParam(required = false) String cliente,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String imobiliaria,
            @RequestParam(required = false) String imovel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal Usuario usuarioLogado,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);

        Page<Negociacao> negociacoes = negociacaoService.filtrarNegociacoes(
                cliente, status, imobiliaria, imovel, data, valorMin, valorMax, usuarioLogado, pageable
        );

        model.addAttribute("negociacoes", negociacoes);
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", negociacoes.getTotalPages());

        // Manter os filtros preenchidos
        model.addAttribute("cliente", cliente);
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("imobiliariaSelecionada", imobiliaria);
        model.addAttribute("imovelSelecionado", imovel);
        model.addAttribute("data", data);
        model.addAttribute("valorMin", valorMin);
        model.addAttribute("valorMax", valorMax);

        // Dados para formulário
        model.addAttribute("negociacao", new Negociacao());
        model.addAttribute("clientes", clienteService.listarTodosPorUsuario(usuarioLogado, Pageable.unpaged()).getContent());
        model.addAttribute("imoveis", imovelService.findByUsuario(usuarioLogado));
        model.addAttribute("imobiliarias", imobiliariaService.findByUsuario(usuarioLogado));

        // Indicadores
        model.addAttribute("totalNegociacoes", negociacaoService.contarTodas(usuarioLogado));
        model.addAttribute("imobiliariaTop", negociacaoService.imobiliariaComMaisNegociacoes(usuarioLogado));
        model.addAttribute("propostasEnviadas", negociacaoService.contarPorStatus("Proposta enviada", usuarioLogado));
        model.addAttribute("pendentes", negociacaoService.contarPorStatus("Em andamento", usuarioLogado));

        return "negociacoes";
    }


    @PostMapping("/salvar")
    public String salvarNegociacao(@ModelAttribute Negociacao negociacao,
                                   @AuthenticationPrincipal Usuario usuarioLogado,
                                   RedirectAttributes redirectAttributes) {
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

    @GetMapping("/detalhes/{id}")
    public String detalhesNegociacao(@PathVariable UUID id,
                                     @AuthenticationPrincipal Usuario usuarioLogado,
                                     Model model) {
        Negociacao negociacao = negociacaoService.buscarPorId(id);
        model.addAttribute("negociacao", negociacao);

        LocalDate inicio = negociacao.getDataNegociacao();
        LocalDate hoje = LocalDate.now();

        // Se inicio estiver no futuro, trata como hoje
        long diasParado = inicio.isAfter(hoje)
                ? 0
                : ChronoUnit.DAYS.between(inicio, hoje);

        model.addAttribute("diasParado", diasParado);

        String corAlerta;
        if (diasParado < 5) {
            corAlerta = "text-success";
        } else if (diasParado < 10) {
            corAlerta = "text-warning";
        } else {
            corAlerta = "text-danger";
        }
        model.addAttribute("corAlerta", corAlerta);

        return "detalhes-negociacao";
    }


}