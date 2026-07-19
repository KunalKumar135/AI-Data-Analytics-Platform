package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GroqService implements AIService {

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

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

    private AIResponse parseResponse(String response) {

        AIResponse ai = new AIResponse();

        response = response.trim();

        String sql;
        String explanation = "";

        if (response.contains("Explanation:")) {

            String[] parts = response.split("Explanation:", 2);

            sql = parts[0]
                    .replace("SQL:", "")
                    .trim();

            explanation = parts[1].trim();

        } else {

            // Fallback when Groq ignores the format
            String[] lines = response.split("\\R");

            sql = lines[0].trim();

            if (lines.length > 1) {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i < lines.length; i++) {
                    sb.append(lines[i]).append(" ");
                }

                explanation = sb.toString().trim();
            }
        }

        ai.setSql(sql);
        ai.setExplanation(explanation);

        return ai;
    }

    public String explainSQL(String sql) {

        try {

            String prompt = """
                    Explain the following PostgreSQL query
                    in one simple sentence.

                    SQL:
                    %s
                    """.formatted(sql);

            GroqRequest request =
                    new GroqRequest();

            request.setModel(model);

            request.setMessages(
                List.of(

                    new Message(
                        "system",
                        """
                        You are a PostgreSQL SQL generator.

                        Always follow the user's format exactly.

                        If asked for SQL and Explanation,
                        ALWAYS output:

                        SQL:
                        <query>

                        Explanation:
                        <one sentence>

                        Never omit SQL: or Explanation:.
                        Never add extra text.
                        """
                    ),

                    new Message(
                        "user",
                        prompt
                    )
                )
            );

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            headers.setBearerAuth(apiKey);

            HttpEntity<GroqRequest> entity =
                    new HttpEntity<>(
                            request,
                            headers);

            ResponseEntity<GroqResponse> response =
                    restTemplate.exchange(
                            apiUrl,
                            HttpMethod.POST,
                            entity,
                            GroqResponse.class
                    );

            return response.getBody()
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

        } catch (Exception e) {

            return "Explanation unavailable";
        }
    }
        @Override
    public AIResponse generateSQL(
            String question,
            String tableName,
            List<String> columns) {

        try {

            String prompt = """
                    You are an expert PostgreSQL SQL generator.

                    Generate a PostgreSQL SQL query and a short explanation.

                    Table Name:
                    %s

                    Columns:
                    %s

                    Question:
                    %s

                    Rules:

                    1. Use ONLY the given table name.
                    2. Use ONLY the given columns.
                    3. Select ONLY the columns required to answer the user's question.
                    4. Do NOT use SELECT * unless the user explicitly requests all details.
                    5. Do NOT use markdown.
                    6. Do NOT add any extra text.
                    7. If the question cannot be answered using ONLY the given table and columns, DO NOT guess.

                    If the question CANNOT be answered, respond EXACTLY like this:

                    SQL:
                    INVALID_QUERY

                    Explanation:
                    The requested information is not available in the selected dataset.

                    Otherwise, respond EXACTLY like this:

                    SQL:
                    SELECT ...;

                    Explanation:
                    One simple sentence explaining what the SQL query does.
                    """
                    .formatted(
                            tableName,
                            String.join(", ", columns),
                            question
                    );

            GroqRequest request =
                    new GroqRequest();

            request.setModel(model);

            request.setMessages(
                    List.of(
                            new Message(
                                    "user",
                                    prompt
                            )
                    )
            );

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            headers.setBearerAuth(apiKey);

            HttpEntity<GroqRequest> entity =
                    new HttpEntity<>(
                            request,
                            headers
                    );

            ResponseEntity<GroqResponse> response =
                    restTemplate.exchange(
                            apiUrl,
                            HttpMethod.POST,
                            entity,
                            GroqResponse.class
                    );

            String aiResponse =
                    response.getBody()
                            .getChoices()
                            .get(0)
                            .getMessage()
                            .getContent();

            System.out.println("========== GROQ RESPONSE ==========");
            System.out.println(aiResponse);
            System.out.println("===================================");

            AIResponse parsed = parseResponse(aiResponse);

            System.out.println("Parsed SQL:");
            System.out.println(parsed.getSql());

            System.out.println("Parsed Explanation:");
            System.out.println(parsed.getExplanation());

            return parsed;

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

        try {

            String prompt = """
                    You are an expert PostgreSQL SQL generator.

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

            GroqRequest request =
                    new GroqRequest();

            request.setModel(model);

            request.setMessages(
                    List.of(
                            new Message(
                                    "user",
                                    prompt
                            )
                    )
            );

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON);

            headers.setBearerAuth(apiKey);

            HttpEntity<GroqRequest> entity =
                    new HttpEntity<>(
                            request,
                            headers
                    );

            ResponseEntity<GroqResponse> response =
                    restTemplate.exchange(
                            apiUrl,
                            HttpMethod.POST,
                            entity,
                            GroqResponse.class
                    );

            String sql =
                    response.getBody()
                            .getChoices()
                            .get(0)
                            .getMessage()
                            .getContent();

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