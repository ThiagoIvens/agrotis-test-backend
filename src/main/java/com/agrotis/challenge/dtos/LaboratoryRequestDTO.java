package com.agrotis.challenge.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class LaboratoryRequestDTO extends BaseRequestDTO {
	@NotNull(message = "O custo de operação do laboratorio é obrigatório")
	private BigDecimal operationCost;
	
	@NotNull(message = "A taxa de operação do laboratorio é obrigatória.")
	@PositiveOrZero
	private BigDecimal operationFee;
}

