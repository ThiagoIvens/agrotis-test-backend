package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrowerDTO extends BaseDTO {
	private UUID laboratoryId;
	private String laboratoryName;
	private List<FarmsteadSummaryDTO> farmsteads;
	private LocalDate operationInitialDate;
	private LocalDate operationFinalDate;
	private String observations;
	private BigDecimal production;
	private BigDecimal commissionRate;
	private BigDecimal calculatedValue;
}