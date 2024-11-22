package project.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ImageFileDto {
    private UUID id;
    private String fileName;
    private String contentType;
    private String imageData;
}
