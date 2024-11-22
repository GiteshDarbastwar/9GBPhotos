package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.Image;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
