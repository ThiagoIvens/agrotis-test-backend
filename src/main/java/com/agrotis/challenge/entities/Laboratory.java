package com.agrotis.challenge.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.agrotis.challenge.utils.FinancialCalulable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "laboratory")
public class Laboratory extends BaseEntity implements FinancialCalulable {
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
	private List<Grower> growers = new ArrayList<>();
	
	private BigDecimal operationCost;
	private BigDecimal operationFee;
	
	@Override
	public BigDecimal calculateFinancialValue() {
		if (operationCost == null || operationFee == null) {
			return BigDecimal.ZERO;
		}
		return operationCost.multiply(operationFee);
	}
}