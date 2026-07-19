package com.example.demo.service;
import com.example.demo.dto.AIResponse;
import java.util.List;

public interface AIService {

    AIResponse generateSQL(
            String question,
            String tableName,
            List<String> columns);

    String generateGeneralSQL(
            String question);
}