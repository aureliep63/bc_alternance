package com.example.BC_alternance.repository;

import com.example.BC_alternance.model.Borne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorneRepository extends JpaRepository<Borne
        , Long> {
}
