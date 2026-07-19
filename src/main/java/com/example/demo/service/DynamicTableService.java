package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class DynamicTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable(
        String tableName,
        Map<String, String> columnTypes) {

        // Drop existing table if it exists
        jdbcTemplate.execute("DROP TABLE IF EXISTS \"" + tableName + "\"");

        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE \"")
        .append(tableName)
        .append("\" (");

        for (Map.Entry<String, String> entry : columnTypes.entrySet()) {

            sql.append(entry.getKey())
                    .append(" ")
                    .append(entry.getValue())
                    .append(",");
        }

        sql.deleteCharAt(sql.length() - 1);

        sql.append(")");

        System.out.println("CREATE TABLE SQL:");
        System.out.println(sql);

        jdbcTemplate.execute(sql.toString());
    }

    public void insertData(
        String tableName,
        List<Map<String, String>> data) {

        for (Map<String, String> row : data) {

            StringJoiner columns = new StringJoiner(",");
            StringJoiner values = new StringJoiner(",");

            for (Map.Entry<String, String> entry : row.entrySet()) {

                columns.add(entry.getKey());

                values.add("'" +
                        entry.getValue().replace("'", "''")
                        + "'");
            }

            String sql =
                    "INSERT INTO " + tableName +
                    " (" + columns + ")" +
                    " VALUES (" + values + ")";

            jdbcTemplate.execute(sql);
        }
    }
}