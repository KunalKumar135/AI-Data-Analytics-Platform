package com.example.demo.service;

import com.example.demo.entity.Dataset;
import com.example.demo.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DatasetService {

    @Autowired
    private DatasetRepository datasetRepository;

    public Dataset saveDataset(String fileName) {

        Dataset dataset = new Dataset();

        dataset.setfileName(fileName);
        dataset.setLocalDateTime(LocalDateTime.now());

        return datasetRepository.save(dataset);
    }

    public Dataset getLatestDataset() {

        return datasetRepository
                .findTopByOrderByIdDesc();
    }
}