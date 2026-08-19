package biblioteca.atividade4.googlebooks.dto;

import lombok.Data;
import java.util.List;

@Data
public class GoogleBooksResponseDTO {
    private Integer totalItems;
    private List<VolumeDTO> items;
}