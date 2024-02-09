package org.example.utils;

import org.example.model.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FlightStatisticsAnalyzer {
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

    private static Double getMedian(List<Double> values) {
        if (values.isEmpty()) {
            return 0.0;
        }

        Collections.sort(values);
        int middle = values.size() / 2;
        return values.size() % 2 == 1 ? values.get(middle) : Double.valueOf((values.get(middle - 1) + values.get(middle)) / 2.0);
    }
}
