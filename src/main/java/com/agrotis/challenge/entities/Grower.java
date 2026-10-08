package com.agrotis.challenge.entities;

import java.math.BigDecimal;
import java.util.List;

import com.agrotis.challenge.utils.FinancialCalulable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "grower")
public class Grower extends BaseEntity implements FinancialCalulable {
	private static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "grower", fetch = FetchType.LAZY)
	private Laboratory laboratory;

	@OneToMany(mappedBy = "grower")
	private List<Farmstead> farmsteads;
	
	private BigDecimal production;
	private BigDecimal commissionRate;
	
	@Override
	public BigDecimal calculateFinancialValue() {
		if(production == null || commissionRate == null) {
			return BigDecimal.ZERO;
		}
		return production.multiply(commissionRate);
	}
}