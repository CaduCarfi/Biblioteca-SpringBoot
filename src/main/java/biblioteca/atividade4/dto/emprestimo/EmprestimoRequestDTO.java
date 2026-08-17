package biblioteca.atividade4.dto.emprestimo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoRequestDTO {

    @NotNull(message = "Id de Usuário é obrigatório")
    @Positive(message = "Id de Usuário deve ser válido")
    private Long usuarioId;

    @NotNull(message = "Id de Livro é obrigatório")
    @Positive(message = "Id de Livro deve ser válido")
    private Long livroId;
}
