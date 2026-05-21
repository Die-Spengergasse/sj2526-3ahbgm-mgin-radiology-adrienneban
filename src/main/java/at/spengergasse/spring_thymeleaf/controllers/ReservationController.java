package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationRepository resRepo;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository resRepo,
                                 PatientRepository patientRepository,
                                 DeviceRepository deviceRepository) {
        this.resRepo = resRepo;
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
    }

    @RequestMapping("/list")
    public String reservations(Model model) {
        model.addAttribute("reservations", resRepo.findAll());
        return "reslist";
    }

    @GetMapping("/add")
    public String addReservation(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "add_reservation";
    }

    @PostMapping("/add")
    public String addReservation(@Valid @ModelAttribute Reservation reservation, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "reservation_error";
        }

        // Überschneidungsprüfung
        String conflict = checkOverlap(reservation, 0);
        if (conflict != null) {
            model.addAttribute("errorMessage", conflict);
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("devices", deviceRepository.findAll());
            return "reservation_error";
        }

        resRepo.save(reservation);
        return "redirect:/reservation/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable int id) {
        resRepo.deleteById(id);
        return "redirect:/reservation/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Reservation reservation = resRepo.findById(id).orElseThrow(null);
        model.addAttribute("reservation", reservation);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "edit_reservation";
    }

    @PostMapping("/edit/{id}")
    public String updateReservation(@PathVariable int id, @ModelAttribute Reservation reservation, BindingResult bindingResult, Model model) {
        reservation.setId(id);

        if (bindingResult.hasErrors()) {
            return "reservation_error";
        }

        // Überschneidungsprüfung – eigene ID ausschließen
        String conflict = checkOverlap(reservation, id);
        if (conflict != null) {
            model.addAttribute("errorMessage", conflict);
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("devices", deviceRepository.findAll());
            return "reservation_error";
        }

        resRepo.save(reservation);
        return "redirect:/reservation/list";
    }

    private String checkOverlap(Reservation reservation, int excludeId) {
        if (reservation.getDevice() == null || reservation.getPatient() == null
                || reservation.getStartTime() == null || reservation.getEndTime() == null) {
            return null; // Validierung übernimmt fehlende Felder
        }

        int deviceId  = reservation.getDevice().getId();
        int patientId = reservation.getPatient().getId();

        List<Reservation> deviceConflicts = resRepo.findOverlappingByDevice(
                deviceId, reservation.getStartTime(), reservation.getEndTime(), excludeId);

        if (!deviceConflicts.isEmpty()) {
            Reservation c = deviceConflicts.get(0);
            return "The modality is already reserved for this time period "
                    + "(Conflict with reservation from "
                    + c.getStartTime() + " to " + c.getEndTime() + ").";
        }

        List<Reservation> patientConflicts = resRepo.findOverlappingByPatient(
                patientId, reservation.getStartTime(), reservation.getEndTime(), excludeId);

        if (!patientConflicts.isEmpty()) {
            Reservation c = patientConflicts.get(0);
            return "This patient already has an appointment during this time period "
                    + "(Conflict with reservation from "
                    + c.getStartTime() + " to " + c.getEndTime() + ").";
        }

        return null;
    }

}
