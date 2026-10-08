package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrowerRequestDTO extends BaseRequestDTO {

    @NotNull(message = "O produtor deve estar relacionado a um laboratório.")
    private UUID laboratoryId;

    private List<UUID> farmsteadIds;

    private LocalDate operationInitialDate;
    private LocalDate operationFinalDate;
    private String observations;

    @NotNull(message = "A produção total é obrigatória.")
    private BigDecimal production;

    @NotNull(message = "A taxa de comissão é obrigatória.")
    @PositiveOrZero(message = "A taxa de comissão deve ser zero ou positiva.")
    private BigDecimal commissionRate;
}