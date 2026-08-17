package biblioteca.atividade4.repository;

import biblioteca.atividade4.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
