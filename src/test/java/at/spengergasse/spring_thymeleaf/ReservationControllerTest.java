package at.spengergasse.spring_thymeleaf;

import at.spengergasse.spring_thymeleaf.controllers.ReservationController;
import at.spengergasse.spring_thymeleaf.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web-layer slice tests for ReservationController.
 */
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ReservationRepository   resRepo;
    @MockitoBean private PatientRepository       patientRepository;
    @MockitoBean private DeviceRepository        deviceRepository;

    // GET /reservation/list

    @Test
    void getReservationList_returnsReslistView() throws Exception {
        when(resRepo.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/reservation/list"))
               .andExpect(status().isOk())
               .andExpect(view().name("reslist"))
               .andExpect(model().attributeExists("reservations"));
    }

    @Test
    void getReservationList_modelContainsReservations() throws Exception {
        Device  d = new Device("Room 1", "MRI");
        Patient p = new Patient('F', "Schmidt", "0987654321",
                java.time.LocalDate.of(1985, 3, 15), "Anna");

        Reservation r = new Reservation();
        r.setStartTime(LocalDateTime.now().plusDays(2));
        r.setEndTime(  LocalDateTime.now().plusDays(2).plusMinutes(60));
        r.setBodyRegion("Head");
        r.setDevice(d);
        r.setPatient(p);

        when(resRepo.findAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/reservation/list"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("reservations", List.of(r)));
    }

    // GET /reservation/add

    @Test
    void getAddReservation_returnsFormWithPatientsAndDevices() throws Exception {
        when(patientRepository.findAll()).thenReturn(List.of());
        when(deviceRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/reservation/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("add_reservation"))
               .andExpect(model().attributeExists("reservation"))
               .andExpect(model().attributeExists("patients"))
               .andExpect(model().attributeExists("devices"));
    }
}
