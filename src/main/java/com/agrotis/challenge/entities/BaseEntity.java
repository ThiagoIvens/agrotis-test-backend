package com.agrotis.challenge.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank(message = "Name is required.")
	private String name;

	@NotBlank(message = "Registration is required")
	@Column(unique = true)
	private String registration;

	@NotBlank(message = "Address is required")
	private String address;
}