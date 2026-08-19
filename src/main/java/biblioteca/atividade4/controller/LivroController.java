package biblioteca.atividade4.controller;

import biblioteca.atividade4.dto.livro.LivroRequestDTO;
import biblioteca.atividade4.dto.livro.LivroResponseDTO;
import biblioteca.atividade4.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@RequestBody @Valid LivroRequestDTO dto) {
        LivroResponseDTO livro = livroService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscar(@PathVariable Long id) {
        LivroResponseDTO livro = livroService.findById(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar() {
        List<LivroResponseDTO> livro = livroService.findAll();
        return ResponseEntity.ok(livro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid LivroRequestDTO dto) {
        LivroResponseDTO livro = livroService.update(id, dto);
        return ResponseEntity.ok(livro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<LivroResponseDTO>> livrosDisponiveis() {
        List<LivroResponseDTO> livro = livroService.livrosDisponiveis();
        return ResponseEntity.ok(livro);
    }

    @GetMapping("/sem-exemplares")
    public ResponseEntity<List<LivroResponseDTO>> livrosSemExemplaresDisponiveis() {
        List<LivroResponseDTO> livro = livroService.livrosSemExemplaresDisponiveis();
        return ResponseEntity.ok(livro);
    }

}
