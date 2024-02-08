package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String destination_name;

    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate departure_date;

    @JsonFormat(pattern = "H:mm")
    private LocalTime departure_time;

    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate arrival_date;

    @JsonFormat(pattern = "H:mm")
    private LocalTime arrival_time;

    private String carrier;
    private int stops;
    private Double price;
}
