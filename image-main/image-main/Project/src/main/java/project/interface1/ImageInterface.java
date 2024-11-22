package project.interface1;

import project.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageInterface {

    ImageDto createImage(ImageDto imageDto, List<MultipartFile> imageMultipart) throws IOException;


    List<ImageDto> getAllWithFiles();


    byte[] getImageById(UUID fileId);
}
