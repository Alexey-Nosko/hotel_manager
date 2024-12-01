package by.ita.je.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmenitiesDto {

    private UUID id;

    private Boolean wifi;

    private Boolean pool;

    private Boolean airConditioner;

    private  Boolean parking;
}