package com.example.crud.controllers;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.ClienteRepository;
import com.example.crud.repositories.ImovelRepository;
import com.example.crud.repositories.NegociacaoRepository;
import com.example.crud.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    @Autowired
    private NegociacaoRepository negociacaoRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Usuario usuarioLogado, Model model) {
        // Imóveis por tipo (filtrando por usuário)
        List<Object[]> imoveisPorTipo = imovelRepository.countImoveisByTipoPorUsuario(usuarioLogado);
        model.addAttribute("imoveisPorTipo", imoveisPorTipo != null ? imoveisPorTipo : Collections.emptyList());

        // 2) Negociações por status (filtrando por usuário)
        List<Object[]> listaStatus = negociacaoRepository.contarPorStatusTodos(usuarioLogado);
        Map<String, Long> negociacoesPorStatusMap = listaStatus.stream()
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),              // chave = status (String)
                        arr -> ((Number) arr[1]).longValue()   // valor = contagem (Long)
                ));
        model.addAttribute("negociacoesPorStatus", negociacoesPorStatusMap);


        long totalClientes = clienteService.contarTodos(usuarioLogado); // deve chamar repository.count()
        model.addAttribute("totalClientes", totalClientes);


        // Última negociação (filtrando por usuário)
        Negociacao ultimaNegociacao = negociacaoRepository.findTopByUsuarioOrderByDataNegociacaoDesc(usuarioLogado);
        model.addAttribute("ultimaNegociacao", ultimaNegociacao);

        // Totalizadores por usuário
        model.addAttribute("totalImoveis", imovelRepository.countByUsuario(usuarioLogado));
        model.addAttribute("totalNegociacoes", negociacaoRepository.countByUsuario(usuarioLogado));

        // 1) Top 5 Clientes
        List<Object[]> topList = negociacaoRepository
                .findTopClientes(usuarioLogado.getId(), PageRequest.of(0, 5));

        List<Map<String, Object>> topClientes = topList.stream()
                .map(arr -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("nome",  arr[0].toString());
                    m.put("total", ((Number) arr[1]).longValue());
                    return m;
                })
                .collect(Collectors.toList());

        model.addAttribute("topClientes", topClientes);

        // Busca a lista de nomes ordenada por maior número de clientes
        List<String> nomes = clienteRepository
                .buscarNomeImobiliariaComMaisClientes(usuarioLogado);

        // Pega o primeiro (o “top 1”), ou null se não houver
                String imobiliariaMaisClientes = (nomes != null && !nomes.isEmpty())
                        ? nomes.get(0)
                        : null;

        // Adiciona ao model para o Thymeleaf
                model.addAttribute("imobiliariaMaisClientes", imobiliariaMaisClientes);


        return "dashboard";
    }
}
