package com.agrotis.challenge.services;

import com.agrotis.challenge.dtos.LaboratoryReportResponseDTO;
import com.agrotis.challenge.dtos.ReportFilterDTO;
import com.agrotis.challenge.repositories.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaboratoryServiceTest {

	@Mock
	private LaboratoryRepository laboratoryRepository;

	@InjectMocks
	private LaboratoryService laboratoryService;

	private ReportFilterDTO filterDTO;
	private LaboratoryReportResponseDTO responseDTO;
	private final UUID existingId = UUID.randomUUID();

	@BeforeEach
	void setUp() {
		// 1. Instanciação e população do ReportFilterDTO via Setters
		filterDTO = new ReportFilterDTO();
		filterDTO.setInitialDateInit(LocalDate.of(2026, 1, 1));
		filterDTO.setInitialDateEnd(LocalDate.of(2026, 1, 31));
		filterDTO.setFinalDateInit(LocalDate.of(2026, 10, 1));
		filterDTO.setFinalDateEnd(LocalDate.of(2026, 10, 31));
		filterDTO.setSearch("soil");
		filterDTO.setMinGrowerQty(5L);

		// 2. Mock da Interface Projection do Spring Data
		responseDTO = mock(LaboratoryReportResponseDTO.class);
		lenient().when(responseDTO.getLaboratoryCode()).thenReturn(existingId);
		lenient().when(responseDTO.getLaboratoryName()).thenReturn("AGROLAB ANÁLISES DE SOLO");
		lenient().when(responseDTO.getTotalLinkedGrowers()).thenReturn(15L);
		lenient().when(responseDTO.getFinancialValueCalculated()).thenReturn(new BigDecimal("1500.00"));
	}

	@Test
	@DisplayName("Deve gerar o relatório de laboratório preenchendo os dados corretos conforme o filtro informado")
	void generateReport_ShouldReturnReportList_WhenFilterMatches() {
		// Arrange
		when(laboratoryRepository.generateLaboratoryReport(any(LocalDate.class), any(LocalDate.class),
				any(LocalDate.class), any(LocalDate.class), any(String.class), any(Long.class)))
				.thenReturn(List.of(responseDTO));

		// Act
		List<LaboratoryReportResponseDTO> result = laboratoryService.generateReport(filterDTO);

		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getLaboratoryCode()).isEqualTo(existingId);
		assertThat(result.get(0).getLaboratoryName()).isEqualTo("AGROLAB ANÁLISES DE SOLO");
		assertThat(result.get(0).getTotalLinkedGrowers()).isEqualTo(15L);
		assertThat(result.get(0).getFinancialValueCalculated()).isEqualTo(new BigDecimal("1500.00"));

		verify(laboratoryRepository, times(1)).generateLaboratoryReport(eq(filterDTO.getInitialDateInit()),
				eq(filterDTO.getInitialDateEnd()), eq(filterDTO.getFinalDateInit()), eq(filterDTO.getFinalDateEnd()),
				any(String.class), eq(filterDTO.getMinGrowerQty()));
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando nenhum registro corresponder aos critérios de filtro do relatório")
	void generateReport_ShouldReturnEmptyList_WhenNoMatchFound() {
		// Arrange
		when(laboratoryRepository.generateLaboratoryReport(any(), any(), any(), any(), any(), any()))
				.thenReturn(Collections.emptyList());

		// Act
		List<LaboratoryReportResponseDTO> result = laboratoryService.generateReport(filterDTO);

		// Assert
		assertThat(result).isNotNull().isEmpty();

		verify(laboratoryRepository, times(1)).generateLaboratoryReport(any(), any(), any(), any(), any(), any());
	}
}