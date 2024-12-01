package by.ita.je.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROFILE")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "role")
    private String role;
    @Column(name = "name")
    private String name;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = true)
    private Hotel hotel;

    @ElementCollection
    private Set<UUID> favoriteHotels;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.PERSIST)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Notification> notifications;
}
