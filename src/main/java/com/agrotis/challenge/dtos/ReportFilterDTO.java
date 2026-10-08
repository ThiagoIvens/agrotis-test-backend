package com.agrotis.challenge.dtos;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportFilterDTO {
    private LocalDate initialDateInit;
    private LocalDate initialDateEnd;
    private LocalDate finalDateInit;
    private LocalDate finalDateEnd;
    private String search;

    @NotNull(message = "A quantidade mínima de produtores é obrigatória.")
    @Min(value = 0, message = "A quantidade mínima de produtores não pode ser negativa.")
    private Long minGrowerQty;
}