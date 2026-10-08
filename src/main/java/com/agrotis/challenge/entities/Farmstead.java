package com.agrotis.challenge.entities;

import java.math.BigDecimal;

import com.agrotis.challenge.utils.FinancialCalulable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "farmstead")
public class Farmstead extends BaseEntity implements FinancialCalulable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "grower_id", nullable = false)
	private Grower grower;
	
	private BigDecimal totalAreaInHectares;
	private BigDecimal taxPerHectare;
	
	@Override
	public BigDecimal calculateFinancialValue() {
		return totalAreaInHectares.multiply(taxPerHectare);
	}
}