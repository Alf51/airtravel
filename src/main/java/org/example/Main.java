package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Ticket;
import org.example.model.TicketList;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.utils.FlightStatisticsAnalyzer.getDifferencePriceBetweenAverageAndMedian;
import static org.example.utils.FlightStatisticsAnalyzer.getMinFlightTimeBetweenCitizen;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String origin = "VVO";
        String destination = "TLV";
        String fileName = "tickets.json";

        try {
            List<Ticket> ticketList = mapper.readValue(new File(fileName), TicketList.class).getTickets().stream()
                    .filter(ticket -> (ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                            || (ticket.getOrigin().equals(destination) && ticket.getDestination().equals(origin))).toList();

            Map<String, Duration> minFlightTime = getMinFlightTimeBetweenCitizen(ticketList);

            //Выводим минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
            minFlightTime.forEach((carrier, duration) -> System.out.println("Carrier: " + carrier + ", Duration: " + duration.toMinutes() + " minute"));

            //Считаем разницу между средней ценой и медианой для полета между городами  Владивосток и Тель-Авив
            List<Double> priceList = ticketList.stream().map(Ticket::getPrice).collect(Collectors.toCollection(ArrayList::new));
            System.out.println("Разницу между средней ценой  и медианой:  " + getDifferencePriceBetweenAverageAndMedian(priceList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}