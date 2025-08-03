package com.example.BC_alternance.repository;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorneRepository extends JpaRepository<Borne, Long> {
    List<Borne> findByUtilisateurId(Long id);
    List<Borne> findByReservations_Id(Long id);
    List<Borne> findByMedias_Id(Long id);

    @Query("""
SELECT b
FROM Borne b
LEFT JOIN b.lieux l
WHERE NOT EXISTS (
  SELECT 1
  FROM Reservation r
  WHERE r.borne = b
    AND (
      (:debut < r.dateFin AND :fin > r.dateDebut)
    )
)
AND (:ville IS NULL OR l.ville IS NULL OR LOWER(l.ville) LIKE LOWER(CONCAT('%', :ville, '%')))
""")
    List<Borne> findBornesDisponibles(
            @Param("ville") String ville,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin
    );

}
