package com.example.demo.controller;

import com.example.demo.service.CsvReaderService;
import com.example.demo.service.DatasetService;
import com.example.demo.service.DynamicTableService;
import com.example.demo.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private CsvReaderService csvReaderService;

    @Autowired
    private DynamicTableService dynamicTableService;

    @PostMapping("/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        try {

        // Save file
        fileStorageService.saveFile(file);

        // Save metadata
        datasetService.saveDataset(
                file.getOriginalFilename());

        String filePath =
                "uploads/" +
                file.getOriginalFilename();

        // Detect column types
        Map<String, String> columnTypes =
                csvReaderService
                        .detectColumnTypes(
                                filePath);

        // Table name from file name
        String tableName =
                file.getOriginalFilename()
                        .replace(".csv", "")
                        .replaceAll(
                                "[^a-zA-Z0-9_]",
                                "_")
                        .toLowerCase();

        // Create table
        dynamicTableService.createTable(
                tableName,
                columnTypes);

        // Read CSV data
        List<Map<String, String>> data =
                csvReaderService.getData(
                        filePath);

        // Insert data
        dynamicTableService.insertData(
                tableName,
                data);

        Map<String, String> response =
        new HashMap<>();

        response.put(
                "tableName",
                tableName);

        return response;

        } catch (Exception e) {

                e.printStackTrace();

                return "Upload failed: "
                        + e.getMessage();
        }
    }
    
}
