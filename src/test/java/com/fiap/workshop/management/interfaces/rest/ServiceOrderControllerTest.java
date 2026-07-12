package com.fiap.workshop.management.interfaces.rest;

import com.fiap.workshop.management.application.port.in.serviceorder.AddItemToServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.ApproveEstimateInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.CompleteDiagnosisInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.CreateServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.DeliverServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.FindServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.FinishServiceItemExecutionInputPort;
import com.fiap.workshop.management.application.dto.customer.CreateCustomerCommand;
import com.fiap.workshop.management.application.dto.serviceorder.OpenFullServiceOrderCommand;
import com.fiap.workshop.management.application.dto.serviceorder.ServiceOrderResponse;
import com.fiap.workshop.management.application.dto.serviceorder.VehicleIntakeCommand;
import com.fiap.workshop.management.application.port.in.serviceorder.GetAverageExecutionTimeInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.OpenFullServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.RejectEstimateInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.RemoveItemFromServiceOrderInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.StartDiagnosisInputPort;
import com.fiap.workshop.management.application.port.in.serviceorder.StartServiceItemExecutionInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServiceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateServiceOrderInputPort createUseCase;
    @MockBean
    private FindServiceOrderInputPort findUseCase;
    @MockBean
    private StartDiagnosisInputPort startDiagnosisUseCase;
    @MockBean
    private AddItemToServiceOrderInputPort addItemUseCase;
    @MockBean
    private RemoveItemFromServiceOrderInputPort removeItemUseCase;
    @MockBean
    private CompleteDiagnosisInputPort completeDiagnosisUseCase;
    @MockBean
    private ApproveEstimateInputPort approveEstimateUseCase;
    @MockBean
    private RejectEstimateInputPort rejectEstimateUseCase;
    @MockBean
    private StartServiceItemExecutionInputPort startItemUseCase;
    @MockBean
    private FinishServiceItemExecutionInputPort finishItemUseCase;
    @MockBean
    private DeliverServiceOrderInputPort deliverUseCase;
    @MockBean
    private GetAverageExecutionTimeInputPort avgExecutionTimeUseCase;
    @MockBean
    private OpenFullServiceOrderInputPort openFullUseCase;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldListExcludingCompletedByDefault() throws Exception {
        when(findUseCase.findAll(false)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/service-orders"))
                .andExpect(status().isOk());

        verify(findUseCase).findAll(false);
    }

    @Test
    void shouldListIncludingCompletedWhenRequested() throws Exception {
        when(findUseCase.findAll(true)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/service-orders").param("includeCompleted", "true"))
                .andExpect(status().isOk());

        verify(findUseCase).findAll(true);
    }

    @Test
    void shouldOpenFullServiceOrder() throws Exception {
        ServiceOrderResponse response = new ServiceOrderResponse(UUID.randomUUID(), "OS-2026-00001", "RECEIVED",
                UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ZERO, null, null, List.of(),
                null, null, null, null, null, null, null, null);
        when(openFullUseCase.execute(any())).thenReturn(response);

        OpenFullServiceOrderCommand command = new OpenFullServiceOrderCommand(
                new CreateCustomerCommand("Carlos Lima", "52998224725", "11977776666", "carlos@test.com"),
                new VehicleIntakeCommand("QWE4455", "Fiat", "Uno", 2018, "White"),
                "Retrovisor quebrado",
                null);

        mockMvc.perform(post("/api/v1/service-orders/full")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());
    }
}
