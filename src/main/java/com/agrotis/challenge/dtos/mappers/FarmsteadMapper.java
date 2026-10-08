package com.agrotis.challenge.dtos.mappers;

import org.springframework.stereotype.Component;

import com.agrotis.challenge.dtos.FarmsteadDTO;
import com.agrotis.challenge.dtos.FarmsteadRequestDTO;
import com.agrotis.challenge.entities.Farmstead;

@Component
public class FarmsteadMapper implements BaseMapper<Farmstead, FarmsteadDTO, FarmsteadRequestDTO> {

	@Override
	public FarmsteadDTO toDTO(Farmstead entity) {
		if (entity == null)
			return null;

		FarmsteadDTO dto = new FarmsteadDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setRegistration(entity.getRegistration());
		dto.setAddress(entity.getAddress());

		dto.setTotalAreaInHectares(entity.getTotalAreaInHectares());
		dto.setTaxPerHectare(entity.getTaxPerHectare());
		dto.setCalculatedValue(entity.calculateFinancialValue());

		if (entity.getGrower() != null) {
			dto.setGrowerId(entity.getGrower().getId());
			dto.setGrowerName(entity.getGrower().getName());
		}

		return dto;
	}

	@Override
	public Farmstead toEntity(FarmsteadRequestDTO request) {
		if (request == null)
			return null;

		Farmstead entity = new Farmstead();
		entity.setName(request.getName());
		entity.setRegistration(request.getRegistration());
		entity.setAddress(request.getAddress());

		entity.setTotalAreaInHectares(request.getTotalAreaInHectares());
		entity.setTaxPerHectare(request.getTaxPerHectare());

		return entity;
	}

	@Override
	public void updateEntityFromDTO(FarmsteadRequestDTO request, Farmstead entity) {
		if (request == null || entity == null)
			return;

		entity.setName(request.getName());
		entity.setRegistration(request.getRegistration());
		entity.setAddress(request.getAddress());

		entity.setTotalAreaInHectares(request.getTotalAreaInHectares());
		entity.setTaxPerHectare(request.getTaxPerHectare());
	}
}