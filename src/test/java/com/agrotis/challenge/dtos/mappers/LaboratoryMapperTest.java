package com.agrotis.challenge.dtos.mappers;

import com.agrotis.challenge.dtos.LaboratoryDTO;
import com.agrotis.challenge.dtos.LaboratoryRequestDTO;
import com.agrotis.challenge.entities.Laboratory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LaboratoryMapperTest {

	private LaboratoryMapper laboratoryMapper;

	@BeforeEach
	void setUp() {
		laboratoryMapper = new LaboratoryMapper();
	}

	@Test
	@DisplayName("Deve converter Laboratory Entity para DTO validando o cálculo financeiro")
	void shouldMapEntityToDTO() {
		// Cenário
		Laboratory laboratory = new Laboratory();
		laboratory.setId(UUID.randomUUID());
		laboratory.setName("Lab Alfa");
		laboratory.setRegistration("LAB-001");
		laboratory.setAddress("Av Central");
		laboratory.setOperationCost(new BigDecimal("200.0"));
		laboratory.setOperationFee(new BigDecimal("1.5"));

		// Execução
		LaboratoryDTO dto = laboratoryMapper.toDTO(laboratory);

		// Validação
		assertNotNull(dto);
		assertEquals(laboratory.getId(), dto.getId());
		assertEquals("Lab Alfa", dto.getName());
		assertThat(dto.getCalculatedValue()).isEqualByComparingTo(new BigDecimal("300.0"));
	}

	@Test
	@DisplayName("Deve converter LaboratoryRequestDTO para Entity com sucesso")
	void shouldMapRequestToEntity() {
		// Cenário
		LaboratoryRequestDTO request = new LaboratoryRequestDTO();
		request.setName("Lab Beta");
		request.setOperationCost(new BigDecimal("500.0"));
		request.setOperationFee(new BigDecimal("2.0"));

		// Execução
		Laboratory entity = laboratoryMapper.toEntity(request);

		// Validação
		assertNotNull(entity);
		assertEquals("Lab Beta", entity.getName());
		assertEquals(new BigDecimal("500.0"), entity.getOperationCost());
	}

	@Test
	@DisplayName("Deve atualizar os dados do laboratório a partir do RequestDTO")
	void shouldUpdateEntityFromDTO() {
		// Cenário
		Laboratory entity = new Laboratory();
		entity.setId(UUID.randomUUID());
		entity.setName("Lab Antigo");

		LaboratoryRequestDTO request = new LaboratoryRequestDTO();
		request.setName("Lab Atualizado");
		request.setOperationCost(new BigDecimal("1000.0"));
		request.setOperationFee(new BigDecimal("1.0"));

		// Execução
		laboratoryMapper.updateEntityFromDTO(request, entity);

		// Validação
		assertEquals("Lab Atualizado", entity.getName());
		assertEquals(new BigDecimal("1000.0"), entity.getOperationCost());
	}
}