package org.example.suggestedlocation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.suggestedlocation.model.LocationResponse;
import org.example.suggestedlocation.model.WebResponse;
import org.example.suggestedlocation.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSuggestedLocationOnlyNameSucces() throws Exception {
        String name = "Amh";

        mockMvc.perform(
                get("/suggestion")
                        .queryParam("name", name)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<LocationResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertNotNull(response.getData(), "Response data should not be null");
            assertTrue(response.getData().size() > 0, "Expected at least one location in the response");

            LocationResponse locationResponse = response.getData().get(0);
            assertTrue(locationResponse.getName().contains(name), "Location name should contain the expected name");
        });
    }

    @Test
    void testSuggestedLocationNotFound() throws Exception {
        String name = "Location Not Found";

        mockMvc.perform(
                get("/suggestion")
                        .queryParam("name", name)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testSuggestedLocationWithCoordinates() throws Exception {
        String name = "A";
        double latitude = 4783345;
        double longitude = -6919874;

        mockMvc.perform(
                        get("/suggestion")
                                .queryParam("name", name)
                                .queryParam("latitude", String.valueOf(latitude))
                                .queryParam("longitude", String.valueOf(longitude))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].score").exists());
    }

    @Test
    void testSuggestedLocationWithCoordinateNotFound() throws Exception {
        String name = "NonExistingLocation";
        double latitude = 4783345;
        double longitude = -6919874;

        mockMvc.perform(
                        get("/suggestion")
                                .queryParam("name", name)
                                .queryParam("latitude", String.valueOf(latitude))
                                .queryParam("longitude", String.valueOf(longitude))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


}