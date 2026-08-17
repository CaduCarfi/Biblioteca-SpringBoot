package biblioteca.atividade4.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

}
