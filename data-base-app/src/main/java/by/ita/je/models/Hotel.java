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
@Table(name = "HOTEL")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "location")
    private String location;
    @Column(name = "description")
    private String description;
    @Column(name = "periodOfWork")
    private String periodOfWork;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToOne(cascade = CascadeType.ALL)
    private Amenities amenities;

    @OneToOne(cascade = CascadeType.ALL)
    private Manager manager;

    @OneToOne(cascade = CascadeType.ALL)
    private Social social;
}
