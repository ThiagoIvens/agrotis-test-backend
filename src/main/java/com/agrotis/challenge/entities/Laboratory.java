package com.agrotis.challenge.entities;

import java.math.BigDecimal;

import com.agrotis.challenge.utils.FinancialCalulable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "laboratory")
public class Laboratory extends BaseEntity implements FinancialCalulable {
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "grower_id", nullable = false, unique = true)
	private Grower grower;

	private BigDecimal operationCost;
	private BigDecimal operationFee;
	
	@Override
	public BigDecimal calculateFinancialValue() {
		return operationCost.multiply(operationFee);
	}
}