package com.example.demo.controller;

import com.example.demo.service.CsvReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private CsvReaderService csvReaderService;

    @GetMapping("/columns")
    public List<String> getColumns() throws Exception {

        return csvReaderService.getColumns(
                "uploads/employees.csv"
        );
    }

    @GetMapping("/data")
    public List<Map<String, String>> getData() throws Exception {

        return csvReaderService.getData(
                "uploads/employees.csv"
        );
    }
}