package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Ticket;
import org.example.model.TicketList;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private static Double getDifferencePriceBetweenAverageAndMedian(List<Double> priceList) {
        Double averagePrice = priceList.stream().mapToDouble(price -> price).average().orElse(0);
        Double medianPrice = getMedian(priceList);
        return Math.abs(averagePrice - medianPrice);
    }

    private static Map<String, Duration> getMinFlightTimeBetweenCitizen(List<Ticket> ticketList, String origin, String destination) {
        Map<String, Duration> minFlightTimeBetweenCitizen = new HashMap<>();
        for (Ticket ticket : ticketList) {
            boolean isFlightBetweenOriginAndDestination = ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination);
            boolean isFlightBetweenDestinationAndOrigin = ticket.getDestination().equals(origin) && ticket.getOrigin().equals(destination);

            if (isFlightBetweenOriginAndDestination || isFlightBetweenDestinationAndOrigin) {
                LocalDateTime departureDateTime = LocalDateTime.of(ticket.getDeparture_date(), ticket.getDeparture_time());
                LocalDateTime arrivalDateTime = LocalDateTime.of(ticket.getArrival_date(), ticket.getArrival_time());
                Duration duration = Duration.between(departureDateTime, arrivalDateTime);
                String carrier = ticket.getCarrier();

                minFlightTimeBetweenCitizen.computeIfPresent(carrier, (key, currentDuration) -> duration.compareTo(currentDuration) < 0 ? duration : currentDuration);
                minFlightTimeBetweenCitizen.computeIfAbsent(carrier, key -> duration);
            }
        }
        return minFlightTimeBetweenCitizen;
    }

    private static Double getMedian(List<Double> values) {
        Collections.sort(values);
        int middle = values.size() / 2;
        if (values.size() % 2 == 1) {
            return values.get(middle);
        } else {
            return (values.get(middle - 1) + values.get(middle)) / 2.0;
        }
    }
}