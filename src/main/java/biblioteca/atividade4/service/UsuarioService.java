package biblioteca.atividade4.service;

import biblioteca.atividade4.Enum.StatusEmprestimo;
import biblioteca.atividade4.dto.usuario.UsuarioRequestDTO;
import biblioteca.atividade4.dto.usuario.UsuarioResponseDTO;
import biblioteca.atividade4.model.Usuario;
import biblioteca.atividade4.repository.EmprestimoRepository;
import biblioteca.atividade4.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, EmprestimoRepository emprestimoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("Cpf ja Cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponseDTO(salvo);
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não existe"));
        return toResponseDTO(usuario);
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não existe"));
        if (usuarioRepository.existsByCpfAndIdNot(dto.getCpf(), id)) {
            throw new RuntimeException("Cpf ja Cadastrado");
        }
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());

        Usuario updated = usuarioRepository.save(usuario);
        return toResponseDTO(updated);
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getDataCadastro(),
                usuario.getAtivo()
        );
    }

    public UsuarioResponseDTO inativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não existe"));

        if (emprestimoRepository.existsByStatusAndUsuarioId(StatusEmprestimo.EMPRESTADO, id)) {
            throw new RuntimeException("Usuário possui emprestimos");
        } else {
            usuario.setAtivo(false);
        }
        Usuario inativado = usuarioRepository.save(usuario);
        return toResponseDTO(inativado);
    }
}
