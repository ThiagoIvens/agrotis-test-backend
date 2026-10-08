package com.agrotis.challenge.controllers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.agrotis.challenge.dtos.LaboratoryDTO;
import com.agrotis.challenge.dtos.LaboratoryReportResponseDTO;
import com.agrotis.challenge.dtos.LaboratoryRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.ReportFilterDTO;
import com.agrotis.challenge.services.LaboratoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/laboratories")
@Tag(name = "Laboratories", description = "Gerenciamento de Laboratórios")
public class LaboratoryController {

	private final LaboratoryService laboratoryService;

	public LaboratoryController(LaboratoryService laboratoryService) {
		this.laboratoryService = laboratoryService;
	}

	@GetMapping
	@Operation(summary = "Listar laboratórios", description = "Retorna uma lista paginada de laboratórios.")
	@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	@SuppressWarnings("null") // Pelo que entendi a resolução do pageable é garantida pelo framework Spring
	public ResponseEntity<PaginatedResponse<LaboratoryDTO>> getAll(@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(laboratoryService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID", description = "Retorna os detalhes de um laboratório específico.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Laboratório encontrado"),
			@ApiResponse(responseCode = "404", description = "Laboratório não encontrado", content = @Content) })
	public ResponseEntity<LaboratoryDTO> findById(@PathVariable UUID id) {
		UUID validId = Objects.requireNonNull(id, "O ID do laboratório não pode ser nulo");
		return ResponseEntity.ok(laboratoryService.findById(validId));
	}

	@GetMapping("/reports")
	@Operation(summary = "Gerar relatório de laboratórios", description = "Retorna dados para relatórios baseados em filtros complexos.")
	@ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
	public ResponseEntity<List<LaboratoryReportResponseDTO>> generateReport(@ParameterObject ReportFilterDTO filter) {
		return ResponseEntity.ok(laboratoryService.generateReport(filter));
	}

	@PostMapping
	@Operation(summary = "Criar laboratório", description = "Cadastra um novo laboratório na base de dados.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Laboratório criado com sucesso", headers = @Header(name = "Location", description = "URI do recurso criado")),
			@ApiResponse(responseCode = "400", description = "Erro de validação nos dados de requisição", content = @Content) })
	public ResponseEntity<LaboratoryDTO> create(@Valid @RequestBody LaboratoryRequestDTO request) {
		LaboratoryDTO created = laboratoryService.create(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId())
				.toUri();

		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar laboratório", description = "Atualiza os dados de um laboratório existente.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Laboratório atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Laboratório não encontrado", content = @Content) })
	public ResponseEntity<LaboratoryDTO> update(@PathVariable UUID id,
			@Valid @RequestBody LaboratoryRequestDTO request) {
		UUID validId = Objects.requireNonNull(id, "O ID do laboratório não pode ser nulo");
		return ResponseEntity.ok(laboratoryService.update(validId, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Remover laboratório", description = "Exclui logicamente ou fisicamente um laboratório.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Laboratório removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Laboratório não encontrado", content = @Content) })
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		UUID validId = Objects.requireNonNull(id, "O ID do laboratório não pode ser nulo");
		laboratoryService.delete(validId);
		return ResponseEntity.noContent().build();
	}
}