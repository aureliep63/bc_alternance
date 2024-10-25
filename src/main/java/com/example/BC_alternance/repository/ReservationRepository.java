package com.example.BC_alternance.repository;

import com.example.BC_alternance.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
