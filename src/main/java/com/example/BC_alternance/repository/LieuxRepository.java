package com.example.BC_alternance.repository;

import com.example.BC_alternance.model.Lieux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LieuxRepository extends JpaRepository<Lieux, Long > {
    List<Lieux> findByLatitudeIsNullOrLongitudeIsNull();

}
