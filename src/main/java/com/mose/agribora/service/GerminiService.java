package com.mose.agribora.service;

import com.mose.agribora.dto.WeatherData;
import com.mose.agribora.entity.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GerminiService {

    // NOTE: Store API keys securely in environment variables or @Value
    @Value("${germini.api.key}")
    private String API_KEY;
    private final String MODEL_NAME = "models/gemini-2.5-flash";
    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/"
            + MODEL_NAME + ":generateContent?key=" + API_KEY;

    public String getPrediction(String location, WeatherData weather, Profile profile) {
        RestTemplate restTemplate = new RestTemplate();

        // Safely extract rainfall (default "0")
        String rainfall = "0";
        if (weather.getRain() != null) {
            if (weather.getRain()!= null) rainfall = String.valueOf(weather.getRain());
            else if (weather.getRain()!= null) rainfall = String.valueOf(weather.getRain());
        }

        // Construct full detailed prompt
        String prompt = String.format("""
                You are an advanced agricultural AI model that analyzes weather data to generate actionable farming insights.
                Given the following data:
                - Location: %s
                - Weather: Temp = %.1f°C, Humidity = %d%%, Rainfall = %s mm
                - Type of farming: %s (%s)

                Predict the most suitable crops and potential risks (pests, floods, droughts)
                for the coming month. Provide clear, actionable recommendations.

                Include the following sections in your report:

                1. **Risk Level** — LOW, MODERATE, or HIGH.
                   Explain briefly what the risk means (e.g., high rainfall could cause waterlogging, or dry conditions could stress crops).

                2. **Rainfall Forecast**
                   - Short descriptive label (e.g., "Light showers", "Heavy rainfall", "Dry conditions").
                   - Estimated rainfall amount in millimeters.
                   - One to two sentences giving irrigation or drainage guidance.

                3. **Temperature Trend**
                   - Provide a temperature range based on the given average temperature (e.g., "22–29°C").
                   - One to two sentences describing the expected effects on crops or livestock.

                4. **AI Analysis & Recommendations**
                   Write a short paragraph analyzing the combined data (temperature, humidity, cloud cover) and what it means for plant growth, soil moisture, or disease risks.

                5. **Recommended Actions**
                   Give 3–5 bullet points suggesting practical steps for the farmer, such as:
                   - When to irrigate or reduce watering
                   - Fertilizer scheduling
                   - Pest/disease management
                   - Field preparation
                   - Crop protection or planting tips

                Style guidelines:
                - Keep the tone professional, concise, and farmer-friendly.
                - Avoid technical jargon.
                - Structure output as labeled sections for display in a web dashboard.

                Example output format:
                ---
                **Risk Level:** LOW 
                Slightly above average rainfall. Good for plant growth. Monitor for waterlogging in low areas.
                **Rainfall Forecast:** Light showers (30mm total)
                Expected rainfall for the selected period. Plan irrigation and drainage accordingly
                **Temperature Trend:** 22–29°C 
                Mild conditions suitable for most crops. Monitor heat stress on livestock.
                **AI Analysis:** Slightly above average humidity and cloud cover suggest stable soil moisture levels and moderate growth conditions.
                **Recommended Actions:**
                - Maintain regular farm activities
                - Apply fertilizers as scheduled
                - Monitor for fungal infections
                - Continue pest and disease control routines
                - Ensure proper drainage in low-lying fields

                Generate fresh, natural language output every time using realistic values and variation.
                """,
                location,
                weather.getMain().getTemp(),          // double -> %.1f
                weather.getMain().getHumidity(),      // int -> %d
                rainfall,                             // String -> %s
                profile.getFarmingType(),             // String -> %s
                profile.getSpecification()            // String -> %s
        );

        // Build Gemini request body
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        // Setup headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Execute API call
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_API_URL, entity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body == null) return "❌ API returned empty body.";

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "⚠️ No prediction generated.";

            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

            return (parts != null && !parts.isEmpty())
                    ? parts.get(0).get("text").toString()
                    : "⚠️ Prediction generated but text is empty.";

        } catch (HttpClientErrorException e) {
            return String.format("❌ HTTP Error: %s. Response: %s",
                    e.getStatusCode(),
                    e.getResponseBodyAsString().substring(0, Math.min(200, e.getResponseBodyAsString().length())) + "...");
        } catch (ResourceAccessException e) {
            return "❌ Network error: Could not reach Gemini API.";
        } catch (Exception e) {
            return "❌ Unexpected error: " + e.getMessage();
        }
    }
}
