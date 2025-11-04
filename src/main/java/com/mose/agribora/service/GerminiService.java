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
    private String API_KEY = "AIzaSyCXy1t9tMxHPqWwEmNgXkcerBQd8sacsh4";
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
                profile.getAnimalType(),
                profile.getCropType()// String -> %s
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
            if (body == null) return "API returned empty body.";

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "No prediction generated.";

            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

            return (parts != null && !parts.isEmpty())
                    ? parts.get(0).get("text").toString()
                    : "Prediction generated but text is empty.";

        } catch (HttpClientErrorException e) {
            return String.format("❌ HTTP Error: %s. Response: %s",
                    e.getStatusCode(),
                    e.getResponseBodyAsString().substring(0, Math.min(200, e.getResponseBodyAsString().length())) + "...");
        } catch (ResourceAccessException e) {
            return "Network error: Could not reach Gemini API.";
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    public String getRearingStages(String location, WeatherData weather, Profile profile) {
        RestTemplate restTemplate = new RestTemplate();

        // **Clean-up:** Safely extract rainfall, converting it to String if it's a Number (assuming getRain is usually a Number/Double/String in the DTO)
        String rainfall = "0";
        if (weather.getRain() != null) {
            rainfall = String.valueOf(weather.getRain());
        }

        // 1. Construct the detailed user prompt (already very good!)
        String prompt = String.format("""
            You are an advanced agricultural AI model that provides detailed livestock rearing insights based on weather and farm type.
            Given the following data:
            - Location: %s
            - Weather: Temp = %.1f°C, Humidity = %d%%, Rainfall = %s mm
            - Type of farming: %s (%s)

            Generate a structured livestock rearing report that includes:
            
            1. **Rearing Stage Overview**
                - Identify which rearing stage the livestock are likely in (e.g., early growth, breeding, fattening, or lactation).
                - Describe the physiological and nutritional focus at this stage.

            2. **Environmental Conditions & Comfort**
                - Describe how current temperature, humidity, and rainfall affect livestock health, productivity, or stress levels.
                - Suggest environmental control actions (e.g., shelter cooling, ventilation, or bedding management).

            3. **Feeding & Nutrition**
                - Recommend specific feeding strategies, quantities, and supplements for this stage.
                - Suggest local feed options or substitutes where relevant.

            4. **Health & Disease Management**
                - Highlight potential disease risks or stress factors based on the weather.
                - Suggest 2–3 preventive actions (e.g., vaccination, deworming, hydration management).

            5. **AI Summary Recommendations**
                - A short paragraph summarizing key actions for optimal livestock performance.

            Style guidelines:
            - Keep tone farmer-friendly and actionable.
            - Avoid overly technical veterinary language.
            - Output should be clear, sectioned, and visually readable for a dashboard or SMS summary.
            """,
                location,
                weather.getMain().getTemp(),
                weather.getMain().getHumidity(),
                rainfall,
                profile.getFarmingType(),
                profile.getAnimalType(),
                profile.getCropType()
        );

        // 2. Build the CORRECT Gemini request body (Structure: contents -> parts -> text)
        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> contentBlock = Map.of("parts", List.of(textPart));
        Map<String, Object> requestBody = Map.of("contents", List.of(contentBlock));

        // 3. Setup Request Entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 4. Execute API Call with robust error handling
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body == null) {
                return "API returned success but with an empty body.";
            }

            // 5. Safely Parse the Response
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "No rearing guidance generated. (Check safety settings).";
            }

            // Drill down: candidates[0].content.parts[0].text
            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

            // Final check and return
            return (parts != null && !parts.isEmpty())
                    ? parts.get(0).get("text").toString()
                    : "Generated response was empty.";

        } catch (HttpClientErrorException e) {
            // Catches 4xx/5xx HTTP errors (e.g., 400 Bad Request, 401 Unauthorized)
            String errorBody = e.getResponseBodyAsString();
            String truncatedError = errorBody.substring(0, Math.min(200, errorBody.length())) + "...";
            return String.format("❌ HTTP Error (%s): %s", e.getStatusCode(), truncatedError);
        } catch (ResourceAccessException e) {
            // Catches network/connectivity errors (e.g., timeout)
            return "Network Error: Could not reach Gemini API. Check connectivity.";
        } catch (Exception e) {
            // General catch-all for any other unexpected exceptions (e.g., JSON parsing)
            return "Unexpected error during API processing: " + e.getMessage();
        }
    }



    public String generateSmartAlert(String location, Profile profile) {
        RestTemplate restTemplate = new RestTemplate();

        // Build the AI prompt
        String prompt = String.format("""
        You are a smart agricultural monitoring assistant.
        A farmer in %s has encountered the following situation:
        "%s"

        Farmer profile:
        - Type of farming: %s
        - Specification: %s

        Generate a short but informative alert message to notify the farmer.
        Your alert should:
        - Be clear and professional.
        - Give 1–2 key actions or advice.
        - Mention the urgency if needed.
        - Be written in simple language suitable for SMS, email, or app notifications.

        Example output:
        ---
        "Low Soil Moisture detected in Nakuru. Please irrigate your crops today to prevent stress."
        or
        "🌧️ Heavy rainfall expected in Kisumu. Ensure proper drainage to avoid root rot."
        ---
        """,
                location,
                profile.getFarmingType(),
                profile.getAnimalType(),
                profile.getCropType()
        );

        // Gemini API Request Body
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        // Setup headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_API_URL, entity, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null) return "Empty Gemini API response.";

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "No alert generated.";

            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

            return (parts != null && !parts.isEmpty())
                    ? parts.get(0).get("text").toString()
                    : "Generated alert text is empty.";

        } catch (HttpClientErrorException e) {
            return String.format("HTTP Error: %s. Response: %s",
                    e.getStatusCode(),
                    e.getResponseBodyAsString().substring(0, Math.min(200, e.getResponseBodyAsString().length())) + "...");
        } catch (ResourceAccessException e) {
            return "Network error: Could not reach Gemini API.";
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }


}
