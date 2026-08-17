package biblioteca.atividade4.dto.livro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private Integer quantidade;
    private Integer quantidadeDisponivel;
}
