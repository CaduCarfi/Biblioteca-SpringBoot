package biblioteca.atividade4.service;

import biblioteca.atividade4.Enum.StatusEmprestimo;
import biblioteca.atividade4.dto.livro.LivroRequestDTO;
import biblioteca.atividade4.dto.livro.LivroResponseDTO;
import biblioteca.atividade4.model.Livro;
import biblioteca.atividade4.repository.EmprestimoRepository;
import biblioteca.atividade4.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LivroService(LivroRepository livroRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public LivroResponseDTO create(LivroRequestDTO dto) {
        if (livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new RuntimeException("Não podem ser cadastrados dois livros com o mesmo isbn");
        }
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setQuantidade(dto.getQuantidade());
        livro.setQuantidadeDisponivel(dto.getQuantidade());

        Livro salvo = livroRepository.save(livro);
        return toResponseDTO(salvo);
    }

    public LivroResponseDTO findById(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não existe"));
        return toResponseDTO(livro);
    }

    public List<LivroResponseDTO> findAll() {
        return livroRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public LivroResponseDTO update(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não existe"));
        if (livroRepository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
            throw new RuntimeException("Não podem ser cadastrados dois livros com o mesmo isbn");
        }

        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setQuantidade(dto.getQuantidade());

        Livro updated = livroRepository.save(livro);
        return toResponseDTO(updated);
    }

    public void delete(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não existe"));
        if (emprestimoRepository.existsByStatusAndLivroId(StatusEmprestimo.EMPRESTADO, id)){
            throw new RuntimeException("Um livro emprestado nao pode ser excluido");
        }
        livroRepository.deleteById(id);
    }

    public List<LivroResponseDTO> livrosDisponiveis() {
        return livroRepository.findByQuantidadeDisponivelGreaterThan(0)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<LivroResponseDTO> livrosSemExemplaresDisponiveis() {
        return livroRepository.findByQuantidadeDisponivel(0)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public LivroResponseDTO toResponseDTO(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getQuantidade(),
                livro.getQuantidadeDisponivel()
        );
    }
}
