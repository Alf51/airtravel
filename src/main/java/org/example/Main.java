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

import static org.example.utils.FlightStatisticsAnalyzer.*;

public class Main {
    public static void main(String[] args) {
        String origin = "VVO";
        String destination = "TLV";
        String fileName = "tickets.json";

        List<Ticket> ticketList = getAllTicketsFromFile(fileName);

        //Получаем билеты для рейсов между конкретных городов
        ticketList = getTicketsBetweenCities(ticketList, origin, destination);

        Map<String, Duration> minFlightTime = getMinFlightTimeBetweenCitizen(ticketList);

        //Выводим минимальное время полета для каждого авиаперевозчика
        minFlightTime.forEach((carrier, duration) -> System.out.println("Carrier: " + carrier + ", Duration: " + duration.toMinutes() + " minute"));

        //Считаем разницу между средней ценой и медианой
        List<Double> priceList = ticketList.stream().map(Ticket::getPrice).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Разницу между средней ценой  и медианой:  " + getDifferencePriceBetweenAverageAndMedian(priceList));
    }
}