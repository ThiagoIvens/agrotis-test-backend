package com.agrotis.challenge.dtos.mappers;

import com.agrotis.challenge.dtos.GrowerDTO;
import com.agrotis.challenge.dtos.GrowerRequestDTO;
import com.agrotis.challenge.entities.Farmstead;
import com.agrotis.challenge.entities.Grower;
import com.agrotis.challenge.entities.Laboratory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GrowerMapperTest {

	private GrowerMapper growerMapper;

	@BeforeEach
	void setUp() {
		growerMapper = new GrowerMapper();
	}

	@Test
	@DisplayName("Deve converter Grower Entity para GrowerDTO com sucesso, incluindo valor calculado e relacionamentos")
	void shouldMapEntityToDTO() {
		// Cenário
		Laboratory laboratory = new Laboratory();
		laboratory.setId(UUID.randomUUID());
		laboratory.setName("Lab Central");

		Farmstead farmstead = new Farmstead();
		UUID farmsteadId = UUID.randomUUID();
		farmstead.setId(farmsteadId);
		farmstead.setName("Fazenda Esperança");

		Grower grower = new Grower();
		grower.setId(UUID.randomUUID());
		grower.setName("Produtor X");
		grower.setRegistration("REG-123");
		grower.setAddress("Rua A, 100");
		grower.setOperationInitialDate(LocalDate.of(2026, 1, 1));
		grower.setOperationFinalDate(LocalDate.of(2026, 12, 31));
		grower.setObservations("Nenhuma");
		grower.setProduction(new BigDecimal("100.00"));
		grower.setCommissionRate(new BigDecimal("2.5"));
		grower.setLaboratory(laboratory);
		grower.setFarmsteads(List.of(farmstead));

		// Execução
		GrowerDTO dto = growerMapper.toDTO(grower);

		// Validação
		assertNotNull(dto);
		assertEquals(grower.getId(), dto.getId());
		assertEquals(grower.getName(), dto.getName());
		assertEquals(grower.getRegistration(), dto.getRegistration());
		assertEquals(grower.getAddress(), dto.getAddress());
		assertThat(dto.getCalculatedValue()).isEqualByComparingTo(new BigDecimal("250.00"));
		assertEquals(laboratory.getId(), dto.getLaboratoryId());
		assertEquals(laboratory.getName(), dto.getLaboratoryName());
		
		// Validação da lista de FarmsteadSummaryDTO (farmsteads)
		assertNotNull(dto.getFarmsteads());
		assertEquals(1, dto.getFarmsteads().size());
		assertEquals(farmsteadId, dto.getFarmsteads().get(0).getId());
		assertEquals("Fazenda Esperança", dto.getFarmsteads().get(0).getName());
	}

	@Test
	@DisplayName("Deve retornar nulo ao tentar mapear entidade nula para DTO")
	void shouldReturnNullWhenEntityIsNull() {
		assertNull(growerMapper.toDTO(null));
	}

	@Test
	@DisplayName("Deve converter GrowerRequestDTO para Grower Entity com sucesso")
	void shouldMapRequestToEntity() {
		// Cenário
		GrowerRequestDTO request = new GrowerRequestDTO();
		request.setName("Produtor Request");
		request.setRegistration("REG-999");
		request.setAddress("Avenida B");
		request.setOperationInitialDate(LocalDate.of(2026, 3, 1));
		request.setOperationFinalDate(LocalDate.of(2026, 10, 1));
		request.setObservations("Obs request");
		request.setProduction(new BigDecimal("50.00"));
		request.setCommissionRate(new BigDecimal("2.0"));

		// Execução
		Grower entity = growerMapper.toEntity(request);

		// Validação
		assertNotNull(entity);
		assertNull(entity.getId(), "O ID gerado por banco deve iniciar nulo");
		assertEquals(request.getName(), entity.getName());
		assertEquals(request.getRegistration(), entity.getRegistration());
		assertEquals(request.getProduction(), entity.getProduction());
	}

	@Test
	@DisplayName("Deve atualizar os campos da Entidade Grower a partir do RequestDTO")
	void shouldUpdateEntityFromDTO() {
		// Cenário
		Grower entity = new Grower();
		entity.setId(UUID.randomUUID());
		entity.setName("Nome Antigo");

		GrowerRequestDTO request = new GrowerRequestDTO();
		request.setName("Nome Atualizado");
		request.setRegistration("REG-NOVO");
		request.setProduction(new BigDecimal("200.00"));
		request.setCommissionRate(new BigDecimal("1.5"));

		// Execução
		growerMapper.updateEntityFromDTO(request, entity);

		// Validação
		assertNotNull(entity.getId(), "O ID da entidade original deve ser preservado");
		assertEquals("Nome Atualizado", entity.getName());
		assertEquals("REG-NOVO", entity.getRegistration());
		assertEquals(new BigDecimal("200.00"), entity.getProduction());
	}
}