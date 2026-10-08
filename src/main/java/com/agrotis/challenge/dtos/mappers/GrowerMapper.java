package com.agrotis.challenge.dtos.mappers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.agrotis.challenge.dtos.GrowerDTO;
import com.agrotis.challenge.dtos.GrowerRequestDTO;
import com.agrotis.challenge.entities.Grower;

@Component
public class GrowerMapper implements BaseMapper<Grower, GrowerDTO, GrowerRequestDTO> {

	@Override
	public GrowerDTO toDTO(Grower entity) {
		if (entity == null)
			return null;

		GrowerDTO dto = new GrowerDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setRegistration(entity.getRegistration());
		dto.setAddress(entity.getAddress());

		dto.setOperationInitialDate(entity.getOperationInitialDate());
		dto.setOperationFinalDate(entity.getOperationFinalDate());
		dto.setObservations(entity.getObservations());
		dto.setProduction(entity.getProduction());
		dto.setCommissionRate(entity.getCommissionRate());
		dto.setCalculatedValue(entity.calculateFinancialValue());

		if (entity.getLaboratory() != null) {
			dto.setLaboratoryId(entity.getLaboratory().getId());
			dto.setLaboratoryName(entity.getLaboratory().getName());
		}

		if (entity.getFarmsteads() != null) {
			List<UUID> farmsteadIds = entity.getFarmsteads().stream()
					.filter(Objects::nonNull)
					.map(farmstead -> farmstead.getId())
					.toList();
			dto.setFarmsteadIds(farmsteadIds);
		}

		return dto;
	}

	@Override
	public Grower toEntity(GrowerRequestDTO request) {
		if (request == null)
			return null;

		Grower entity = new Grower();
		entity.setName(request.getName());
		entity.setRegistration(request.getRegistration());
		entity.setAddress(request.getAddress());

		entity.setOperationInitialDate(request.getOperationInitialDate());
		entity.setOperationFinalDate(request.getOperationFinalDate());
		entity.setObservations(request.getObservations());
		entity.setProduction(request.getProduction());
		entity.setCommissionRate(request.getCommissionRate());

		return entity;
	}

	@Override
	public void updateEntityFromDTO(GrowerRequestDTO request, Grower entity) {
		if (request == null || entity == null)
			return;

		entity.setName(request.getName());
		entity.setRegistration(request.getRegistration());
		entity.setAddress(request.getAddress());

		entity.setOperationInitialDate(request.getOperationInitialDate());
		entity.setOperationFinalDate(request.getOperationFinalDate());
		entity.setObservations(request.getObservations());
		entity.setProduction(request.getProduction());
		entity.setCommissionRate(request.getCommissionRate());
	}
}