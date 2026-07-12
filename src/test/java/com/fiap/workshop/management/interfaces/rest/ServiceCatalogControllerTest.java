package com.fiap.workshop.management.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.workshop.management.application.dto.catalog.ServiceCatalogItemResponse;
import com.fiap.workshop.management.application.dto.catalog.UpsertServiceCatalogItemCommand;
import com.fiap.workshop.management.application.port.in.catalog.CreateServiceCatalogItemInputPort;
import com.fiap.workshop.management.application.port.in.catalog.DeleteServiceCatalogItemInputPort;
import com.fiap.workshop.management.application.port.in.catalog.FindServiceCatalogItemInputPort;
import com.fiap.workshop.management.application.port.in.catalog.UpdateServiceCatalogItemInputPort;
import com.fiap.workshop.management.domain.model.catalog.ServiceType;
import com.fiap.workshop.management.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

@WebMvcTest(ServiceCatalogController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServiceCatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateServiceCatalogItemInputPort createUseCase;

    @MockBean
    private FindServiceCatalogItemInputPort findUseCase;

    @MockBean
    private UpdateServiceCatalogItemInputPort updateUseCase;

    @MockBean
    private DeleteServiceCatalogItemInputPort deleteUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UUID id = UUID.randomUUID();

    private ServiceCatalogItemResponse sampleResponse() {
        return new ServiceCatalogItemResponse(id, "Oil Change", "desc", "MECHANICAL", BigDecimal.valueOf(150), true);
    }

    @Test
    void shouldCreateServiceCatalogItem() throws Exception {
        UpsertServiceCatalogItemCommand command =
                new UpsertServiceCatalogItemCommand("Oil Change", "desc", ServiceType.MECHANICAL, BigDecimal.valueOf(150), true);
        when(createUseCase.execute(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Oil Change"));
    }

    @Test
    void shouldFindActiveItemsByDefault() throws Exception {
        when(findUseCase.findAllActive()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    void shouldFindAllItemsIncludingInactive() throws Exception {
        when(findUseCase.findAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/services").param("includeInactive", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindItemById() throws Exception {
        when(findUseCase.findById(id)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/services/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateItem() throws Exception {
        UpsertServiceCatalogItemCommand command =
                new UpsertServiceCatalogItemCommand("Oil Change", "desc", ServiceType.MECHANICAL, BigDecimal.valueOf(150), true);
        when(updateUseCase.execute(eq(id), any())).thenReturn(sampleResponse());

        mockMvc.perform(put("/api/v1/services/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteItem() throws Exception {
        mockMvc.perform(delete("/api/v1/services/{id}", id))
                .andExpect(status().isNoContent());
    }
}
