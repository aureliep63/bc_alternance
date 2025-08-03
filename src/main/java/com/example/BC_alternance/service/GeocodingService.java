package com.example.BC_alternance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LatLng geocodeAddress(String adresse) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                    .queryParam("q", adresse)
                    .queryParam("format", "json")
                    .queryParam("limit", 1)
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (root.isArray() && root.size() > 0) {
                JsonNode location = root.get(0);
                double lat = location.get("lat").asDouble();
                double lon = location.get("lon").asDouble();
                return new LatLng(lat, lon);
            } else {
                System.out.println("Aucun résultat pour l'adresse : " + adresse);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class LatLng {
        public final double lat;
        public final double lon;

        public LatLng(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}
