package org.example.suggestedlocation.service;

import jakarta.persistence.criteria.Predicate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.suggestedlocation.controller.LocationController;
import org.example.suggestedlocation.entity.Location;
import org.example.suggestedlocation.model.LocationResponse;
import org.example.suggestedlocation.model.SuggestedLocationReq;
import org.example.suggestedlocation.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ValidationService validationService;

    private final double EARTH_RADIUS_KM = 6371;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationResponse> getSuggestedLocationByName(SuggestedLocationReq request){
        validationService.validate(request);
        Specification<Location> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName()) && !request.getName().isEmpty()){
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        List<Location> locations = locationRepository.findAll(specification);

        List<LocationResponse> responses = new ArrayList<>();

        if (request.getLatitude() != null && request.getLongitude() != null) {
            locations.forEach(location -> {
                double score = calculateProximityScore(request.getLatitude(), request.getLongitude(),
                        location.getLatitude(), location.getLongitude());

                LocationResponse response = convertToLocationResponse(location);
                response.setScore(score);
                responses.add(response);
            });

            responses.sort((response1, response2) -> Double.compare(response2.getScore(), response1.getScore()));
        } else {
            for (Location location : locations) {
                LocationResponse response = convertToLocationResponse(location);
                responses.add(response);
            }
        }

        return responses;
    }

    private double calculateProximityScore(double requestLat, double requestLng, double locationLat, double locationLng) {
        double distance = haversine(requestLat, requestLng, locationLat, locationLng);
        double maxDistance = 10000;
        if (distance > maxDistance) {
            return 0;
        }
        System.out.println("Score : " + (1 - (distance / maxDistance)) );
        return 1 - (distance / maxDistance);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        System.out.println(EARTH_RADIUS_KM * c);
        return EARTH_RADIUS_KM * c;
    }

    private LocationResponse convertToLocationResponse(Location location) {
        return LocationResponse.builder()
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}
