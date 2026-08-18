package biblioteca.atividade4.repository;

import biblioteca.atividade4.Enum.StatusEmprestimo;
import biblioteca.atividade4.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    boolean existsByStatusAndUsuarioId(StatusEmprestimo status, Long id);
    boolean existsByStatusAndLivroId(StatusEmprestimo status, Long id);
    List<Emprestimo> findByUsuarioId(Long id);
    List<Emprestimo> findByStatus(StatusEmprestimo status);
}
