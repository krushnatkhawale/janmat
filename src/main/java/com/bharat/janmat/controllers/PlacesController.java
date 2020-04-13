package com.bharat.janmat.controllers;

import com.bharat.janmat.model.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RestController
public class PlacesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlacesController.class);

    private List<Place> places;

    public PlacesController() {
        try {
            places = Files.lines(Paths.get("indian_cities.csv"))
                    .skip(1)
                    .map(String::toLowerCase)
                    .map(line -> {
                        String[] split = line.split(",");
                        return new Place(split[0], split[1], split[2]);
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.info("Error while loading cities");
            places = new ArrayList<>(0);
        }
    }

    @GetMapping("/states")
    public List<Place> getPlaces(@RequestParam(value = "q", required = false) String query) throws IOException {
        if (isNull(query)   || query.isEmpty()) {
            return places;
        }
        return places
                .stream()
                .filter(place -> place.getCity().contains(query))
                .collect(Collectors.toList());
    }
}