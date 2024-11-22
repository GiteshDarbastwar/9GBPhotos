package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.ImageFile;

import java.util.UUID;

public interface ImageFileRepository extends JpaRepository<ImageFile, UUID> {}

