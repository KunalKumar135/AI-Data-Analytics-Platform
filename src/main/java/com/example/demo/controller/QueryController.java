package com.example.demo.controller;
import com.example.demo.dto.AIResponse;
import com.example.demo.dto.QueryRequest;
import com.example.demo.dto.QueryResultResponse;
import com.example.demo.service.AIService;
import com.example.demo.service.DatasetSchemaService;
// import com.example.demo.service.OllamaService;
import com.example.demo.service.QueryExecutionService;
import com.example.demo.dto.SqlRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private AIService aiService;

    @Autowired
    private QueryExecutionService queryExecutionService;

    @Autowired
    private DatasetSchemaService datasetSchemaService;

    @PostMapping("/ask")
    public Object askQuestion(
            @RequestBody QueryRequest request) {

        try {

            String tableName =
                request.getTableName();

            List<String> columns =
                    datasetSchemaService.getColumns(
                            tableName);

            AIResponse aiResponse =
                aiService.generateSQL(
                        request.getQuestion(),
                        tableName,
                        columns);

                String sql = aiResponse.getSql();
                if ("INVALID_QUERY".equalsIgnoreCase(sql)) {

                QueryResultResponse response = new QueryResultResponse();

                response.setQuestion(request.getQuestion());
                response.setSql("No SQL generated");
                response.setExplanation(aiResponse.getExplanation());
                response.setAnswer("Unable to answer the question.");

                return response;
                }
                String explanation = aiResponse.getExplanation();
                
                System.out.println("Executing SQL:");
                System.out.println(sql);
                List<Map<String, Object>> result =
                        queryExecutionService
                                .executeQuery(sql);

            QueryResultResponse response =
                    new QueryResultResponse();

            response.setQuestion(
                    request.getQuestion());

            response.setSql(sql);

            response.setExplanation(explanation);

            response.setResult(result);
                
            System.out.println(result);

            response.setAnswer(
                convertResultToText(result));

            return response;

        } catch (Exception e) {

            e.printStackTrace();

            return e.getMessage();
        }
    }

    @GetMapping("/schema/{id}")
    public List<String> getSchema(
            @PathVariable Long id) {

        String tableName =
                "dataset_" + id;

        return datasetSchemaService
                .getColumns(tableName);
    }

    @PostMapping("/generate-sql")
    public String generateSql(
            @RequestBody SqlRequest request) {

        return aiService.generateGeneralSQL(
                request.getQuestion());
    }
    private String convertResultToText(
        List<Map<String, Object>> result) {

        if (result == null || result.isEmpty()) {

                return "No records found.";
        }

        StringBuilder answer =
                new StringBuilder();

        for (Map<String, Object> row : result) {

                for (Object value : row.values()) {

                answer.append(value)
                        .append(", ");
                }
        }

        return answer.substring(
                0,
                answer.length() - 2
        );
        }
}