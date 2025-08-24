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


    // Ce sera utilisé pour le cas "ville seule"
    List<Borne> findByLieuxVilleStartingWithIgnoreCase(String ville);

    // Utilisez cette requête personnalisée pour le cas "dates seules"
    // C'est la même que la précédente, mais plus claire.
    @Query("SELECT b FROM Borne b WHERE NOT EXISTS (" +
            "  SELECT 1 FROM Reservation r " +
            "  WHERE r.borne = b AND r.dateDebut < :fin AND r.dateFin > :debut" +
            ")")
    List<Borne> findAvailableBornesByDateRange(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    // Ajoutez cette requête pour le cas "ville + dates"
    @Query("SELECT b FROM Borne b LEFT JOIN b.lieux l WHERE " +
            "LOWER(l.ville) LIKE :villePattern AND " +
            "NOT EXISTS (" +
            "  SELECT 1 FROM Reservation r WHERE r.borne = b " +
            "  AND (r.dateDebut < :fin AND r.dateFin > :debut)" +
            ")")
    List<Borne> findBornesWithVilleAndDateRange(@Param("villePattern") String villePattern,
                                                @Param("debut") LocalDateTime debut,
                                                @Param("fin") LocalDateTime fin);
}