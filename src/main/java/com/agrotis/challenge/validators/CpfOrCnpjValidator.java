package com.agrotis.challenge.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfOrCnpjValidator implements ConstraintValidator<CpfOrCnpj, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Permite valores nulos/vazios. Se for obrigatório, use @NotNull/@NotBlank no
		// DTO.
		if (value == null || value.trim().isEmpty()) {
			return true;
		}

		// Remove caracteres não numéricos
		String cleanValue = value.replaceAll("\\D", "");

		if (cleanValue.length() == 11) {
			return isValidCPF(cleanValue);
		} else if (cleanValue.length() == 14) {
			return isValidCNPJ(cleanValue);
		}

		return false;
	}

	private boolean isValidCPF(String cpf) {
		// Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
		if (cpf.matches("(\\d)\\1{10}"))
			return false;

		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += (cpf.charAt(i) - '0') * (10 - i);
		}
		int dig1 = 11 - (sum % 11);
		if (dig1 > 9)
			dig1 = 0;

		sum = 0;
		for (int i = 0; i < 10; i++) {
			sum += (cpf.charAt(i) - '0') * (11 - i);
		}
		int dig2 = 11 - (sum % 11);
		if (dig2 > 9)
			dig2 = 0;

		return (cpf.charAt(9) - '0' == dig1) && (cpf.charAt(10) - '0' == dig2);
	}

	private boolean isValidCNPJ(String cnpj) {
		// Verifica se todos os dígitos são iguais
		if (cnpj.matches("(\\d)\\1{13}"))
			return false;

		int[] weight1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		int sum = 0;
		for (int i = 0; i < 12; i++) {
			sum += (cnpj.charAt(i) - '0') * weight1[i];
		}
		int dig1 = 11 - (sum % 11);
		if (dig1 > 9)
			dig1 = 0;

		int[] weight2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		sum = 0;
		for (int i = 0; i < 13; i++) {
			sum += (cnpj.charAt(i) - '0') * weight2[i];
		}
		int dig2 = 11 - (sum % 11);
		if (dig2 > 9)
			dig2 = 0;

		return (cnpj.charAt(12) - '0' == dig1) && (cnpj.charAt(13) - '0' == dig2);
	}
}