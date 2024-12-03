package by.ita.je.models;

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
public class Hotel {

    private UUID id;

    private String name;

    private String location;

    private String description;

    private String periodOfWork;

    private List<Room> rooms;

    private Amenities amenities;

    private Social social;

    private List<Profile> profiles;
}
