package com.example.BC_alternance.repository;

import com.example.BC_alternance.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUtilisateurId(Long id);
    List<Reservation> findByBorneId(Long id);

}


