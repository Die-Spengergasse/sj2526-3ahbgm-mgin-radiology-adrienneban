package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    // Prüft ob das Gerät im Zeitraum bereits belegt ist (außer eigene ID beim Bearbeiten)
    @Query("SELECT r FROM Reservation r WHERE r.device.id = :deviceId " +
            "AND r.id <> :excludeId " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Reservation> findOverlappingByDevice(
            @Param("deviceId") int deviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") int excludeId);

    // Prüft, ob der Patient im Zeitraum bereits einen Termin hat (außer eigene ID beim Bearbeiten)
    @Query("SELECT r FROM Reservation r WHERE r.patient.id = :patientId " +
            "AND r.id <> :excludeId " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Reservation> findOverlappingByPatient(
            @Param("patientId") int patientId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") int excludeId);
}
