package com.example.crud.controllers;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.domain.entitys.Imovel;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.requests.RequestImovel;
import com.example.crud.services.ImobiliariaService;
import com.example.crud.services.ImovelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/imovel")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @Autowired
    private ImobiliariaService imobiliariaService;

    //ROTAS JSON(API)

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Imovel>> getAllImoveis() {
        List<Imovel> imoveis = imovelService.getAllImoveis();
        return ResponseEntity.ok(imoveis);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> registerImovel(@RequestBody @Valid RequestImovel request) {
        imovelService.registerImovel(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Imovel> updateImovel(@PathVariable UUID id, @RequestBody @Valid RequestImovel request) {
        Imovel imovelAtualizado = imovelService.updateImovel(id, request);
        return ResponseEntity.ok(imovelAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteImovel(@PathVariable UUID id) {
        imovelService.deleteImovel(id);
        return ResponseEntity.noContent().build();
    }

    //ROTAS HTML

    @GetMapping("/pagina")
    public String listarImoveisComFiltro(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Integer quartos,
            @RequestParam(required = false) Integer banheiros,
            @RequestParam(required = false) Integer vagas,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID imobiliariaId,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) BigDecimal precoCondominioMin,
            @RequestParam(required = false) BigDecimal precoCondominioMax,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);

        // chama o service incluindo os novos filtros
        model.addAttribute("imoveis", imovelService.filtrarImoveis(
                titulo, tipo, precoMin, precoMax, quartos, banheiros, vagas,
                status, imobiliariaId, descricao, endereco,
                precoCondominioMin, precoCondominioMax,
                usuarioLogado, pageable
        ));

        // reenvia atributos para o Thymeleaf
        model.addAttribute("titulo", titulo);
        model.addAttribute("tipo", tipo);
        model.addAttribute("precoMin", precoMin);
        model.addAttribute("precoMax", precoMax);
        model.addAttribute("quartos", quartos);
        model.addAttribute("banheiros", banheiros);
        model.addAttribute("vagas", vagas);
        model.addAttribute("status", status);
        model.addAttribute("imobiliariaId", imobiliariaId);
        model.addAttribute("descricao", descricao);
        model.addAttribute("endereco", endereco);
        model.addAttribute("precoCondominioMin", precoCondominioMin);
        model.addAttribute("precoCondominioMax", precoCondominioMax);
        model.addAttribute("paginaAtual", page);

        model.addAttribute("totalDisponiveis", imovelService.contarPorStatus("Disponível", usuarioLogado));
        model.addAttribute("totalNegociacao", imovelService.contarPorStatus("Reservado", usuarioLogado));
        model.addAttribute("totalVendidos", imovelService.contarPorStatus("Vendido", usuarioLogado));

        model.addAttribute("imobiliarias", imobiliariaService.findByUsuario(usuarioLogado));
        Imovel novo = new Imovel();
        novo.setImobiliaria(new Imobiliaria());
        model.addAttribute("imovel", novo);

        return "imoveis";
    }


    @PostMapping("/salvar")
    public String salvarImovel(@ModelAttribute Imovel imovel,
                               @AuthenticationPrincipal Usuario usuarioLogado,
                               RedirectAttributes redirectAttributes) {
        imovelService.salvarViaHTML(imovel, usuarioLogado);
        redirectAttributes.addFlashAttribute("mensagem", "Imóvel salvo com sucesso!");
        return "redirect:/imovel/pagina";
    }


    @GetMapping("/deletar/{id}")
    public String deletarImovel(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        imovelService.deleteImovel(id);
        redirectAttributes.addFlashAttribute("mensagem", "Imóvel excluído com sucesso!");
        return "redirect:/imovel/pagina";
    }

}
