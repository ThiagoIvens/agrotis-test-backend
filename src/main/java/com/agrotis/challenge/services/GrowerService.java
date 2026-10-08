package com.agrotis.challenge.services;

import java.util.Objects;
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
import com.agrotis.challenge.entities.Grower;
import com.agrotis.challenge.repositories.GrowerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GrowerService {

	private final GrowerRepository growerRepository = null;
	private final GrowerMapper growerMapper = new GrowerMapper();

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
		Grower saved = growerRepository.save(entity);
		return growerMapper.toDTO(saved);
	}

	@Transactional
	public GrowerDTO update(@NonNull UUID id, GrowerRequestDTO request) {
		Grower entity = Objects.requireNonNull(growerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + id)));

		growerMapper.updateEntityFromDTO(request, entity);
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
}
