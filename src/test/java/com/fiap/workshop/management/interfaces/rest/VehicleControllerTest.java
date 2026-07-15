package com.fiap.workshop.management.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.workshop.management.application.dto.vehicle.RegisterVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.UpdateVehicleCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.application.port.in.vehicle.DeleteVehicleInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.FindVehicleInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.RegisterVehicleInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.UpdateVehicleInputPort;
import com.fiap.workshop.management.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = false)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterVehicleInputPort registerVehicleUseCase;

    @MockBean
    private FindVehicleInputPort findVehicleUseCase;

    @MockBean
    private UpdateVehicleInputPort updateVehicleUseCase;

    @MockBean
    private DeleteVehicleInputPort deleteVehicleUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UUID id = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();

    private VehicleResponse sampleResponse() {
        return new VehicleResponse(id, "ABC1234", "Toyota", "Corolla", 2020, "Black", customerId);
    }

    @Test
    void shouldRegisterVehicle() throws Exception {
        RegisterVehicleCommand command = new RegisterVehicleCommand("ABC1234", "Toyota", "Corolla", 2020, "Black", customerId);
        when(registerVehicleUseCase.execute(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.plate").value("ABC1234"));
    }

    @Test
    void shouldFindAllVehicles() throws Exception {
        when(findVehicleUseCase.findAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    void shouldFindVehicleById() throws Exception {
        when(findVehicleUseCase.findById(id)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/vehicles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    @Test
    void shouldFindVehicleByPlate() throws Exception {
        when(findVehicleUseCase.findByPlate("ABC1234")).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/vehicles/plate/{plate}", "ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("ABC1234"));
    }

    @Test
    void shouldUpdateVehicle() throws Exception {
        UpdateVehicleCommand command = new UpdateVehicleCommand("Toyota", "Corolla", 2021, "White");
        when(updateVehicleUseCase.execute(eq(id), any())).thenReturn(sampleResponse());

        mockMvc.perform(put("/api/v1/vehicles/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteVehicle() throws Exception {
        mockMvc.perform(delete("/api/v1/vehicles/{id}", id))
                .andExpect(status().isNoContent());
    }
}
