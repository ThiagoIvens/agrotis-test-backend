package com.agrotis.challenge.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class FarmsteadRequestDTO extends BaseRequestDTO {
	@NotNull(message = "A área total é obrigatória")
	@Positive(message = "A área deve ser maior que zero")
	private BigDecimal totalAreaInHectares;

	@NotNull(message = "A taxa por hectare é obrigatória")
	@PositiveOrZero
	private BigDecimal taxPerHectare;
}
