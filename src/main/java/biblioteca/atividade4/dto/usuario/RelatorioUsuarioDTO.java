package biblioteca.atividade4.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioUsuarioDTO {

    private String nome;
    private int quantidadeEmprestimos;
    private int quantidadeEmprestimosAbertos;
    private int quantidadeLivrosDevolvidos;

}
