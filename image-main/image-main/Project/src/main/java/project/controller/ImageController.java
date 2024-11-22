package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.ImageDto;
import project.service.ImageService;
import project.utils.Response;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Response> createImage(@RequestPart List<MultipartFile> images) {
        try {
            ImageDto imageDto = imageService.createImage(new ImageDto(), images);
            Response response = new Response("Images uploaded successfully", imageDto, false);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Response errorResponse = new Response("Failed to upload images", e.getMessage(), true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<Response> getAll() {
        try {
            List<ImageDto> images = imageService.getAllWithFiles();
            Response response = new Response("Images retrieved successfully", images, false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Response errorResponse = new Response("Failed to retrieve images", e.getMessage(), true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Endpoint to retrieve an image by its ID
    @GetMapping("/download")
    public ResponseEntity<byte[]> getImageById(@RequestParam UUID id) {
        try {
            byte[] image = imageService.getImageById(id);
            if (image != null) {
                return ResponseEntity.ok()
                        .header("Content-Type", "image/jpeg")
                        .body(image);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
