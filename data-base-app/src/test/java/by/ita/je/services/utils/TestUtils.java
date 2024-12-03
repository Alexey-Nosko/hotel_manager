package by.ita.je.services.utils;

import by.ita.je.models.Hotel;
import java.util.UUID;

public abstract class TestUtils {

    protected static UUID uuid = UUID.randomUUID();

    protected Hotel buildHotel(UUID id, String name, String location, String description, String periodOfWork) {
        return Hotel.builder()
                .id(id)
                .name(name)
                .location(location)
                .description(description)
                .periodOfWork(periodOfWork)
                .build();
    }

}
