package biblioteca.atividade4.dto.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroRequestDTO {

    @NotBlank(message = "Titulo é obrigatório")
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    private String autor;

    @NotBlank(message = "Isbn é obrigatório")
    private String isbn;

    @NotNull(message = "Ano Publicação é obrigatório")
    private Integer anoPublicacao;

    @NotNull(message = "Quantidade é obrigatório")
    private Integer quantidade;
}
