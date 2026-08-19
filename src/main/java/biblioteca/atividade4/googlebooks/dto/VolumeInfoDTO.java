package biblioteca.atividade4.googlebooks.dto;

import lombok.Data;
import java.util.List;

@Data
public class VolumeInfoDTO {
    private String title;
    private List<String> authors;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifierDTO> industryIdentifiers;
    private ImageLinksDTO imageLinks;
}