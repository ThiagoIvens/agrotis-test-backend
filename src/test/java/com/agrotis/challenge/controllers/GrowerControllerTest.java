package com.agrotis.challenge.controllers;

import com.agrotis.challenge.dtos.GrowerDTO;
import com.agrotis.challenge.dtos.GrowerRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.services.GrowerService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GrowerController.class)
@DisplayName("GrowerController - Testes da Camada Web")
class GrowerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GrowerService growerService;

	private UUID existingId;
	private UUID nonExistingId;
	private GrowerDTO growerDTO;
	private String validJsonPayload;

	@BeforeEach
	void setUp() {
		existingId = UUID.randomUUID();
		nonExistingId = UUID.randomUUID();

		growerDTO = new GrowerDTO();
		growerDTO.setId(existingId);
		growerDTO.setName("Produtor Agrícola Silva");
		// CPF com dígitos verificadores reais 
		growerDTO.setRegistration("123.456.789-09"); 
		growerDTO.setAddress("Rodovia BR-116, Km 50");
		growerDTO.setLaboratoryId(UUID.randomUUID());
		growerDTO.setLaboratoryName("AgroLab Central");
		growerDTO.setFarmsteadIds(List.of(UUID.randomUUID()));
		growerDTO.setOperationInitialDate(LocalDate.of(2026, 1, 1));
		growerDTO.setOperationFinalDate(LocalDate.of(2026, 12, 31));
		growerDTO.setObservations("Operação de safra principal");
		growerDTO.setProduction(new BigDecimal("1000.50"));
		growerDTO.setCommissionRate(new BigDecimal("5.00"));
		growerDTO.setCalculatedValue(new BigDecimal("5002.50"));

		validJsonPayload = String.format("""
				{
					"name": "Produtor Agrícola Silva",
					"registration": "123.456.789-09",
					"address": "Rodovia BR-116, Km 50",
					"laboratoryId": "%s",
					"farmsteadIds": ["%s"],
					"operationInitialDate": "2026-01-01",
					"operationFinalDate": "2026-12-31",
					"observations": "Operação de safra principal",
					"production": 1000.50,
					"commissionRate": 5.00
				}
				""", growerDTO.getLaboratoryId(), growerDTO.getFarmsteadIds().get(0));
	}

	@Nested
	@DisplayName("GET / - Busca Paginada")
	class GetAllTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK e PaginatedResponse")
		void shouldReturn200OKAndPaginatedList() throws Exception {
			Page<GrowerDTO> growerPage = new PageImpl<>(List.of(growerDTO), PageRequest.of(0, 10), 1L);
			PaginatedResponse<GrowerDTO> paginatedResponse = PaginatedResponse.from(growerPage);

			given(growerService.getAll(any(Pageable.class))).willReturn(paginatedResponse);

			mockMvc.perform(get("/api/v1/growers").param("page", "0").param("size", "10")
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$.content[0].id").value(existingId.toString()))
					.andExpect(jsonPath("$.content[0].name").value("Produtor Agrícola Silva"))
					.andExpect(jsonPath("$.totalElements").value(1)).andExpect(jsonPath("$.totalPages").value(1))
					.andExpect(jsonPath("$.isFirst").value(true));
		}
	}

	@Nested
	@DisplayName("GET /api/v1/growers/{id} - Busca por ID")
	class FindByIdTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK quando encontrar o ID")
		void shouldReturn200OKWhenIdExists() throws Exception {
			given(growerService.findById(existingId)).willReturn(growerDTO);

			mockMvc.perform(get("/api/v1/growers/{id}", existingId).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(existingId.toString()))
					.andExpect(jsonPath("$.name").value("Produtor Agrícola Silva"));
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found quando o ID não existir")
		void shouldReturn404NotFoundWhenIdDoesNotExist() throws Exception {
			given(growerService.findById(nonExistingId)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

			mockMvc.perform(get("/api/v1/growers/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("POST /api/v1/growers - Criação")
	class CreateTests {

		@Test
		@DisplayName("Deve retornar HTTP 201 Created ao enviar DTO válido")
		void shouldReturn201CreatedWhenPayloadIsValid() throws Exception {
			given(growerService.create(any(GrowerRequestDTO.class))).willReturn(growerDTO);

			mockMvc.perform(post("/api/v1/growers").contentType(MediaType.APPLICATION_JSON).content(validJsonPayload))
					.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(existingId.toString()))
					.andExpect(jsonPath("$.name").value("Produtor Agrícola Silva"));
		}

		@Test
		@DisplayName("Deve retornar HTTP 400 Bad Request se a validação @Valid falhar")
		void shouldReturn400BadRequestWhenPayloadIsInvalid() throws Exception {
			String invalidJson = "{}";

			mockMvc.perform(post("/api/v1/growers").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("PUT /api/v1/growers/{id} - Atualização")
	class UpdateTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK ao atualizar um registro existente")
		void shouldReturn200OKWhenUpdateIsValid() throws Exception {
			given(growerService.update(eq(existingId), any(GrowerRequestDTO.class))).willReturn(growerDTO);

			mockMvc.perform(put("/api/v1/growers/{id}", existingId).contentType(MediaType.APPLICATION_JSON)
					.content(validJsonPayload)).andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(existingId.toString()));
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found ao atualizar ID inexistente")
		void shouldReturn404WhenUpdatingNonExistingId() throws Exception {
			given(growerService.update(eq(nonExistingId), any(GrowerRequestDTO.class)))
					.willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

			mockMvc.perform(put("/api/v1/growers/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON)
					.content(validJsonPayload)).andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("DELETE /api/v1/growers/{id} - Exclusão")
	class DeleteTests {

		@Test
		@DisplayName("Deve retornar HTTP 204 No Content ao excluir com sucesso")
		void shouldReturn204NoContentWhenDeletedSuccessfully() throws Exception {
			willDoNothing().given(growerService).delete(existingId);

			mockMvc.perform(delete("/api/v1/growers/{id}", existingId)).andExpect(status().isNoContent());
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found ao deletar ID inexistente")
		void shouldReturn404NotFoundWhenDeletingNonExistingId() throws Exception {
			willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(growerService).delete(nonExistingId);

			mockMvc.perform(delete("/api/v1/growers/{id}", nonExistingId)).andExpect(status().isNotFound());
		}
	}
}