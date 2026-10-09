package com.agrotis.challenge.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agrotis.challenge.dtos.FarmsteadDTO;
import com.agrotis.challenge.dtos.FarmsteadRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.mappers.FarmsteadMapper;
import com.agrotis.challenge.entities.Farmstead;
import com.agrotis.challenge.repositories.FarmsteadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FarmsteadService {

	private final FarmsteadRepository farmsteadRepository;
	private final FarmsteadMapper farmsteadMapper = new FarmsteadMapper();

	@Transactional(readOnly = true)
	public PaginatedResponse<FarmsteadDTO> getAll(@NonNull Pageable pageable) {
		Page<Farmstead> page = farmsteadRepository.findAll(pageable);

		return PaginatedResponse.from(page, farmsteadMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public FarmsteadDTO findById(@NonNull UUID id) {
		Farmstead entity = farmsteadRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Propriedade rural não encontrada com o ID: " + id));
		return farmsteadMapper.toDTO(entity);
	}

	@Transactional
	public FarmsteadDTO create(FarmsteadRequestDTO request) {
		Farmstead entity = Objects.requireNonNull(farmsteadMapper.toEntity(request));
		Farmstead saved = farmsteadRepository.save(entity);
		return farmsteadMapper.toDTO(saved);
	}

	@Transactional
	public FarmsteadDTO update(@NonNull UUID id, FarmsteadRequestDTO request) {
		Farmstead entity = Objects.requireNonNull(farmsteadRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Propriedade rural não encontrada com o ID: " + id)));

		farmsteadMapper.updateEntityFromDTO(request, entity);
		Farmstead updated = farmsteadRepository.save(entity);
		return farmsteadMapper.toDTO(updated);
	}

	@Transactional
	public void delete(@NonNull UUID id) {
		if (!farmsteadRepository.existsById(id)) {
			throw new EntityNotFoundException("Propriedade rural não encontrada com o ID: " + id);
		}
		farmsteadRepository.deleteById(id);
	}
}
