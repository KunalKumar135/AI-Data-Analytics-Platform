package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset,Long>{
    Dataset findTopByOrderByIdDesc();
}
