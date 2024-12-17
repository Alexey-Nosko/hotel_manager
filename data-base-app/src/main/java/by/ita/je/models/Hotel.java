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
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "location")
    private String location;
    @Column(name = "description")
    private String description;
    @Column(name = "period_of_work")
    private String periodOfWork;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amenities_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Amenities amenities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Social social;

    @OneToMany(mappedBy = "hotel")
    private List<Profile> profiles;
}
