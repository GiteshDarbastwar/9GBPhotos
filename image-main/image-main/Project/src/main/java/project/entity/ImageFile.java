package project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;

    private String fileName;
    private String contentType;
}
