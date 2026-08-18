package biblioteca.atividade4.repository;

import biblioteca.atividade4.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCpfAndIdNot(String cpf, Long id);
}
