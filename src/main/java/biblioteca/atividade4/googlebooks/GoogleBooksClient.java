package biblioteca.atividade4.googlebooks;

import biblioteca.atividade4.googlebooks.dto.GoogleBooksResponseDTO;
import biblioteca.atividade4.googlebooks.dto.VolumeDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleBooksClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    public GoogleBooksClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GoogleBooksResponseDTO buscarPorTermo(String termo) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("q", termo)
                .toUriString();
        return restTemplate.getForObject(url, GoogleBooksResponseDTO.class);
    }

    public VolumeDTO buscarPorId(String volumeId) {
        String url = BASE_URL + "/" + volumeId;
        return restTemplate.getForObject(url, VolumeDTO.class);
    }
}