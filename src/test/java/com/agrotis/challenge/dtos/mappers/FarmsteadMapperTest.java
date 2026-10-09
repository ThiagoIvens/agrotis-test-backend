package com.agrotis.challenge.dtos.mappers;

import com.agrotis.challenge.dtos.FarmsteadDTO;
import com.agrotis.challenge.dtos.FarmsteadRequestDTO;
import com.agrotis.challenge.entities.Farmstead;
import com.agrotis.challenge.entities.Grower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FarmsteadMapperTest {

	private FarmsteadMapper farmsteadMapper;

	@BeforeEach
	void setUp() {
		farmsteadMapper = new FarmsteadMapper();
	}

	@Test
	@DisplayName("Deve converter Farmstead Entity para DTO validando cálculo e dados do Grower")
	void shouldMapEntityToDTO() {
		// Cenário
		Grower grower = new Grower();
		UUID growerId = UUID.randomUUID();
		grower.setId(growerId);
		grower.setName("Dono da Fazenda");

		Farmstead farmstead = new Farmstead();
		farmstead.setId(UUID.randomUUID());
		farmstead.setName("Fazenda Boa Esperança");
		farmstead.setRegistration("FARM-01");
		farmstead.setAddress("Zona Rural");
		farmstead.setTotalAreaInHectares(new BigDecimal("10.5"));
		farmstead.setTaxPerHectare(new BigDecimal("100.0"));
		farmstead.setGrower(grower);

		// Execução
		FarmsteadDTO dto = farmsteadMapper.toDTO(farmstead);

		// Validação
		assertNotNull(dto);
		assertEquals(farmstead.getId(), dto.getId());
		assertEquals(farmstead.getName(), dto.getName());
		assertThat(dto.getCalculatedValue()).isEqualByComparingTo(new BigDecimal("1050.0"));
		assertEquals(growerId, dto.getGrowerId());
		assertEquals("Dono da Fazenda", dto.getGrowerName());
	}

	@Test
	@DisplayName("Deve converter FarmsteadRequestDTO para Entity com sucesso")
	void shouldMapRequestToEntity() {
		// Cenário
		FarmsteadRequestDTO request = new FarmsteadRequestDTO();
		request.setName("Nova Fazenda");
		request.setTotalAreaInHectares(new BigDecimal("20.0"));
		request.setTaxPerHectare(new BigDecimal("50.0"));

		// Execução
		Farmstead entity = farmsteadMapper.toEntity(request);

		// Validação
		assertNotNull(entity);
		assertEquals("Nova Fazenda", entity.getName());
		assertEquals(new BigDecimal("20.0"), entity.getTotalAreaInHectares());
	}

	@Test
	@DisplayName("Deve atualizar os dados da Fazenda a partir do RequestDTO")
	void shouldUpdateEntityFromDTO() {
		// Cenário
		Farmstead entity = new Farmstead();
		entity.setId(UUID.randomUUID());
		entity.setName("Fazenda Antiga");

		FarmsteadRequestDTO request = new FarmsteadRequestDTO();
		request.setName("Fazenda Nova");
		request.setTotalAreaInHectares(new BigDecimal("30.0"));
		request.setTaxPerHectare(new BigDecimal("10.0"));

		// Execução
		farmsteadMapper.updateEntityFromDTO(request, entity);

		// Validação
		assertEquals("Fazenda Nova", entity.getName());
		assertEquals(new BigDecimal("30.0"), entity.getTotalAreaInHectares());
	}
}