package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final PatientRepository patientRepository;
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/list")
    public String patients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "patlist";
    }

    @GetMapping("/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "add_patient";
    }

    @PostMapping("/add")
    public String addPatient(@Valid @ModelAttribute("patient") Patient patient, BindingResult result) {

        if (result.hasErrors()) {
            return "add_patient";
        }

        patientRepository.save(patient);
        return "redirect:/patient/list";
    }

    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable int id, Model model) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        model.addAttribute("patient", patientRepository.findById(id).orElseThrow());
        return "edit_patient";
    }

    @PostMapping("/edit/{id}")
    public String editPatient(@PathVariable int id, @Valid @ModelAttribute("patient") Patient patient, BindingResult result) {

        if(result.hasErrors()){
            patient.setId(id);
            return "edit_patient";
        }

        patient.setId(id);
        patientRepository.save(patient);
        return "redirect:/patient/list";
    }

    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable int  id) {
        patientRepository.deleteById(id);
        return "redirect:/patient/list";
    }

}
