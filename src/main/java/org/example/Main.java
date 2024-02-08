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
        try {
            List<Ticket> ticketList = mapper.readValue(new File("tickets.json"), TicketList.class).getTickets();

            Map<String, Duration> minFlightTimeBetweenCitizen;
            minFlightTimeBetweenCitizen = getMinFlightTimeBetweenCitizen(ticketList, "VVO", "TLV");

            //Выводим минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
            minFlightTimeBetweenCitizen.forEach((carrier, duration) -> System.out.println("Carrier: " + carrier + ", Duration: " + duration.toMinutes() + " minute"));

            List<Double> priceList = ticketList.stream().map(Ticket::getPrice).collect(Collectors.toCollection(ArrayList::new));
            System.out.println("Разницу между средней ценой  и медианой:  " + getDifferencePriceBetweenAverageAndMedian(priceList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}