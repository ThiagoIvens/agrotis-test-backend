package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class GrowerRequestDTO extends BaseRequestDTO {
	@NotNull(message = "O produtor deve estar relacionado a um laboratorio")
    private UUID laboratoryId;
	
	@NotNull(message = "O produtor deve ter pelo menos uma propriedade rural")
    private List<UUID> farmsteads;
	
	@NotNull(message = "A produção total é obrigatória")
	private BigDecimal production;

	@NotNull(message = "A taxa de comissão é obrigatória")
	@PositiveOrZero
	private BigDecimal commissionRate;
}
