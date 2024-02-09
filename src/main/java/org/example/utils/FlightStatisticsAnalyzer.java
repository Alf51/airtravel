package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Ticket;
import org.example.model.TicketList;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FlightStatisticsAnalyzer {
    public static List<Ticket> getAllTicketsFromFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Ticket> ticketList = new ArrayList<>();
        try {
            ticketList = mapper.readValue(new File(fileName), TicketList.class).getTickets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ticketList;
    }

    public static Double getDifferencePriceBetweenAverageAndMedian(List<Double> priceList) {
        Double averagePrice = priceList.stream().mapToDouble(price -> price).average().orElse(0);
        Double medianPrice = getMedian(priceList);
        return Math.abs(averagePrice - medianPrice);
    }

    public static Map<String, Duration> getMinFlightTimeBetweenCitizen(List<Ticket> ticketList) {
        Map<String, Duration> minFlightTimeBetweenCitizen = new HashMap<>();
        for (Ticket ticket : ticketList) {
            LocalDateTime departureDateTime = LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime());
            LocalDateTime arrivalDateTime = LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime());
            Duration duration = Duration.between(departureDateTime, arrivalDateTime);
            String carrier = ticket.getCarrier();

            minFlightTimeBetweenCitizen.computeIfPresent(carrier, (key, currentDuration) -> duration.compareTo(currentDuration) < 0 ? duration : currentDuration);
            minFlightTimeBetweenCitizen.computeIfAbsent(carrier, key -> duration);
        }
        return minFlightTimeBetweenCitizen;
    }

    public static List<Ticket> getTicketsBetweenCities(List<Ticket> ticketList, String origin, String destination) {
        return ticketList.stream()
                .filter(ticket -> (ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                        || (ticket.getOrigin().equals(destination) && ticket.getDestination().equals(origin)))
                .toList();
    }

    private static Double getMedian(List<Double> values) {
        if (values.isEmpty()) {
            return 0.0;
        }

        Collections.sort(values);
        int middle = values.size() / 2;
        return values.size() % 2 == 1 ? values.get(middle) : Double.valueOf((values.get(middle - 1) + values.get(middle)) / 2.0);
    }
}
