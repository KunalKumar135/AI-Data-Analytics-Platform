package com.example.demo.service;

import com.example.demo.entity.Dataset;
import com.example.demo.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads";

    @Autowired
    private DatasetRepository datasetRepository;

    public void saveFile(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath =
                uploadPath.resolve(file.getOriginalFilename());

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // Save metadata in PostgreSQL
        Dataset dataset = new Dataset();
        dataset.setfileName(file.getOriginalFilename());
        dataset.setLocalDateTime(LocalDateTime.now());

        datasetRepository.save(dataset);
    }
}