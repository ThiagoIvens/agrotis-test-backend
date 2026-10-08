package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public interface LaboratoryReportResponseDTO {
	UUID getLaboratoryCode();
	String getLaboratoryName();
	Long getTotalLinkedGrowers();
	BigDecimal getFinancialValueCalculated();
}
