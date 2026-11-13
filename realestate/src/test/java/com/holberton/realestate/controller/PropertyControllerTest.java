package com.holberton.realestate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.dto.PropertyResponse;
import com.holberton.realestate.exception.PropertyNotFoundException;
import com.holberton.realestate.model.Address;
import com.holberton.realestate.model.EnergyClass;
import com.holberton.realestate.service.PropertyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyService service;

    private final Address address = new Address("1 rue de Rivoli", "75001", "Paris");

    @Test
    @DisplayName("GET /api/properties/{reference} answers 200 with the property as JSON")
    void getAnswers200WithJson() throws Exception {
        PropertyResponse response = new PropertyResponse(
                "P-001", address, 25, 950, EnergyClass.D, 11400, null, false);
        when(service.findByReference("P-001")).thenReturn(response);

        mockMvc.perform(get("/api/properties/P-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value("P-001"))
                .andExpect(jsonPath("$.address.city").value("Paris"))
                .andExpect(jsonPath("$.annualRent").value(11400))
                .andExpect(jsonPath("$.energyClass").value("D"));
    }

    @Test
    @DisplayName("GET /api/properties/{reference} answers 404 for an unknown reference")
    void getAnswers404WhenUnknown() throws Exception {
        when(service.findByReference("P-999")).thenThrow(new PropertyNotFoundException("P-999"));

        mockMvc.perform(get("/api/properties/P-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("an invalid POST answers 400 without calling the service")
    void invalidPostAnswers400WithoutCallingService() throws Exception {
        String invalidBody = """
                {"reference":"bad",
                 "address":{"street":"","zipCode":"1","city":""},
                 "surface":0,
                 "monthlyRent":-1,
                 "energyClass":"D"}
                """;

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.reference").exists());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("a valid POST answers 201 with the Location header")
    void validPostAnswers201WithLocation() throws Exception {
        PropertyResponse response = new PropertyResponse(
                "P-001", address, 25, 950, EnergyClass.D, 11400, null, false);
        when(service.create(any(PropertyRequest.class))).thenReturn(response);
        String validBody = """
                {"reference":"P-001",
                 "address":{"street":"1 rue de Rivoli","zipCode":"75001","city":"Paris"},
                 "surface":25,
                 "monthlyRent":950,
                 "energyClass":"D"}
                """;

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/properties/P-001"));
    }
}
