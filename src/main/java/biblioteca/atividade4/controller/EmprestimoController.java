package biblioteca.atividade4.controller;

import biblioteca.atividade4.dto.emprestimo.EmprestimoRequestDTO;
import biblioteca.atividade4.dto.emprestimo.EmprestimoResponseDTO;
import biblioteca.atividade4.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponseDTO> criar(@RequestBody @Valid EmprestimoRequestDTO dto) {
        EmprestimoResponseDTO emprestimo = emprestimoService.realizarEmprestimo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponseDTO> buscar(@PathVariable Long id) {
        EmprestimoResponseDTO emprestimo = emprestimoService.findById(id);
        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoResponseDTO>> listar() {
        List<EmprestimoResponseDTO> emprestimo = emprestimoService.findAll();
        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoResponseDTO>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<EmprestimoResponseDTO> emprestimo = emprestimoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping("/abertos")
    public ResponseEntity<List<EmprestimoResponseDTO>> findByStatus() {
        List<EmprestimoResponseDTO> emprestimo = emprestimoService.findByStatus();
        return ResponseEntity.ok(emprestimo);
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoResponseDTO> realizarDevolucao(@PathVariable Long id) {
        EmprestimoResponseDTO emprestimo = emprestimoService.realizarDevolucao(id);
        return ResponseEntity.ok(emprestimo);
    }
 }
