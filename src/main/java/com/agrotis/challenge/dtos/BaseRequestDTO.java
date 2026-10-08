package com.agrotis.challenge.dtos;

import com.agrotis.challenge.validators.CpfOrCnpj;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseRequestDTO {
	@NotBlank(message = "O nome é obrigatório.")
	private String name;

	@NotBlank(message = "A inscrição fiscal é obrigatória.")
	@CpfOrCnpj
	private String registration;

	@NotBlank(message = "O endereço é obrigatório.")
	private String address;
}