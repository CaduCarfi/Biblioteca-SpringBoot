package biblioteca.atividade4.repository;

import biblioteca.atividade4.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, Long id);
    List<Livro> findByQuantidadeDisponivelGreaterThan(int quantidade);
    List<Livro> findByQuantidadeDisponivel(int quantidade);
}