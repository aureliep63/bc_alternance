package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.dto.ReservationDto;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationRestController {

    private ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService){
        this.reservationService=reservationService;
    }

    @GetMapping("")
    public List<ReservationDto> getAllReservations(){
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ReservationDto getReservationById(@PathVariable Long id){
        return reservationService.getReservationById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation saveReservation(@RequestBody ReservationDto reservationDto){
        return reservationService.saveReservation(reservationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id){
        reservationService.deleteReservation(id);
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable Long id, @RequestBody ReservationDto reservationDto) {
        reservationDto.setId(id);
        return reservationService.saveReservation(reservationDto);
    }

    @GetMapping("/user/{idUser}/reservations")
    public List<ReservationDto> getBornesByUser(@PathVariable Long idUser) {
        return reservationService.getReservationByUserId(idUser);
    }

    @GetMapping("/borne/{idBorne}/reservations")
    public List<ReservationDto> getRservationByBorne(@PathVariable Long idBorne) {
        return reservationService.getReservationsByBorneId(idBorne);
    }
}
