package at.spengergasse.spring_thymeleaf;

import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Reservation entity.
 */
class ReservationTest {

    // getDurationMinutes

    @Test
    void getDurationMinutes_returnsCorrectDuration() {
        Reservation r = new Reservation();
        r.setStartTime(LocalDateTime.of(2030, 6, 1, 9, 0));
        r.setEndTime(  LocalDateTime.of(2030, 6, 1, 9, 45));

        assertEquals(45, r.getDurationMinutes());
    }

    @Test
    void getDurationMinutes_returnsZeroWhenStartTimeIsNull() {
        Reservation r = new Reservation();
        r.setEndTime(LocalDateTime.of(2030, 6, 1, 9, 45));

        assertEquals(0, r.getDurationMinutes());
    }

    @Test
    void getDurationMinutes_returnsZeroWhenEndTimeIsNull() {
        Reservation r = new Reservation();
        r.setStartTime(LocalDateTime.of(2030, 6, 1, 9, 0));

        assertEquals(0, r.getDurationMinutes());
    }

    @Test
    void getDurationMinutes_returnsZeroWhenBothTimesNull() {
        Reservation r = new Reservation();

        assertEquals(0, r.getDurationMinutes());
    }

    @Test
    void getDurationMinutes_worksForMultiHourExam() {
        Reservation r = new Reservation();
        r.setStartTime(LocalDateTime.of(2030, 6, 1,  8, 0));
        r.setEndTime(  LocalDateTime.of(2030, 6, 1, 10, 30));

        assertEquals(150, r.getDurationMinutes());
    }

    // Device.displayName

    @Test
    void deviceDisplayName_combinesTypeAndLocation() {
        Device d = new Device("Radiology Room 1", "MRI");
        assertEquals("MRI (Radiology Room 1)", d.displayName());
    }

    // Device.add – beziehung

    @Test
    void deviceAdd_setsDeviceOnReservation() {
        Device d = new Device("Room A", "CT");
        Reservation r = new Reservation();

        d.add(r);

        assertSame(d, r.getDevice(),
                "Reservation should reference the device it was added to");
        assertTrue(d.getReservations().contains(r),
                "Device's reservation list should contain the added reservation");
    }

    @Test
    void deviceAdd_multipleReservationsAreAllStored() {
        Device d = new Device("Room B", "X-Ray");
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();

        d.add(r1);
        d.add(r2);

        assertEquals(2, d.getReservations().size());
    }

    // Patient.add – Reservation

    @Test
    void patientAdd_setsPatientOnReservation() {
        Patient p = new Patient('M', "Mustermann", "1234567890",
                java.time.LocalDate.of(1990, 1, 1), "Max");
        Reservation r = new Reservation();

        p.add(r);

        assertSame(p, r.getPatient());
        assertTrue(p.getReservations().contains(r));
    }

    // Reservation setter / getter

    @Test
    void reservationBodyRegion_setAndGet() {
        Reservation r = new Reservation();
        r.setBodyRegion("Chest");
        assertEquals("Chest", r.getBodyRegion());
    }

    @Test
    void reservationComment_setAndGet() {
        Reservation r = new Reservation();
        r.setComment("Urgent");
        assertEquals("Urgent", r.getComment());
    }
}
