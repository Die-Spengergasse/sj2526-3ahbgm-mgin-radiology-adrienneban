package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "p_patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private int id;

    private String name;
    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate birth;

    @Pattern(regexp = "\\d{10}", message = "SSN must be 10 digits")
    private String ssn;
    private String surname;
    private char gender;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    public Patient() {
    }

    public Patient(char gender, String surname, String ssn, LocalDate birth, String name) {
        this.gender = gender;
        this.surname = surname;
        this.ssn = ssn;
        this.birth = birth;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", birth=" + birth +
                ", ssn=" + ssn +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", reservations=" + reservations +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void add(Reservation reservation) {
        reservations.add(reservation);
        reservation.setPatient(this);
    }
}
