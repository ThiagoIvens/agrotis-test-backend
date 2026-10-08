package com.agrotis.challenge.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmsteadDTO extends BaseDTO {
	private UUID growerId;
	private String growerName;
	private BigDecimal totalAreaInHectares;
	private BigDecimal taxPerHectare;
	private BigDecimal calculatedValue;
}