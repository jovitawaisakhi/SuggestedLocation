package org.example.suggestedlocation.controller;

import org.example.suggestedlocation.model.LocationResponse;
import org.example.suggestedlocation.model.SuggestedLocationReq;
import org.example.suggestedlocation.model.WebResponse;
import org.example.suggestedlocation.service.LocationService;
import org.example.suggestedlocation.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/suggestion")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public WebResponse<List<LocationResponse>> suggestedLocation(
            @RequestParam String name,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude
    ){
        SuggestedLocationReq req = SuggestedLocationReq.builder().name(name).latitude(latitude).longitude(longitude).build();
        List<LocationResponse> locationResponses = locationService.getSuggestedLocationByName(req);

        return WebResponse.<List<LocationResponse>>builder()
                .data(locationResponses)
                .build();
    }
}
