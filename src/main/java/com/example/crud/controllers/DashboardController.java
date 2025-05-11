package com.example.crud.controllers;

import com.example.crud.repositorys.ImovelRepository;
import com.example.crud.repositorys.NegociacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.crud.domain.entitys.Negociacao;


import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private NegociacaoRepository negociacaoRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Agrupamento de imóveis por tipo
        List<Object[]> imoveisPorTipo = imovelRepository.countImoveisByTipo();
        model.addAttribute("imoveisPorTipo", imoveisPorTipo);

        // Agrupamento de negociações por status
        List<Object[]> negociacoesPorStatus = negociacaoRepository.countNegociacoesByStatus();
        model.addAttribute("negociacoesPorStatus", negociacoesPorStatus);

        Negociacao ultimaNegociacao = negociacaoRepository.findTopByOrderByDataNegociacaoDesc();
        model.addAttribute("ultimaNegociacao", ultimaNegociacao);

        return "dashboard";
    }


}
