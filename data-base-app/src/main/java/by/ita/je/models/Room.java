package by.ita.je.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROOM")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "roomNumber")
    private Integer roomNumber;
    @Column(name = "type")
    private String type;
    @Column(name = "pricePerNight")
    private Integer pricePerNight;
    @Column(name = "isAvailable")
    private Boolean isAvailable;

    @ManyToOne(cascade = CascadeType.ALL)
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
