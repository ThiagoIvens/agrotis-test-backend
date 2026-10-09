package com.agrotis.challenge.controllers;

import com.agrotis.challenge.dtos.LaboratoryDTO;
import com.agrotis.challenge.dtos.LaboratoryReportResponseDTO;
import com.agrotis.challenge.dtos.LaboratoryRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.ReportFilterDTO;
import com.agrotis.challenge.services.LaboratoryService;
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
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LaboratoryController.class)
@DisplayName("LaboratoryController - Testes da Camada Web")
class LaboratoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private LaboratoryService laboratoryService;

	private UUID existingId;
	private UUID nonExistingId;
	private LaboratoryDTO laboratoryDTO;
	private String validJsonPayload;

	@BeforeEach
	void setUp() {
		existingId = UUID.randomUUID();
		nonExistingId = UUID.randomUUID();

		laboratoryDTO = new LaboratoryDTO();
		laboratoryDTO.setId(existingId);
		laboratoryDTO.setName("AgroLab Análises de Solo");
		// CNPJ com dígitos verificadores reais para aprovar na matemática do validador
		laboratoryDTO.setRegistration("00.000.000/0001-91"); 
		laboratoryDTO.setAddress("Rua do Laboratório, 123");
		laboratoryDTO.setOperationCost(new BigDecimal("200.00"));
		laboratoryDTO.setOperationFee(new BigDecimal("50.00"));
		laboratoryDTO.setCalculatedValue(new BigDecimal("250.00"));

		validJsonPayload = """
				{
					"name": "AgroLab Análises de Solo",
					"registration": "00.000.000/0001-91",
					"address": "Rua do Laboratório, 123",
					"operationCost": 200.00,
					"operationFee": 50.00
				}
				""";
	}

	@Nested
	@DisplayName("GET / - Listagem Paginada")
	class GetAllTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK com PaginatedResponse")
		void shouldReturn200OKAndPaginatedData() throws Exception {
			Page<LaboratoryDTO> laboratoryPage = new PageImpl<>(List.of(laboratoryDTO), PageRequest.of(0, 10), 1L);
			PaginatedResponse<LaboratoryDTO> paginatedResponse = PaginatedResponse.from(laboratoryPage);

			given(laboratoryService.getAll(any(Pageable.class))).willReturn(paginatedResponse);

			mockMvc.perform(get("/api/v1/laboratories").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(existingId.toString()))
					.andExpect(jsonPath("$.content[0].name").value("AgroLab Análises de Solo"));
		}
	}

	@Nested
	@DisplayName("GET /{id} - Busca por ID")
	class FindByIdTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK quando encontrar")
		void shouldReturn200OKWhenFound() throws Exception {
			given(laboratoryService.findById(existingId)).willReturn(laboratoryDTO);

			mockMvc.perform(get("/api/v1/laboratories/{id}", existingId)).andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(existingId.toString()))
					.andExpect(jsonPath("$.name").value("AgroLab Análises de Solo"));
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found quando ID não existir")
		void shouldReturn404WhenNotFound() throws Exception {
			given(laboratoryService.findById(nonExistingId))
					.willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

			mockMvc.perform(get("/api/v1/laboratories/{id}", nonExistingId)).andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("POST / - Cadastro")
	class CreateTests {

		@Test
		@DisplayName("Deve retornar HTTP 201 Created quando DTO for válido")
		void shouldReturn201CreatedWhenValid() throws Exception {
			given(laboratoryService.create(any(LaboratoryRequestDTO.class))).willReturn(laboratoryDTO);

			mockMvc.perform(
					post("/api/v1/laboratories").contentType(MediaType.APPLICATION_JSON).content(validJsonPayload))
					.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(existingId.toString()));
		}

		@Test
		@DisplayName("Deve retornar HTTP 400 Bad Request se falhar o @Valid")
		void shouldReturn400BadRequestWhenInvalid() throws Exception {
			String invalidJson = "{}";

			mockMvc.perform(post("/api/v1/laboratories").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("PUT /{id} - Atualização")
	class UpdateTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK ao atualizar com sucesso")
		void shouldReturn200OKWhenValid() throws Exception {
			given(laboratoryService.update(eq(existingId), any(LaboratoryRequestDTO.class))).willReturn(laboratoryDTO);

			mockMvc.perform(put("/api/v1/laboratories/{id}", existingId).contentType(MediaType.APPLICATION_JSON)
					.content(validJsonPayload)).andExpect(status().isOk())
					.andExpect(jsonPath("$.name").value("AgroLab Análises de Solo"));
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found ao atualizar inexistente")
		void shouldReturn404WhenNotFound() throws Exception {
			given(laboratoryService.update(eq(nonExistingId), any(LaboratoryRequestDTO.class)))
					.willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

			mockMvc.perform(put("/api/v1/laboratories/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON)
					.content(validJsonPayload)).andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("DELETE /{id} - Exclusão")
	class DeleteTests {

		@Test
		@DisplayName("Deve retornar HTTP 204 No Content ao excluir com sucesso")
		void shouldReturn204NoContent() throws Exception {
			willDoNothing().given(laboratoryService).delete(existingId);

			mockMvc.perform(delete("/api/v1/laboratories/{id}", existingId)).andExpect(status().isNoContent());
		}

		@Test
		@DisplayName("Deve retornar HTTP 404 Not Found ao excluir inexistente")
		void shouldReturn404NotFound() throws Exception {
			willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(laboratoryService).delete(nonExistingId);

			mockMvc.perform(delete("/api/v1/laboratories/{id}", nonExistingId)).andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("GET /reports - Geração de Relatórios (Endpoint Único)")
	class GenerateReportTests {

		@Test
		@DisplayName("Deve retornar HTTP 200 OK e a lista de relatórios")
		void shouldReturn200OKAndListOfReportsWhenFilterIsProvided() throws Exception {
			ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
			LaboratoryReportResponseDTO reportItem = factory.createProjection(LaboratoryReportResponseDTO.class,
					Map.of("laboratoryCode", existingId, "laboratoryName", "AgroLab Análises de Solo",
							"totalLinkedGrowers", 15L, "financialValueCalculated", new BigDecimal("1500.00")));

			given(laboratoryService.generateReport(any(ReportFilterDTO.class))).willReturn(List.of(reportItem));

			mockMvc.perform(get("/api/v1/laboratories/reports").param("initialDateInit", "2026-01-01")
					.param("initialDateEnd", "2026-01-31").param("finalDateInit", "2026-10-01")
					.param("finalDateEnd", "2026-10-31").param("search", "soil").param("minGrowerQty", "0")
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$").isArray())
					.andExpect(jsonPath("$[0].laboratoryCode").value(existingId.toString()))
					.andExpect(jsonPath("$[0].laboratoryName").value("AgroLab Análises de Solo"))
					.andExpect(jsonPath("$[0].totalLinkedGrowers").value(15))
					.andExpect(jsonPath("$[0].financialValueCalculated").value(1500.00));
		}
	}
}