package com.example.demo.service;

import com.example.demo.dto.AIResponse;
import com.example.demo.dto.OllamaResponse;
import org.springframework.http.*;
// import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// @Service
public class OllamaService implements AIService {

    private final RestTemplate restTemplate =
            new RestTemplate();

    private String extractSql(String response) {

        response = response.trim();

        if (response.startsWith("```sql")) {

            response = response
                    .replace("```sql", "")
                    .replace("```", "")
                    .trim();
        }

        return response;
    }

    public String explainSQL(String sql) {

        try {

            String prompt = """
                    Explain the following SQL query
                    in one simple sentence.

                    SQL:
                    %s
                    """
                    .formatted(sql);

            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");

            String requestBody = """
                    {
                    "model": "qwen2.5",
                    "prompt": "%s",
                    "stream": false
                    }
                    """
                    .formatted(escapedPrompt);

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            HttpEntity<String> entity =
                    new HttpEntity<>(
                            requestBody,
                            headers);

            ResponseEntity<OllamaResponse> response =
                    restTemplate.exchange(
                            "http://localhost:11434/api/generate",
                            HttpMethod.POST,
                            entity,
                            OllamaResponse.class
                    );

            return response.getBody()
                    .getResponse();

        } catch (Exception e) {

            return "Explanation unavailable";
        }
    }
    private AIResponse parseResponse(String response) {

        AIResponse ai = new AIResponse();

        String[] parts = response.split("Explanation:", 2);

        String sql = parts[0]
                .replace("SQL:", "")
                .trim();

        ai.setSql(sql);

        if (parts.length > 1) {
                ai.setExplanation(parts[1].trim());
        } else {
                ai.setExplanation("No explanation available.");
        }

        return ai;
        }

    @Override
    public AIResponse generateSQL(
            String question,
            String tableName,
            List<String> columns) {

        try {

            String prompt = """
                You are a PostgreSQL SQL generator.

                Generate a PostgreSQL SQL query and a short explanation.

                Table Name:
                %s

                Columns:
                %s

                Question:
                %s

                Rules:
                1. Use only the given table and columns.
                2. Return ONLY in the exact format below.
                3. Do NOT use markdown.
                4. Do NOT use ```sql.
                5. Do NOT add any extra text.

                SQL:
                SELECT ...

                Explanation:
                One simple sentence explaining what the SQL query does.
                """
                .formatted(
                        tableName,
                        String.join(", ", columns),
                        question
                );

            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");

            String requestBody = """
                    {
                      "model": "qwen2.5",
                      "prompt": "%s",
                      "stream": false
                    }
                    """
                    .formatted(escapedPrompt);

            String url =
                    "http://localhost:11434/api/generate";

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            HttpEntity<String> entity =
                    new HttpEntity<>(
                            requestBody,
                            headers);

            ResponseEntity<OllamaResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            OllamaResponse.class
                    );

            String aiResponse =
                response.getBody()
                .getResponse();

            System.out.println("========== AI RESPONSE ==========");
            System.out.println(aiResponse);
            System.out.println("=================================");

                return parseResponse(aiResponse);

            

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to generate SQL",
                    e
            );
        }
    }

    @Override
    public String generateGeneralSQL(
            String question) {
                System.out.println("generateGeneralSQL() called");

        try {

            String prompt = """
                    You are a PostgreSQL SQL generator.

                    Rules:
                    1. Return ONLY SQL.
                    2. Do NOT explain.
                    3. Do NOT use markdown.
                    4. Do NOT use ```sql.
                    5. Response must contain only SQL.

                    Question:
                    %s
                    """
                    .formatted(question);

            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");

            String requestBody = """
                    {
                    "model": "qwen2.5",
                    "prompt": "%s",
                    "stream": false
                    }
                    """
                    .formatted(escapedPrompt);

            String url =
                    "http://localhost:11434/api/generate";

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            HttpEntity<String> entity =
                    new HttpEntity<>(
                            requestBody,
                            headers);

            ResponseEntity<OllamaResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            OllamaResponse.class
                    );

            String sql =
                    response.getBody()
                            .getResponse();

            sql = extractSql(sql);

            sql = sql.replace("\n", " ")
                    .replace("\r", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

            return sql;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to generate SQL",
                    e
            );
        }
    }
}