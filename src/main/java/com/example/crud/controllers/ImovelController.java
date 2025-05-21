package com.example.crud.controllers;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.requests.RequestImovel;
import com.example.crud.domain.entitys.Imovel;
import com.example.crud.services.ImovelService;
import com.example.crud.services.ImobiliariaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.crud.domain.entitys.Usuario;


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
    public String listarImoveis(@AuthenticationPrincipal Usuario usuarioLogado,
                                @RequestParam(defaultValue = "") String busca,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {

        System.out.println("Usuário logado: " + usuarioLogado);

        Pageable pageable = PageRequest.of(page, 10); // 10 é o tamanho da página
        model.addAttribute("imoveis", imovelService.buscarPorUsuario(busca, usuarioLogado, pageable));

        Imovel novo = new Imovel();
        novo.setImobiliaria(new Imobiliaria()); // evita NullPointerException no th:field
        model.addAttribute("imovel", novo);

        model.addAttribute("imobiliarias", imobiliariaService.findByUsuario(usuarioLogado));
        model.addAttribute("busca", busca);
        model.addAttribute("paginaAtual", page);

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
