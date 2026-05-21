package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="d_devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_id")
    private int id;
    @Column(name = "d_type")
    private String type;
    @Column(name = "d_location")
    private String location;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public Device() {
    }

    public Device(String location, String type) {
        this.location = location;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String displayName() {
        return type + " (" + location + ")";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void add(Reservation reservation) {
        reservations.add(reservation);
        reservation.setDevice(this);
    }
}
