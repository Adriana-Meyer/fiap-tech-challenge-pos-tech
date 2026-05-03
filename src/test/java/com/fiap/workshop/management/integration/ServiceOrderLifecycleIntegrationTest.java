package com.fiap.workshop.management.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.workshop.management.application.dto.auth.AuthRequest;
import com.fiap.workshop.management.infrastructure.persistence.repository.UserJpaRepository;
import com.fiap.workshop.management.infrastructure.security.UserJpaEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Service Order Lifecycle Integration")
class ServiceOrderLifecycleIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;
    private String mechanicToken;
    private UUID customerId;
    private UUID vehicleId;
    private UUID catalogItemId;
    private UUID supplyId;

    @BeforeAll
    void setUpReferenceData() throws Exception {
        userJpaRepository.save(new UserJpaEntity(
                UUID.randomUUID().toString(),
                "lifecycle-admin@test.com",
                passwordEncoder.encode("pass123"),
                "ROLE_ADMIN",
                true));
        userJpaRepository.save(new UserJpaEntity(
                UUID.randomUUID().toString(),
                "lifecycle-mechanic@test.com",
                passwordEncoder.encode("pass123"),
                "ROLE_MECHANIC",
                true));

        adminToken = loginAndGetToken("lifecycle-admin@test.com", "pass123");
        mechanicToken = loginAndGetToken("lifecycle-mechanic@test.com", "pass123");

        customerId = createCustomer();
        vehicleId = createVehicle(customerId);
        catalogItemId = createCatalogItem();
        supplyId = createSupply();
    }

    @BeforeEach
    void cleanServiceOrders() {
        jdbcTemplate.execute("DELETE FROM service_order_items");
        jdbcTemplate.execute("DELETE FROM service_orders");
    }

    @Test
    @DisplayName("should complete full service order lifecycle from creation to delivery")
    void shouldCompleteFullServiceOrderLifecycleFromCreationToDelivery() throws Exception {
        UUID orderId = createServiceOrder();

        performPatch("/api/v1/service-orders/" + orderId + "/diagnosis/start", mechanicToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_DIAGNOSIS"));

        performPost("/api/v1/service-orders/" + orderId + "/items",
                """
                {"serviceCatalogItemId":"%s","quantity":1}
                """.formatted(catalogItemId), mechanicToken)
                .andExpect(status().isOk());

        String afterAddSupply = performPost("/api/v1/service-orders/" + orderId + "/items",
                """
                {"supplyId":"%s","quantity":2}
                """.formatted(supplyId), mechanicToken)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var items = objectMapper.readTree(afterAddSupply).get("items");
        UUID item1Id = UUID.fromString(items.get(0).get("id").asText());
        UUID item2Id = UUID.fromString(items.get(1).get("id").asText());

        performPost("/api/v1/service-orders/" + orderId + "/diagnosis/complete",
                """
                {"diagnosisNotes":"Engine oil and filter replacement required"}
                """, mechanicToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAITING_APPROVAL"))
                .andExpect(jsonPath("$.totalAmount").isNumber());

        performPatch("/api/v1/service-orders/" + orderId + "/estimate/approve", adminToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_EXECUTION"));

        performPatch("/api/v1/service-orders/" + orderId + "/items/" + item1Id + "/start", mechanicToken)
                .andExpect(status().isOk());
        performPatch("/api/v1/service-orders/" + orderId + "/items/" + item2Id + "/start", mechanicToken)
                .andExpect(status().isOk());

        performPatch("/api/v1/service-orders/" + orderId + "/items/" + item1Id + "/finish", mechanicToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_EXECUTION"));

        performPatch("/api/v1/service-orders/" + orderId + "/items/" + item2Id + "/finish", mechanicToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINISHED"));

        performPatch("/api/v1/service-orders/" + orderId + "/deliver", adminToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"))
                .andExpect(jsonPath("$.deliveredAt").isNotEmpty());
    }

    @Test
    @DisplayName("should return order to IN_DIAGNOSIS when estimate is rejected")
    void shouldReturnOrderToInDiagnosisWhenEstimateIsRejected() throws Exception {
        UUID orderId = createServiceOrder();

        performPatch("/api/v1/service-orders/" + orderId + "/diagnosis/start", mechanicToken)
                .andExpect(status().isOk());

        performPost("/api/v1/service-orders/" + orderId + "/items",
                """
                {"serviceCatalogItemId":"%s","quantity":1}
                """.formatted(catalogItemId), mechanicToken)
                .andExpect(status().isOk());

        performPost("/api/v1/service-orders/" + orderId + "/diagnosis/complete",
                """
                {"diagnosisNotes":"Brake pads worn out"}
                """, mechanicToken)
                .andExpect(status().isOk());

        performPatch("/api/v1/service-orders/" + orderId + "/estimate/reject", adminToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_DIAGNOSIS"));
    }

    @Test
    @DisplayName("should list all service orders when multiple orders exist")
    void shouldListAllServiceOrdersWhenMultipleOrdersExist() throws Exception {
        createServiceOrder();
        createServiceOrder();

        mockMvc.perform(get("/api/v1/service-orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("should return 403 when mechanic accesses admin-only analytics endpoint")
    void shouldReturn403WhenMechanicAccessesAdminOnlyAnalyticsEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/service-orders/analytics/avg-execution-time")
                        .header("Authorization", "Bearer " + mechanicToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 400 when completing diagnosis with blank notes")
    void shouldReturn400WhenCompletingDiagnosisWithBlankNotes() throws Exception {
        UUID orderId = createServiceOrder();
        performPatch("/api/v1/service-orders/" + orderId + "/diagnosis/start", mechanicToken);

        performPost("/api/v1/service-orders/" + orderId + "/diagnosis/complete",
                "{\"diagnosisNotes\":\"\"}",
                mechanicToken)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return tracking info without authentication")
    void shouldReturnTrackingInfoWithoutAuthentication() throws Exception {
        UUID orderId = createServiceOrder();

        String orderJson = mockMvc.perform(get("/api/v1/service-orders/" + orderId)
                        .header("Authorization", "Bearer " + adminToken))
                .andReturn().getResponse().getContentAsString();
        String osCode = objectMapper.readTree(orderJson).get("osCode").asText();

        mockMvc.perform(get("/api/v1/tracking/" + osCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.osCode").value(osCode))
                .andExpect(jsonPath("$.status").value("RECEIVED"));
    }

    private UUID createServiceOrder() throws Exception {
        String response = mockMvc.perform(post("/api/v1/service-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customerId":"%s","vehicleId":"%s"}
                                """.formatted(customerId, vehicleId))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    private ResultActions performPatch(String url, String token) throws Exception {
        return mockMvc.perform(patch(url)
                .header("Authorization", "Bearer " + token));
    }

    private ResultActions performPost(String url, String body, String token) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.strip())
                .header("Authorization", "Bearer " + token));
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthRequest(email, password))))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    private UUID createCustomer() throws Exception {
        String response = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"João Silva","documentNumber":"52998224725","phone":"11999990000","email":"joao@test.com"}
                                """.strip())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    private UUID createVehicle(UUID ownerId) throws Exception {
        String response = mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"plate":"ABC1234","brand":"Toyota","model":"Corolla","year":2020,"color":"Silver","customerId":"%s"}
                                """.formatted(ownerId).strip())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    private UUID createCatalogItem() throws Exception {
        String response = mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Oil Change","description":"Engine oil replacement","type":"MECHANICAL","basePrice":150.00}
                                """.strip())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    private UUID createSupply() throws Exception {
        String response = mockMvc.perform(post("/api/v1/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"P001","name":"Oil Filter","description":"Engine oil filter","type":"PART","unitPrice":25.00,"minimumStock":5}
                                """.strip())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }
}
