package com.example.BC_alternance.repository;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorneRepository extends JpaRepository<Borne
        , Long> {
    List<Borne> findByUtilisateurId(Long id);

    List<Borne> findByReservations_Id(Long id);

    List<Borne> findByMedias_Id(Long id);


}
