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

import com.agrotis.challenge.dtos.GrowerDTO;
import com.agrotis.challenge.dtos.GrowerRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.services.GrowerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/growers")
@Tag(name = "Growers", description = "Gerenciamento de Produtores")
public class GrowerController {

	private final GrowerService growerService;

	public GrowerController(GrowerService growerService) {
		this.growerService = growerService;
	}

	@GetMapping
	@Operation(summary = "Listar produtores", description = "Retorna uma lista paginada de produtores.")
	@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	@SuppressWarnings("null") // Pelo que entendi a resolução do pageable é garantida pelo framework Spring
	public ResponseEntity<PaginatedResponse<GrowerDTO>> getAll(@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(growerService.getAll(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar por ID", description = "Retorna os detalhes de um produtor específico.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Produtor encontrado"),
			@ApiResponse(responseCode = "404", description = "Produtor não encontrado", content = @Content) })
	public ResponseEntity<GrowerDTO> findById(@PathVariable UUID id) {
		UUID validId = Objects.requireNonNull(id, "O ID do produtor não pode ser nulo");
		return ResponseEntity.ok(growerService.findById(validId));
	}

	@PostMapping
	@Operation(summary = "Criar produtor", description = "Cadastra um novo produtor na base de dados.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Produtor criado com sucesso", headers = @Header(name = "Location", description = "URI do recurso criado")),
			@ApiResponse(responseCode = "400", description = "Erro de validação nos dados de requisição", content = @Content) })
	public ResponseEntity<GrowerDTO> create(@Valid @RequestBody GrowerRequestDTO request) {
		GrowerDTO created = growerService.create(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId())
				.toUri();

		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar produtor", description = "Atualiza os dados de um produtor existente.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Produtor atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Produtor não encontrado", content = @Content) })
	public ResponseEntity<GrowerDTO> update(@PathVariable UUID id,
			@Valid @RequestBody GrowerRequestDTO request) {
		UUID validId = Objects.requireNonNull(id, "O ID do produtor não pode ser nulo");
		return ResponseEntity.ok(growerService.update(validId, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Remover produtor", description = "Exclui logicamente ou fisicamente um produtor.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Produtor removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Produtor não encontrado", content = @Content) })
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		UUID validId = Objects.requireNonNull(id, "O ID do produtor não pode ser nulo");
		growerService.delete(validId);
		return ResponseEntity.noContent().build();
	}
}