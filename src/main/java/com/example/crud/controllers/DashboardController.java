package com.example.crud.controllers;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositorys.ImovelRepository;
import com.example.crud.repositorys.NegociacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private NegociacaoRepository negociacaoRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Usuario usuarioLogado, Model model) {
        // Imóveis por tipo (filtrando por usuário)
        List<Object[]> imoveisPorTipo = imovelRepository.countImoveisByTipoPorUsuario(usuarioLogado);
        model.addAttribute("imoveisPorTipo", imoveisPorTipo != null ? imoveisPorTipo : Collections.emptyList());

        // Negociações por status (filtrando por usuário)
        List<Object[]> negociacoesPorStatus = negociacaoRepository.countNegociacoesByStatusPorUsuario(usuarioLogado);
        model.addAttribute("negociacoesPorStatus", negociacoesPorStatus != null ? negociacoesPorStatus : Collections.emptyList());

        // Última negociação (filtrando por usuário)
        Negociacao ultimaNegociacao = negociacaoRepository.findTopByUsuarioOrderByDataNegociacaoDesc(usuarioLogado);
        model.addAttribute("ultimaNegociacao", ultimaNegociacao);

        // Totalizadores por usuário
        model.addAttribute("totalImoveis", imovelRepository.countByUsuario(usuarioLogado));
        model.addAttribute("totalNegociacoes", negociacaoRepository.countByUsuario(usuarioLogado));

        return "dashboard";
    }
}
