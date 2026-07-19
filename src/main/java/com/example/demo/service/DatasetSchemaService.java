package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasetSchemaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getColumns(String tableName) {

        String sql = """
            SELECT column_name
            FROM information_schema.columns
            WHERE table_name = ?
            ORDER BY ordinal_position
            """;

        return jdbcTemplate.queryForList(
                sql,
                String.class,
                tableName
        );
    }
}