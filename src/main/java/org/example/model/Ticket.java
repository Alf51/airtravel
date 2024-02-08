package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Ticket {
    private String origin;
    private String origin_name;
    private String destination;

    @JsonProperty("destination_name")
    private String destinationName;

    @JsonFormat(pattern = "dd.MM.yy")
    @JsonProperty("departure_date")
    private LocalDate departureDate;

    @JsonFormat(pattern = "H:mm")
    @JsonProperty("departure_time")
    private LocalTime departureTime;

    @JsonFormat(pattern = "dd.MM.yy")
    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;

    @JsonFormat(pattern = "H:mm")
    @JsonProperty("arrival_time")
    private LocalTime arrivalTime;

    private String carrier;
    private int stops;
    private Double price;
}
