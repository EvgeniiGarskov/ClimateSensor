package ru.garskov.springcourse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        String newSensorName = "Sensor 7";

        addSensor(newSensorName);

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            double value = 40 * random.nextDouble();
            boolean raining = random.nextBoolean();

            addMeasurement(value, raining, newSensorName);
        }
    }

    private static void addSensor(String newSensorName) {

        String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonToSend = new HashMap<>();
        jsonToSend.put("name", newSensorName);

        makePostRequestWithJSONData(url, jsonToSend);
    }

    private static void addMeasurement(double value, boolean raining, String newSensorName) {

        String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonToSend = new HashMap<>();
        jsonToSend.put("value", value);
        jsonToSend.put("raining", raining);
        jsonToSend.put("sensor", Map.of("name", newSensorName));

        makePostRequestWithJSONData(url, jsonToSend);
    }

    public static void getMeasurements() {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/measurements";

        String response = restTemplate.getForObject(url, String.class);

        System.out.println("Response: " + response);
    }

    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Измерение успешно отправлено на сервер!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }
}