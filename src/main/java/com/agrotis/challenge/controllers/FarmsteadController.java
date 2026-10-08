package com.agrotis.challenge.controllers;

import java.net.URI;
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

import com.agrotis.challenge.dtos.FarmsteadDTO;
import com.agrotis.challenge.dtos.FarmsteadRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.services.FarmsteadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/farmsteads")
@Tag(name = "Farmsteads", description = "Gerenciamento de propriedades rurais")
public class FarmsteadController {

	private final FarmsteadService farmsteadService;

	public FarmsteadController(FarmsteadService farmsteadService) {
		this.farmsteadService = farmsteadService;
	}

	@GetMapping
	@Operation(summary = "Listar propriedade rurales", description = "Retorna uma lista paginada de propriedade rurales.")
	@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	@SuppressWarnings("null") // Pelo que entendi a resolução do pageable é garantida pelo framework Spring
	public ResponseEntity<PaginatedResponse<FarmsteadDTO>> getAll(@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(farmsteadService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID", description = "Retorna os detalhes de uma propriedade rural específica.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Propriedade rural encontrada"),
			@ApiResponse(responseCode = "404", description = "Propriedade rural não encontrada", content = @Content) })
	public ResponseEntity<FarmsteadDTO> findById(@PathVariable UUID id) {
		UUID validId = Objects.requireNonNull(id, "O ID da propriedade rural não pode ser nulo");
		return ResponseEntity.ok(farmsteadService.findById(validId));
	}

	@PostMapping
	@Operation(summary = "Criar propriedade rural", description = "Cadastra uma nova propriedade rural na base de dados.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Propriedade rural criada com sucesso", headers = @Header(name = "Location", description = "URI do recurso criado")),
			@ApiResponse(responseCode = "400", description = "Erro de validação nos dados de requisição", content = @Content) })
	public ResponseEntity<FarmsteadDTO> create(@Valid @RequestBody FarmsteadRequestDTO request) {
		FarmsteadDTO created = farmsteadService.create(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId())
				.toUri();

		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar propriedade rural", description = "Atualiza os dados de um propriedade rural existente.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Propriedade rural atualizada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Propriedade rural não encontrada", content = @Content) })
	public ResponseEntity<FarmsteadDTO> update(@PathVariable UUID id,
			@Valid @RequestBody FarmsteadRequestDTO request) {
		UUID validId = Objects.requireNonNull(id, "O ID da propriedade rural não pode ser nulo");
		return ResponseEntity.ok(farmsteadService.update(validId, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Remover propriedade rural", description = "Exclui logicamente ou fisicamente uma propriedade rural.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Propriedade rural removida com sucesso"),
			@ApiResponse(responseCode = "404", description = "Propriedade rural não encontrada", content = @Content) })
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		UUID validId = Objects.requireNonNull(id, "O ID da propriedade rural não pode ser nulo");
		farmsteadService.delete(validId);
		return ResponseEntity.noContent().build();
	}
}