package com.agrotis.challenge.controllers;

import com.agrotis.challenge.dtos.FarmsteadDTO;
import com.agrotis.challenge.dtos.FarmsteadRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.services.FarmsteadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FarmsteadController.class)
@DisplayName("FarmsteadController - Testes da Camada Web")
class FarmsteadControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private FarmsteadService farmsteadService;

	private UUID existingId;
	private UUID nonExistingId;
	private FarmsteadDTO farmsteadDTO;
	private String validJsonPayload;

	@BeforeEach
	void setUp() {
		existingId = UUID.randomUUID();
		nonExistingId = UUID.randomUUID();

		farmsteadDTO = new FarmsteadDTO();
		farmsteadDTO.setId(existingId);
		farmsteadDTO.setName("Fazenda Vista Alegre");
		// CNPJ com dígitos verificadores reais
		farmsteadDTO.setRegistration("00.000.000/0001-91"); 
		farmsteadDTO.setAddress("Estrada Rural, Km 10");
		farmsteadDTO.setGrowerId(UUID.randomUUID());
		farmsteadDTO.setGrowerName("Produtor Silva");
		farmsteadDTO.setTotalAreaInHectares(new BigDecimal("1500.50"));
		farmsteadDTO.setTaxPerHectare(new BigDecimal("10.00"));
		farmsteadDTO.setCalculatedValue(new BigDecimal("15005.00"));

		validJsonPayload = """
				{
					"name": "Fazenda Vista Alegre",
					"registration": "00.000.000/0001-91",
					"address": "Estrada Rural, Km 10",
					"totalAreaInHectares": 1500.50,
					"taxPerHectare": 10.00
				}
				""";
	}

	@Nested
	@DisplayName("GET / - Listagem Paginada")
	class GetAllTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK e PaginatedResponse")
		void shouldReturn200OKAndPaginatedData() throws Exception {
			Page<FarmsteadDTO> farmsteadPage = new PageImpl<>(List.of(farmsteadDTO), PageRequest.of(0, 10), 1L);
			PaginatedResponse<FarmsteadDTO> paginatedResponse = PaginatedResponse.from(farmsteadPage);

			given(farmsteadService.getAll(any(Pageable.class))).willReturn(paginatedResponse);

			mockMvc.perform(get("/api/v1/farmsteads").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(existingId.toString()))
					.andExpect(jsonPath("$.content[0].name").value("Fazenda Vista Alegre"))
					.andExpect(jsonPath("$.content[0].totalAreaInHectares").value(1500.50));
		}
	}

	@Nested
	@DisplayName("GET /{id} - Busca por ID")
	class FindByIdTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK quando encontrar")
		void shouldReturn200OKWhenFound() throws Exception {
			given(farmsteadService.findById(existingId)).willReturn(farmsteadDTO);

			mockMvc.perform(get("/api/v1/farmsteads/{id}", existingId)).andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(existingId.toString()))
					.andExpect(jsonPath("$.name").value("Fazenda Vista Alegre"));
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found quando ID for inexistente")
		void shouldReturn404WhenNotFound() throws Exception {
			given(farmsteadService.findById(nonExistingId))
					.willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

			mockMvc.perform(get("/api/v1/farmsteads/{id}", nonExistingId)).andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("POST / - Cadastro")
	class CreateTests {

		@Test
		@DisplayName("Deve retornar HTTP 201 Created quando DTO for válido")
		void shouldReturn201CreatedWhenValid() throws Exception {
			given(farmsteadService.create(any(FarmsteadRequestDTO.class))).willReturn(farmsteadDTO);

			mockMvc.perform(
					post("/api/v1/farmsteads").contentType(MediaType.APPLICATION_JSON).content(validJsonPayload))
					.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(existingId.toString()));
		}

		@Test
		@DisplayName("Deve retornar HTTP 400 Bad Request se a validação @Valid falhar")
		void shouldReturn400BadRequestWhenInvalid() throws Exception {
			String invalidJson = "{}";

			mockMvc.perform(post("/api/v1/farmsteads").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("PUT /{id} - Atualização")
	class UpdateTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK quando atualização for bem-sucedida")
		void shouldReturn200OKWhenValid() throws Exception {
			given(farmsteadService.update(eq(existingId), any(FarmsteadRequestDTO.class))).willReturn(farmsteadDTO);

			mockMvc.perform(put("/api/v1/farmsteads/{id}", existingId).contentType(MediaType.APPLICATION_JSON)
					.content(validJsonPayload)).andExpect(status().isOk())
					.andExpect(jsonPath("$.name").value("Fazenda Vista Alegre"));
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found para recurso inexistente")
		void shouldReturn404WhenNotFound() throws Exception {
			given(farmsteadService.update(eq(nonExistingId), any(FarmsteadRequestDTO.class)))
					.willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

			mockMvc.perform(put("/api/v1/farmsteads/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON)
					.content(validJsonPayload)).andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("DELETE /{id} - Remoção")
	class DeleteTests {

		@Test
		@DisplayName("Deve retornar HTTP 204 No Content ao excluir existente")
		void shouldReturn204NoContent() throws Exception {
			willDoNothing().given(farmsteadService).delete(existingId);

			mockMvc.perform(delete("/api/v1/farmsteads/{id}", existingId)).andExpect(status().isNoContent());
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found ao tentar excluir inexistente")
		void shouldReturn404NotFound() throws Exception {
			willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(farmsteadService).delete(nonExistingId);

			mockMvc.perform(delete("/api/v1/farmsteads/{id}", nonExistingId)).andExpect(status().isNotFound());
		}
	}
}