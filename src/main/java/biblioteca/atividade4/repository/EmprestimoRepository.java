package biblioteca.atividade4.repository;

import biblioteca.atividade4.Enum.StatusEmprestimo;
import biblioteca.atividade4.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    boolean existsByStatusAndUsuarioId(StatusEmprestimo status, Long id);
}
