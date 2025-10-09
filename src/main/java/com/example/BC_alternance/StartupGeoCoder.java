package com.example.BC_alternance;

import com.example.BC_alternance.service.LieuxService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupGeoCoder implements ApplicationRunner {

    private final LieuxService lieuxService;

    public StartupGeoCoder(LieuxService lieuxService) {
        this.lieuxService = lieuxService;
    }

    @Override
    public void run(ApplicationArguments args) {
        // lance le géocodage une seule fois au démarrage
        lieuxService.updateCoordinatesForAllLieuxWithoutCoords();
    }
}
