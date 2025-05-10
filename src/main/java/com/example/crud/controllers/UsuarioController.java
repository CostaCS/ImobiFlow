package com.example.crud.controllers;

import com.example.crud.requests.RequestUsuario;
import com.example.crud.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.crud.domain.entitys.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDate;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

        @Autowired
        private UsuarioService usuarioService;

        @GetMapping("/pagina")
        public String listarUsuariosPagina(HttpSession session, Model model) {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

            if (usuario == null || !usuario.isAdmin()) {
                return "redirect:/login";
            }

            model.addAttribute("usuarios", usuarioService.getAllUsuarios());
            return "usuarios/lista"; // HTML com tabela
        }

        @PostMapping("/editar")
        public String editarUsuario(@RequestParam UUID id,
                                    @RequestParam String nome,
                                    @RequestParam String email,
                                    @RequestParam String telefone,
                                    @RequestParam(required = false) boolean admin,
                                    HttpSession session) {

            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            if (usuarioLogado == null || !usuarioLogado.isAdmin()) return "redirect:/login";

            Usuario usuario = usuarioService.findById(id);
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTelefone(telefone);
            usuario.setAdmin(admin);

            usuarioService.salvar(usuario); // pode ser save() ou update()

            return "redirect:/usuario/pagina";
        }

        @GetMapping("/excluir/{id}")
        public String excluirUsuario(@PathVariable UUID id, HttpSession session) {
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            if (usuarioLogado == null || !usuarioLogado.isAdmin()) return "redirect:/login";

            usuarioService.deleteUsuario(id);
            return "redirect:/usuario/pagina";
        }

        @PostMapping("/novo")
        public String cadastrarUsuario(@RequestParam String nome,
                                       @RequestParam String email,
                                       @RequestParam String telefone,
                                       @RequestParam String senha,
                                       @RequestParam(required = false) boolean admin,
                                       HttpSession session) {

            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            if (usuarioLogado == null || !usuarioLogado.isAdmin()) return "redirect:/login";

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
        public ResponseEntity registerUsuario(@RequestBody @Valid RequestUsuario data){
            usuarioService.registerUsuario(data);
            return ResponseEntity.ok().build();
        }

        @PutMapping
        @ResponseBody
        @Transactional
        public ResponseEntity updateUsuario(@RequestBody @Valid RequestUsuario data){
            var usuarioAtualizado = usuarioService.updateUsuario(data);
            return ResponseEntity.ok(usuarioAtualizado);
        }

        @DeleteMapping("/{id}")
        @ResponseBody
        @Transactional
        public ResponseEntity deleteProduct(@PathVariable UUID id){
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        }


}
