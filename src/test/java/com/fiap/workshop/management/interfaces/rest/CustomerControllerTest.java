package com.fiap.workshop.management.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.workshop.management.application.dto.customer.CreateCustomerCommand;
import com.fiap.workshop.management.application.dto.customer.CustomerResponse;
import com.fiap.workshop.management.application.dto.customer.UpdateCustomerCommand;
import com.fiap.workshop.management.application.dto.vehicle.VehicleResponse;
import com.fiap.workshop.management.application.port.in.customer.CreateCustomerInputPort;
import com.fiap.workshop.management.application.port.in.customer.DeleteCustomerInputPort;
import com.fiap.workshop.management.application.port.in.customer.FindCustomerInputPort;
import com.fiap.workshop.management.application.port.in.customer.UpdateCustomerInputPort;
import com.fiap.workshop.management.application.port.in.vehicle.FindVehicleInputPort;
import com.fiap.workshop.management.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCustomerInputPort createCustomerUseCase;

    @MockBean
    private FindCustomerInputPort findCustomerUseCase;

    @MockBean
    private UpdateCustomerInputPort updateCustomerUseCase;

    @MockBean
    private DeleteCustomerInputPort deleteCustomerUseCase;

    @MockBean
    private FindVehicleInputPort findVehicleUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UUID id = UUID.randomUUID();

    private CustomerResponse sampleResponse() {
        return new CustomerResponse(id, "João Silva", "52998224725", "CPF", "11999999999", "joao@test.com", LocalDateTime.now());
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        CreateCustomerCommand command = new CreateCustomerCommand("João Silva", "52998224725", "11999999999", "joao@test.com");
        when(createCustomerUseCase.execute(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    void shouldFindAllCustomers() throws Exception {
        when(findCustomerUseCase.findAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    void shouldFindCustomerById() throws Exception {
        when(findCustomerUseCase.findById(id)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/customers/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindCustomerByDocument() throws Exception {
        when(findCustomerUseCase.findByDocument("52998224725")).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/customers/document/{document}", "52998224725"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindVehiclesByCustomer() throws Exception {
        VehicleResponse vehicle = new VehicleResponse(UUID.randomUUID(), "ABC1234", "Toyota", "Corolla", 2020, "Black", id);
        when(findVehicleUseCase.findByCustomerId(id)).thenReturn(List.of(vehicle));

        mockMvc.perform(get("/api/v1/customers/{customerId}/vehicles", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(id.toString()));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        UpdateCustomerCommand command = new UpdateCustomerCommand("João Silva", "11999999999", "joao@test.com");
        when(updateCustomerUseCase.execute(eq(id), any())).thenReturn(sampleResponse());

        mockMvc.perform(put("/api/v1/customers/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/{id}", id))
                .andExpect(status().isNoContent());
    }
}
