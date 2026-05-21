package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "r_reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @FutureOrPresent
    private LocalDateTime startTime;
    @FutureOrPresent
    private LocalDateTime endTime;
    private String bodyRegion;
    private String comment;

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_p_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_d_id")
    private Device device;

    public long getDurationMinutes() {
        if (startTime == null || endTime == null) {
            return 0; // oder -1 oder Exception
        }
        return Duration.between(startTime, endTime).toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getBodyRegion() {
        return bodyRegion;
    }

    public void setBodyRegion(String bodyRegion) {
        this.bodyRegion = bodyRegion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Patient getPatient() {
        return patient;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public Integer getPatientId() {
        return patientId;
    }

    @Transient
    private Integer patientId;

    @Transient
    private Integer deviceId;


    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }*/



}
