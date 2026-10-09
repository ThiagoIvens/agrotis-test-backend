package com.agrotis.challenge.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agrotis.challenge.dtos.GrowerDTO;
import com.agrotis.challenge.dtos.GrowerRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.mappers.GrowerMapper;
import com.agrotis.challenge.entities.Farmstead;
import com.agrotis.challenge.entities.Grower;
import com.agrotis.challenge.entities.Laboratory;
import com.agrotis.challenge.repositories.FarmsteadRepository;
import com.agrotis.challenge.repositories.GrowerRepository;
import com.agrotis.challenge.repositories.LaboratoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GrowerService {

	private final GrowerRepository growerRepository;
	private final LaboratoryRepository laboratoryRepository;
	private final FarmsteadRepository farmsteadRepository;
	private final GrowerMapper growerMapper;

	@Transactional(readOnly = true)
	public PaginatedResponse<GrowerDTO> getAll(@NonNull Pageable pageable) {
		Page<Grower> page = growerRepository.findAll(pageable);

		return PaginatedResponse.from(page, growerMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public GrowerDTO findById(@NonNull UUID id) {
		Grower entity = growerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + id));
		return growerMapper.toDTO(entity);
	}

	@Transactional
	public GrowerDTO create(GrowerRequestDTO request) {
		Grower entity = Objects.requireNonNull(growerMapper.toEntity(request));
		applyRelations(entity, request);
		Grower saved = growerRepository.save(entity);
		return growerMapper.toDTO(saved);
	}

	@Transactional
	public GrowerDTO update(@NonNull UUID id, GrowerRequestDTO request) {
		Grower entity = Objects.requireNonNull(growerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + id)));

		growerMapper.updateEntityFromDTO(request, entity);
		applyRelations(entity, request);
		Grower updated = growerRepository.save(entity);
		return growerMapper.toDTO(updated);
	}

	@Transactional
	public void delete(@NonNull UUID id) {
		if (!growerRepository.existsById(id)) {
			throw new EntityNotFoundException("Produtor não encontrado com o ID: " + id);
		}
		growerRepository.deleteById(id);
	}

	private void applyRelations(Grower entity, GrowerRequestDTO request) {
		UUID laboratoryId = request.getLaboratoryId();
		if (laboratoryId != null) {
			Laboratory lab = laboratoryRepository.findById(laboratoryId)
					.orElseThrow(() -> new EntityNotFoundException(
							"Laboratório não encontrado com o ID: " + request.getLaboratoryId()));
			entity.setLaboratory(lab);
		} else {
			entity.setLaboratory(null);
		}

		if (request.getFarmsteadIds() != null) {
			Set<UUID> ids = new HashSet<>(request.getFarmsteadIds());
			List<Farmstead> farmsteads = farmsteadRepository.findAllById(ids);
			if (farmsteads.size() != ids.size()) {
				throw new EntityNotFoundException("Uma ou mais fazendas não foram encontradas");
			}
			entity.setFarmsteads(new ArrayList<>(farmsteads));
		}
	}
}
