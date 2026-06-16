package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/device")
public class DeviceController {
    private final DeviceRepository deviceRepository;
    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("device", new Device());
        return "add_device";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("device") Device device) {
        deviceRepository.save(device);
        return "redirect:/device/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Device device = deviceRepository.findById(id).orElseThrow(null);
        model.addAttribute("device", device);
        return "edit_device";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute Device device) {
        device.setId(id);
        deviceRepository.save(device);
        return "redirect:/device/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        return "devlist";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        deviceRepository.deleteById(id);
        return "redirect:/device/list";
    }

    @GetMapping("/{id}/reservations")
    public String deviceReservations(@PathVariable int id, Model model) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + id));
        model.addAttribute("device", device);
        model.addAttribute("reservations", device.getReservations());
        return "device_reservations";
    }
}
