package biblioteca.atividade4.controller;

import biblioteca.atividade4.dto.usuario.RelatorioUsuarioDTO;
import biblioteca.atividade4.dto.usuario.UsuarioRequestDTO;
import biblioteca.atividade4.dto.usuario.UsuarioResponseDTO;
import biblioteca.atividade4.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> usuario = usuarioService.findAll();
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.update(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioUsuarioDTO>> gerarRelatorio() {
        List<RelatorioUsuarioDTO> relatorio = usuarioService.gerarRelatorio();
        return ResponseEntity.ok(relatorio);
    }

}
