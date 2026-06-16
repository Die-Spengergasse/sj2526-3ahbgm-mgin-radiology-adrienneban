package at.spengergasse.spring_thymeleaf;

import at.spengergasse.spring_thymeleaf.controllers.DeviceController;
import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web-layer slice tests for DeviceController.
 */
@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceRepository deviceRepository;

    // GET /device/list

    @Test
    void getDeviceList_returnsDevlistView() throws Exception {
        when(deviceRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/device/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("devlist"))
                .andExpect(model().attributeExists("devices"));
    }

    @Test
    void getDeviceList_modelContainsAllDevices() throws Exception {
        Device d1 = new Device("Room 1", "MRI");
        Device d2 = new Device("Room 2", "CT");
        when(deviceRepository.findAll()).thenReturn(List.of(d1, d2));

        mockMvc.perform(get("/device/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("devices", List.of(d1, d2)));
    }

    // GET /device/{id}/reservations

    @Test
    void getDeviceReservations_returnsCorrectViewAndModel() throws Exception {
        Device device = new Device("Room 3", "X-Ray");
        device.setId(1);

        // Patient muss gesetzt sein, da das Template r.patient.name aufruft
        Patient patient = new Patient('F', "Muster", "1234567890",
                LocalDate.of(1990, 1, 1), "Anna");

        Reservation r = new Reservation();
        r.setStartTime(LocalDateTime.now().plusDays(1));
        r.setEndTime(  LocalDateTime.now().plusDays(1).plusHours(1));
        r.setBodyRegion("Chest");
        r.setPatient(patient);
        device.add(r);

        when(deviceRepository.findById(1)).thenReturn(Optional.of(device));

        mockMvc.perform(get("/device/1/reservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("device_reservations"))
                .andExpect(model().attributeExists("device"))
                .andExpect(model().attributeExists("reservations"));
    }

    @Test
    void getDeviceReservations_emptyListWhenNoReservations() throws Exception {
        Device device = new Device("Room 4", "Ultrasound");
        device.setId(2);
        when(deviceRepository.findById(2)).thenReturn(Optional.of(device));

        mockMvc.perform(get("/device/2/reservations"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("reservations", List.of()));
    }


    // GET /device/add

    @Test
    void getAddDevice_returnsFormView() throws Exception {
        mockMvc.perform(get("/device/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_device"))
                .andExpect(model().attributeExists("device"));
    }
}
