package biblioteca.atividade4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "livro")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String autor;
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column(nullable = false)
    private Integer anoPublicacao;
    @Column(nullable = false)
    private Integer quantidade;
    @Column(nullable = false)
    private Integer quantidadeDisponivel;
    @OneToMany(mappedBy = "livro")
    private List<Emprestimo> emprestimos;
}
