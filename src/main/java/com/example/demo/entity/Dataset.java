package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private LocalDateTime uploadedAt;

    public Dataset(){
    }

    public String getfileName(){
        return fileName;
    }

    public void setfileName(String fileName){
        this.fileName = fileName;
    }

    public Long getId(){
        return id;
    }

    public LocalDateTime getLocalDateTime(){
        return uploadedAt;
    }

    public void setLocalDateTime(LocalDateTime uploadedAt){
        this.uploadedAt = uploadedAt;
    }

    public void setUploadedAt(LocalDateTime now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUploadedAt'");
    }
}
