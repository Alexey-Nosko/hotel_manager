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
@Table(name = "SOCIAL")
public class Social {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "followers_count")
    private Long followersCount;

    @OneToOne(cascade = CascadeType.ALL)
    private Hotel hotel;

}
