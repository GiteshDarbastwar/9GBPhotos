package project.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dto.ImageDto;
import project.dto.ImageFileDto;
import project.entity.Image;
import project.entity.ImageFile;
import project.interface1.ImageInterface;
import project.repository.ImageFileRepository;
import project.repository.ImageRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService implements ImageInterface {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageFileRepository imageFileRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ImageDto createImage(ImageDto imageDto, List<MultipartFile> imageMultipart) throws IOException {

        Image image = new Image();
        image = imageRepository.save(image);

        List<ImageFile> imageFiles = new ArrayList<>();
        for (MultipartFile file : imageMultipart) {
            ImageFile imageFile = new ImageFile();
            imageFile.setImage(image);
            imageFile.setFile(file.getBytes()); // Save file as byte array
            imageFile.setFileName(file.getOriginalFilename()); // Set file name
            imageFile.setContentType(file.getContentType()); // Set content type
            imageFiles.add(imageFile);
        }
        imageFileRepository.saveAll(imageFiles); // Save all files at once

        // Associate the image files with the image
        image.setImageFiles(imageFiles);


        List<ImageFileDto> imageFileDtos = new ArrayList<>();
        for (ImageFile imageFile : imageFiles) {
            ImageFileDto imageFileDto = new ImageFileDto();
            imageFileDto.setId(imageFile.getId());
            imageFileDto.setFileName(imageFile.getFileName());
            imageFileDto.setContentType(imageFile.getContentType());

            // Convert the image byte array to Base64 string
            if (imageFile.getFile() != null) {
                String base64Image = Base64.encodeBase64String(imageFile.getFile());
                imageFileDto.setImageData(base64Image);
            } else {
                imageFileDto.setImageData(null);
            }

            imageFileDtos.add(imageFileDto);
        }

        // Set the list of ImageFileDtos in the ImageDto
        imageDto.setImageFiles(imageFileDtos);
        return imageDto;
    }

    @Override
    public List<ImageDto> getAllWithFiles() {
        List<Image> images = imageRepository.findAll();
        List<ImageDto> dtos = new ArrayList<>();

        for (Image image : images) {
            ImageDto dto = mapper.map(image, ImageDto.class);

            List<ImageFileDto> imageFileDtos = new ArrayList<>();
            for (ImageFile imageFile : image.getImageFiles()) {
                ImageFileDto imageFileDto = new ImageFileDto();
                imageFileDto.setId(imageFile.getId());
                imageFileDto.setFileName(imageFile.getFileName());
                imageFileDto.setContentType(imageFile.getContentType());

                // Convert byte[] to Base64 for easy display on frontend (or use byte[] if you prefer raw data)
                String base64Image = Base64.encodeBase64String(imageFile.getFile());
                imageFileDto.setImageData(base64Image);

                imageFileDtos.add(imageFileDto);
            }
            dto.setImageFiles(imageFileDtos);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public byte[] getImageById(UUID fileId) {
        ImageFile imageFile = imageFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Image file not found"));
        return imageFile.getFile();
    }
}
