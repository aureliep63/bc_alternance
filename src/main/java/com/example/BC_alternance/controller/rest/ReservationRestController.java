package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.AvailabilityCheckDto;
import com.example.BC_alternance.dto.ReservationDto;
import com.example.BC_alternance.mapper.ReservationMapper;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationRestController {

    private final ReservationService reservationService;
    private ReservationMapper reservationMapper;


    public ReservationRestController(ReservationService reservationService) {
        this.reservationService=reservationService;

    }

    @GetMapping("")
    @Operation(summary = "Affiche toutes les réservations", description = "Affiche toutes les réservations ")
    public List<ReservationDto> getAllReservations(){
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Affiche une réservation", description = "Affiche une réservation par son ID")
    public ReservationDto getReservationById(@PathVariable Long id){
        return reservationService.getReservationById(id);
    }


@PostMapping("")
@Operation(summary = "Ajoute une nouvelle réservation",
        description = "Ajoute une nouvelle reservation")
public ResponseEntity<Void> saveReservation(
        @RequestBody @Valid ReservationDto reservationDto,
        BindingResult bindingResult) {
    reservationService.saveReservation(reservationDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
}


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprime une réservation", description = "Supprime une reservation par son ID")
    public void deleteReservation(@PathVariable Long id){
        reservationService.deleteReservation(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifie une réservation", description = "Modifie une réservation par son ID")
    public ReservationDto updateReservation(@PathVariable Long id, @RequestBody @Valid ReservationDto reservationDto , BindingResult bindingResult) {
        reservationDto.setId(id);
        Reservation reservationUpdate = reservationService.saveReservation(reservationDto);
        return this.reservationMapper.toDto(reservationUpdate);
    }

    @GetMapping("/user/{idUser}/reservations")
    @Operation(summary = "Affiche la réservation d'un user", description = "Affiche la réservation d'un user par l'ID du User")
    public List<ReservationDto> getBornesByUser(@PathVariable Long idUser) {
        return reservationService.getReservationByUserId(idUser);
    }

    @GetMapping("/borne/{idBorne}/reservations")
    @Operation(summary = "Affiche la réservation d'une borne ",
            description = "Affiche la réservation faite sur une borne par l'ID de la Borne")
    public List<ReservationDto> getRservationByBorne(@PathVariable Long idBorne) {
        return reservationService.getReservationsByBorneId(idBorne);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Modifie le status d'une réservation",
            description = "Modifie le status d'une réservation par son ID")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody @Valid Map<String, String> body,
                                          BindingResult bindingResult) {
        String newStatus = body.get("status");
        reservationService.updateStatus(id, newStatus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-availability")
    @Operation(summary = "Vérifie la disponibilité d'une borne", description = "Vérifie si une borne est disponible pour une période donnée")
    public ResponseEntity<Boolean> checkAvailability(@RequestBody AvailabilityCheckDto availabilityCheckDto) {
        boolean isAvailable = reservationService.isBorneAvailable(
                availabilityCheckDto.getBorneId(),
                availabilityCheckDto.getDateDebut(),
                availabilityCheckDto.getDateFin()
        );
        return ResponseEntity.ok(isAvailable);
    }
}
