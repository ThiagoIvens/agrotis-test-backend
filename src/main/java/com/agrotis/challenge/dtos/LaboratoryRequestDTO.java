package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
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
public class LaboratoryRequestDTO extends BaseRequestDTO {

    @NotNull(message = "O custo de operação do laboratório é obrigatório.")
    private BigDecimal operationCost;

    @NotNull(message = "A taxa de operação do laboratório é obrigatória.")
    @PositiveOrZero(message = "A taxa de operação deve ser zero ou positiva.")
    private BigDecimal operationFee;
}