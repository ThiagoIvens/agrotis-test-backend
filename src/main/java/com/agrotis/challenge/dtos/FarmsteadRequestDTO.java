package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmsteadRequestDTO extends BaseRequestDTO {

	@NotNull(message = "A propriedade deve estar vinculada a um produtor.")
	private UUID growerId;

	@NotNull(message = "A área total é obrigatória.")
	@Positive(message = "A área deve ser maior que zero.")
	private BigDecimal totalAreaInHectares;

	@NotNull(message = "A taxa por hectare é obrigatória.")
	@PositiveOrZero(message = "A taxa por hectare deve ser zero ou positiva.")
	private BigDecimal taxPerHectare;
}