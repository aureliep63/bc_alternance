package com.example.BC_alternance.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/geocode")
public class GeocodingController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<String> geocodeCity(@RequestParam("q") String city) {
        String nominatimUrl = "https://nominatim.openstreetmap.org/search?q=" + city + "&format=json&limit=1";
        try {
            String response = restTemplate.getForObject(nominatimUrl, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving geocoding data.");
        }
    }

}
