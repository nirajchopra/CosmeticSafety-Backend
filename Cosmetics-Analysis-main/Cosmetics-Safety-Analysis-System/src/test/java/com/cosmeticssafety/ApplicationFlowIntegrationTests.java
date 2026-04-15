package com.cosmeticssafety;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cosmeticssafety.repository.BrandRepository;
import com.cosmeticssafety.repository.IngredientRepository;
import com.cosmeticssafety.repository.ProductRepository;
import com.cosmeticssafety.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationFlowIntegrationTests {

	private static final String API_CONTEXT_PATH = "/api";
	private static final String LOCALHOST_ORIGIN = "http://localhost:4200";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void registerShouldPersistConsumerAndReturnJwt() throws Exception {
		String email = "integration.user." + System.nanoTime() + "@example.com";
		Map<String, Object> payload = new HashMap<>();
		payload.put("fullName", "Integration User");
		payload.put("email", email);
		payload.put("password", "Password@123");

		mockMvc.perform(post("/api/v1/auth/register")
				.contextPath(API_CONTEXT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andExpect(jsonPath("$.tokenType").value("Bearer"))
				.andExpect(jsonPath("$.roles", hasItem("CONSUMER")));

		userRepository.findByEmailIgnoreCase(email).ifPresentOrElse(user -> {
			Assertions.assertEquals("Integration User", user.getFullName());
			Assertions.assertTrue(passwordEncoder.matches("Password@123", user.getPassword()));
			Assertions.assertTrue(user.getRoles().stream()
					.anyMatch(role -> "CONSUMER".equals(role.getName().name())));
		}, () -> Assertions.fail("User was not persisted in the database"));
	}

	@Test
	void loginShouldReturnJwtForSeededAdmin() throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", "admin@cosmeticssafety.com");
		payload.put("password", "Password@123");

		mockMvc.perform(post("/api/v1/auth/login")
				.contextPath(API_CONTEXT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andExpect(jsonPath("$.roles", hasItem("ADMIN")));
	}

	@Test
	void corsPreflightShouldAllowAngularOriginForLogin() throws Exception {
		mockMvc.perform(options("/api/v1/auth/login")
				.contextPath(API_CONTEXT_PATH)
				.header(HttpHeaders.ORIGIN, LOCALHOST_ORIGIN)
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "content-type,authorization"))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, LOCALHOST_ORIGIN));
	}

	@Test
	void adminShouldCreateAndFetchBrandIngredientAndProduct() throws Exception {
		String token = loginAndExtractToken("admin@cosmeticssafety.com", "Password@123");

		Map<String, Object> brandPayload = new HashMap<>();
		brandPayload.put("name", "Integration Brand");
		brandPayload.put("description", "Brand created during integration testing");
		brandPayload.put("active", true);

		MvcResult brandResult = mockMvc.perform(post("/api/v1/brands")
				.contextPath(API_CONTEXT_PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(brandPayload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andReturn();

		Long brandId = readId(brandResult);

		Map<String, Object> ingredientPayload = new HashMap<>();
		ingredientPayload.put("name", "Integration Ingredient");
		ingredientPayload.put("scientificName", "Integration Ingredient Scientific");
		ingredientPayload.put("riskLevel", "LOW");
		ingredientPayload.put("allowedInCosmetics", true);
		ingredientPayload.put("sideEffects", "None");
		ingredientPayload.put("references", "Integration test reference");

		mockMvc.perform(post("/api/v1/ingredients")
				.contextPath(API_CONTEXT_PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ingredientPayload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Integration Ingredient"));

		Map<String, Object> productPayload = new HashMap<>();
		productPayload.put("name", "Integration Product");
		productPayload.put("productType", "Serum");
		productPayload.put("skuCode", "INT-001");
		productPayload.put("description", "Product created during integration testing");
		productPayload.put("active", true);
		productPayload.put("brandId", brandId);

		mockMvc.perform(post("/api/v1/products")
				.contextPath(API_CONTEXT_PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productPayload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Integration Product"))
				.andExpect(jsonPath("$.brandId").value(brandId));

		mockMvc.perform(get("/api/v1/products")
				.contextPath(API_CONTEXT_PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Integration Product"));

		Assertions.assertTrue(brandRepository.findByNameIgnoreCase("Integration Brand").isPresent());
		Assertions.assertTrue(ingredientRepository.findByNameIgnoreCase("Integration Ingredient").isPresent());
		Assertions.assertTrue(productRepository.findAll().stream()
				.anyMatch(product -> "Integration Product".equals(product.getName())));
	}

	private String loginAndExtractToken(String email, String password) throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", email);
		payload.put("password", password);

		MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
				.contextPath(API_CONTEXT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload)))
				.andExpect(status().isOk())
				.andReturn();

		JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
		return response.get("accessToken").asText();
	}

	private Long readId(MvcResult result) throws Exception {
		JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
		return response.get("id").asLong();
	}
}
