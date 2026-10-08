package com.agrotis.challenge.dtos.mappers;

import org.springframework.stereotype.Component;

import com.agrotis.challenge.dtos.LaboratoryDTO;
import com.agrotis.challenge.dtos.LaboratoryRequestDTO;
import com.agrotis.challenge.entities.Laboratory;

@Component
public class LaboratoryMapper implements BaseMapper<Laboratory, LaboratoryDTO, LaboratoryRequestDTO> {

	@Override
	public LaboratoryDTO toDTO(Laboratory entity) {
		if (entity == null)
			return null;

		LaboratoryDTO dto = new LaboratoryDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setRegistration(entity.getRegistration());
		dto.setAddress(entity.getAddress());

		dto.setOperationCost(entity.getOperationCost());
		dto.setOperationFee(entity.getOperationFee());
		dto.setCalculatedValue(entity.calculateFinancialValue());

		return dto;
	}

	@Override
	public Laboratory toEntity(LaboratoryRequestDTO request) {
		if (request == null)
			return null;

		Laboratory entity = new Laboratory();
		entity.setName(request.getName());
		entity.setRegistration(request.getRegistration());
		entity.setAddress(request.getAddress());

		entity.setOperationCost(request.getOperationCost());
		entity.setOperationFee(request.getOperationFee());

		return entity;
	}

	@Override
	public void updateEntityFromDTO(LaboratoryRequestDTO request, Laboratory entity) {
		if (request == null || entity == null)
			return;

		entity.setName(request.getName());
		entity.setRegistration(request.getRegistration());
		entity.setAddress(request.getAddress());

		entity.setOperationCost(request.getOperationCost());
		entity.setOperationFee(request.getOperationFee());
	}
}