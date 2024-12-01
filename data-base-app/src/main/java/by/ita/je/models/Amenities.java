package by.ita.je.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AMENITIES")
public class Amenities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "wifi")
    private Boolean wifi;
    @Column(name = "pool")
    private Boolean pool;
    @Column(name = "air_conditioner")
    private Boolean airConditioner;
    @Column(name = "parking")
    private  Boolean parking;

    @OneToOne(cascade = CascadeType.ALL)
    private Hotel hotel;
}
