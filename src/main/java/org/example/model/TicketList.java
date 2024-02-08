package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TicketList {
    @JsonProperty("tickets")
    private List<Ticket> tickets = new ArrayList<>();
}
