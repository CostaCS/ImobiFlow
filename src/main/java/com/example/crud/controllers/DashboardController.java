package com.example.crud.controllers;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.repositorys.ImovelRepository;
import com.example.crud.repositorys.NegociacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String dashboard(Model model) {
        // Imóveis por tipo
        List<Object[]> imoveisPorTipo = imovelRepository.countImoveisByTipo();
        model.addAttribute("imoveisPorTipo", imoveisPorTipo != null ? imoveisPorTipo : Collections.emptyList());

        // Negociações por status
        List<Object[]> negociacoesPorStatus = negociacaoRepository.countNegociacoesByStatus();
        model.addAttribute("negociacoesPorStatus", negociacoesPorStatus != null ? negociacoesPorStatus : Collections.emptyList());

        // Última negociação
        Negociacao ultimaNegociacao = negociacaoRepository.findTopByOrderByDataNegociacaoDesc();
        model.addAttribute("ultimaNegociacao", ultimaNegociacao);

        // Totalizadores (caso vá usar no HTML)
        model.addAttribute("totalImoveis", imovelRepository.count());
        model.addAttribute("totalNegociacoes", negociacaoRepository.count());

        return "dashboard";
    }
}
