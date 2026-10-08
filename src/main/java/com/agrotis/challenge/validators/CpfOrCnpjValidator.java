package com.agrotis.challenge.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

public class CpfOrCnpjValidator implements ConstraintValidator<CpfOrCnpj, String> {

	private final CPFValidator cpfValidator = new CPFValidator();
	private final CNPJValidator cnpjValidator = new CNPJValidator();

	@Override
	public void initialize(CpfOrCnpj constraintAnnotation) {
		cpfValidator.initialize(null);
		cnpjValidator.initialize(null);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Permite valores nulos/vazios. Se for obrigatório, use @NotNull/@NotBlank no
		// DTO.
		if (value == null || value.trim().isEmpty()) {
			return true;
		}

		// Remove caracteres não numéricos para verificar o tamanho
		String cleanValue = value.replaceAll("\\D", "");

		if (cleanValue.length() == 11) {
			return cpfValidator.isValid(value, context);
		} else if (cleanValue.length() == 14) {
			return cnpjValidator.isValid(value, context);
		}

		return false;
	}
}