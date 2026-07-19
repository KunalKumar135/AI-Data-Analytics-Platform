package com.example.demo.controller;

import com.example.demo.service.DatasetService;
import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DatasetService datasetService;

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file) {

        try {

            fileStorageService.saveFile(file);

            datasetService.saveDataset(
                    file.getOriginalFilename());

            return "File uploaded successfully: "
                    + file.getOriginalFilename();

        } catch (Exception e) {

            return "Upload failed: "
                    + e.getMessage();
        }
    }
}