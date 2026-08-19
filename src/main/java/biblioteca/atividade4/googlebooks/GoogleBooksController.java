package biblioteca.atividade4.googlebooks;

import biblioteca.atividade4.dto.livro.LivroResponseDTO;
import biblioteca.atividade4.googlebooks.dto.VolumeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros/google-books")
public class GoogleBooksController {

    private final GoogleBooksService googleBooksService;

    public GoogleBooksController(GoogleBooksService googleBooksService) {
        this.googleBooksService = googleBooksService;
    }

    @GetMapping
    public ResponseEntity<List<VolumeDTO>> pesquisar(@RequestParam String nome) {
        List<VolumeDTO> volume = googleBooksService.pesquisar(nome);
        return ResponseEntity.ok(volume);
    }

    @PostMapping("/{volumeId}/importar")
    public ResponseEntity<LivroResponseDTO> importar(@PathVariable String volumeId) {
        LivroResponseDTO livro = googleBooksService.importar(volumeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }
}
