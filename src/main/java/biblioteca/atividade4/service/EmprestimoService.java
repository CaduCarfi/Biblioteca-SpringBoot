package biblioteca.atividade4.service;

import biblioteca.atividade4.Enum.StatusEmprestimo;
import biblioteca.atividade4.dto.emprestimo.EmprestimoRequestDTO;
import biblioteca.atividade4.dto.emprestimo.EmprestimoResponseDTO;
import biblioteca.atividade4.model.Emprestimo;
import biblioteca.atividade4.model.Livro;
import biblioteca.atividade4.model.Usuario;
import biblioteca.atividade4.repository.EmprestimoRepository;
import biblioteca.atividade4.repository.LivroRepository;
import biblioteca.atividade4.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final UsuarioService usuarioService;
    private final LivroService livroService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository, UsuarioService usuarioService, LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.usuarioService = usuarioService;
        this.livroService = livroService;
    }

    @Transactional
    public EmprestimoResponseDTO realizarEmprestimo(EmprestimoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não existe"));
        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuário inativo não pode realizar empréstimos");
        }
        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new RuntimeException("Livro não existe"));
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro não possui exemplares disponíveis");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setStatus(StatusEmprestimo.EMPRESTADO);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return toResponseDTO(salvo);
    }

    public EmprestimoResponseDTO findById(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não existe"));
        return toResponseDTO(emprestimo);
    }

    public List<EmprestimoResponseDTO> findAll() {
        return emprestimoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<EmprestimoResponseDTO> findByUsuarioId(Long id) {
        return emprestimoRepository.findByUsuarioId(id)
                .stream()
                .map(this::toResponseDTO)
                .toList();

    }

    public List<EmprestimoResponseDTO> findByStatus() {
        return emprestimoRepository.findByStatus(StatusEmprestimo.EMPRESTADO)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public EmprestimoResponseDTO realizarDevolucao(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprestimo não existe"));
        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new RuntimeException("Emprestimo já foi devolvido");
        }

        emprestimo.setDataDevolucao(LocalDateTime.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        Livro livro = emprestimo.getLivro();

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() +1);
        livroRepository.save(livro);

        Emprestimo salvar = emprestimoRepository.save(emprestimo);
        return toResponseDTO(salvar);
    }

    public EmprestimoResponseDTO toResponseDTO(Emprestimo emprestimo) {
        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                usuarioService.toResponseDTO(emprestimo.getUsuario()),
                livroService.toResponseDTO(emprestimo.getLivro()),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatus()
        );
    }
}
