package com.agrotis.challenge.dtos;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class ReportFilterDTO {
    private LocalDate initialDateInit;
    private LocalDate initialDateEnd;
    private LocalDate finalDateInit;
    private LocalDate finalDateEnd;
    private String search;

    @NotNull(message = "A quantidade mínima de produtores é obrigatória.")
    @Min(value = 0, message = "A quantidade mínima de produtores não pode ser negativa.")
    private Long minGrowerQty;

	public LocalDate getInitialDateInit() {
		return initialDateInit;
	}

	public void setInitialDateInit(LocalDate initialDateInit) {
		this.initialDateInit = initialDateInit;
	}

	public LocalDate getInitialDateEnd() {
		return initialDateEnd;
	}

	public void setInitialDateEnd(LocalDate initialDateEnd) {
		this.initialDateEnd = initialDateEnd;
	}

	public LocalDate getFinalDateInit() {
		return finalDateInit;
	}

	public void setFinalDateInit(LocalDate finalDateInit) {
		this.finalDateInit = finalDateInit;
	}

	public LocalDate getFinalDateEnd() {
		return finalDateEnd;
	}

	public void setFinalDateEnd(LocalDate finalDateEnd) {
		this.finalDateEnd = finalDateEnd;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Long getMinGrowerQty() {
		return minGrowerQty;
	}

	public void setMinGrowerQty(Long minGrowerQty) {
		this.minGrowerQty = minGrowerQty;
	}
    
}