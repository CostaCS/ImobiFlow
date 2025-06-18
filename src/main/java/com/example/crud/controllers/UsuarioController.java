package com.example.crud.controllers;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.dtos.SenhaDto;
import com.example.crud.dtos.UsuarioDto;
import com.example.crud.requests.RequestUsuario;
import com.example.crud.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    //ROTAS HTML

    @GetMapping("/pagina")
    public String listarUsuariosPagina(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null || !usuario.isAdmin()) {
            return "redirect:/login";
        }

        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        model.addAttribute("usuarioLogado", usuario);

        return "usuarios/lista"; // HTML com tabela
    }

    @PostMapping("/editar")
    public String editarUsuario(@RequestParam UUID id, @RequestParam String nome, @RequestParam String email, @RequestParam String telefone, @RequestParam(required = false) String senha,
                                @RequestParam(required = false) String confirmSenha, @RequestParam(required = false) boolean admin, HttpSession session, RedirectAttributes ra) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) return "redirect:/login";

        Usuario usuario = usuarioService.findById(id);

        if (!usuario.getEmail().equals(email)) {
            ra.addFlashAttribute("erro", "E‑mail não pode ser alterado");
            return "redirect:/usuario/pagina";
        }

        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setAdmin(admin);

        if (senha != null && !senha.isBlank()) {
            if (!senha.equals(confirmSenha)) {
                ra.addFlashAttribute("erro", "Senhas não conferem");
                return "redirect:/usuario/pagina";
            }
            usuario.setSenha(encoder.encode(senha));
        }

        usuarioService.salvar(usuario); // pode ser save() ou update()

        return "redirect:/usuario/pagina";
    }

    @GetMapping("/excluir/{id}")
    public String deletarUsuario(@PathVariable UUID id,
                                 HttpSession session,
                                 RedirectAttributes ra) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return "redirect:/login";
        }

        if (usuarioLogado.getId().equals(id)) {
            ra.addFlashAttribute("erro", "Você não pode excluir seu próprio usuário.");
            return "redirect:/usuario/pagina";
        }

        usuarioService.deleteUsuarioComTudo(id);
        ra.addFlashAttribute("mensagem",
                "Usuário e todas as entidades vinculadas foram excluídos com sucesso.");
        return "redirect:/usuario/pagina";
    }


    @PostMapping("/novo")
    public String cadastrarUsuario(@RequestParam String nome, @RequestParam String email, @RequestParam String telefone, @RequestParam String senha, @RequestParam String confirmSenha, @RequestParam(required = false) boolean admin, HttpSession session, RedirectAttributes ra) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) return "redirect:/login";

        if (usuarioService.existsByEmail(email)) {
            ra.addFlashAttribute("erro", "Já existe um usuário cadastrado com este e‑mail");
            return "redirect:/usuario/pagina";
        }

        if (!senha.equals(confirmSenha)) {
            ra.addFlashAttribute("erro", "Senhas não conferem");
            return "redirect:/usuario/pagina";
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setTelefone(telefone);
        novoUsuario.setSenha(new BCryptPasswordEncoder().encode(senha)); // criptografa senha
        novoUsuario.setAdmin(admin);
        novoUsuario.setData_cadastro(LocalDate.now());

        usuarioService.salvar(novoUsuario);

        return "redirect:/usuario/pagina";
    }

    //ROTAS JSON (API)

    @GetMapping
    @ResponseBody
    public ResponseEntity getAllUsuarios(@SessionAttribute(name = "usuarioLogado", required = false) Usuario usuarioLogado) {
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        var allUsuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(allUsuarios);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity registerUsuario(@RequestBody @Valid RequestUsuario data) {
        usuarioService.registerUsuario(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @ResponseBody
    @Transactional
    public ResponseEntity updateUsuario(@RequestBody @Valid RequestUsuario data) {
        var usuarioAtualizado = usuarioService.updateUsuario(data);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity deleteProduct(@PathVariable UUID id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Editar meu perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(
            HttpSession session,
            Model model,
            CsrfToken csrfToken        // ← injeta o token CSRF
    ) {
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario entidade = usuarioService.findById(logado.getId());

        model.addAttribute("usuarioDto", UsuarioDto.fromEntity(entidade));
        model.addAttribute("passwordDto", new SenhaDto());

        // ← expõe o CsrfToken para o Thymeleaf
        model.addAttribute("_csrf", csrfToken);

        return "usuarios/perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(
            @ModelAttribute("usuarioDto") @Valid UsuarioDto dto,
            BindingResult br, HttpSession session, RedirectAttributes ra
    ) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("erro", "Verifique os campos");
            return "redirect:/usuario/perfil";
        }
        Usuario u = usuarioService.findById(dto.getId());
        dto.applyTo(u);
        usuarioService.updateProfile(u);
        ra.addFlashAttribute("mensagem", "Perfil atualizado!");
        return "redirect:/usuario/perfil";
    }

    @PostMapping("/perfil/mudar-senha")
    public String mudarSenha(
            @ModelAttribute("passwordDto") @Valid SenhaDto dto,
            BindingResult br,
            HttpSession session,
            RedirectAttributes ra
    ) {
        // 1) erros de validação no DTO?
        if (br.hasErrors()) {
            ra.addFlashAttribute("erroSenha", "Verifique os campos");
            return "redirect:/usuario/perfil";
        }

        // 2) conferência de confirmação
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            ra.addFlashAttribute("erroSenha", "Senhas não conferem");
            return "redirect:/usuario/perfil";
        }

        // 3) atualiza no banco
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        usuarioService.updatePassword(logado.getId(), dto.getNewPassword());

        // 4) atualiza sessão
        Usuario atualizado = usuarioService.findById(logado.getId());
        session.setAttribute("usuarioLogado", atualizado);

        ra.addFlashAttribute("mensagem", "Senha alterada com sucesso!");
        return "redirect:/usuario/perfil";
    }

    @PostMapping("/perfil/excluir")
    public String excluirMeuPerfil(
            HttpSession session,
            RedirectAttributes ra
    ) {
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        if (logado != null) {
            usuarioService.deleteUsuarioComTudo(logado.getId());
            session.invalidate();
            ra.addFlashAttribute("mensagem", "Sua conta foi excluída com sucesso.");
        }
        return "redirect:/login";
    }


}
