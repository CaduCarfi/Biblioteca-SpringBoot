package biblioteca.atividade4.googlebooks;

import biblioteca.atividade4.dto.livro.LivroResponseDTO;
import biblioteca.atividade4.googlebooks.dto.IndustryIdentifierDTO;
import biblioteca.atividade4.googlebooks.dto.VolumeDTO;
import biblioteca.atividade4.googlebooks.dto.VolumeInfoDTO;
import biblioteca.atividade4.model.Livro;
import biblioteca.atividade4.repository.LivroRepository;
import biblioteca.atividade4.service.LivroService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.util.List;

@Service
public class GoogleBooksService {

    private final GoogleBooksClient googleBooksClient;
    private final LivroRepository livroRepository;
    private final LivroService livroService;

    public GoogleBooksService(GoogleBooksClient googleBooksClient, LivroRepository livroRepository, LivroService livroService) {
        this.googleBooksClient = googleBooksClient;
        this.livroRepository = livroRepository;
        this.livroService = livroService;
    }

    public List<VolumeDTO> pesquisar(String nome) {
        try {
            return googleBooksClient.buscarPorTermo(nome).getItems();
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao comunicar com a Google Books API");
        }
    }

    public LivroResponseDTO importar(String volumeId) {
        VolumeDTO volume;
        try {
            volume = googleBooksClient.buscarPorId(volumeId);
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao comunicar com a Google Books API");
        }

        VolumeInfoDTO info = volume.getVolumeInfo();
        String isbn = extrairIsbn(info);

        if (isbn != null && livroRepository.existsByIsbn(isbn)) {
            throw new RuntimeException("Livro já cadastrado com esse ISBN");
        }

        Livro livro = new Livro();
        livro.setTitulo(info.getTitle());
        livro.setAutor(extrairAutor(info));
        livro.setIsbn(isbn);
        livro.setAnoPublicacao(extrairAno(info));
        livro.setQuantidade(1);
        livro.setQuantidadeDisponivel(1);

        Livro salvo = livroRepository.save(livro);
        return livroService.toResponseDTO(salvo);
    }

    private String extrairIsbn(VolumeInfoDTO info) {
        if (info.getIndustryIdentifiers() == null) {
            return null;
        }
        return info.getIndustryIdentifiers().stream()
                .filter(i -> "ISBN_13".equals(i.getType()))
                .map(IndustryIdentifierDTO::getIdentifier)
                .findFirst()
                .orElseGet(() -> info.getIndustryIdentifiers().stream()
                        .filter(i -> "ISBN_10".equals(i.getType()))
                        .map(IndustryIdentifierDTO::getIdentifier)
                        .findFirst()
                        .orElse(null));
    }

    private String extrairAutor(VolumeInfoDTO info) {
        if (info.getAuthors() == null || info.getAuthors().isEmpty()) {
            return "Autor desconhecido";
        }
        return String.join(", ", info.getAuthors());
    }

    private Integer extrairAno(VolumeInfoDTO info) {
        if (info.getPublishedDate() == null || info.getPublishedDate().length() < 4) {
            return 0;
        }
        try {
            return Integer.parseInt(info.getPublishedDate().substring(0, 4));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}