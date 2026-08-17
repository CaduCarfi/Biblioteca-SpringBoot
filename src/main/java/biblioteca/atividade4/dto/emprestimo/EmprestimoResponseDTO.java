package biblioteca.atividade4.dto.emprestimo;

import biblioteca.atividade4.Enum.StatusEmprestimo;
import biblioteca.atividade4.dto.livro.LivroResponseDTO;
import biblioteca.atividade4.dto.usuario.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoResponseDTO {

    private Long id;
    private UsuarioResponseDTO usuarioResponseDTO;
    private LivroResponseDTO livroResponseDTO;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataDevolucao;
    private StatusEmprestimo status;
}
