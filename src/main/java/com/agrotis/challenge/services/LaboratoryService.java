package com.agrotis.challenge.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agrotis.challenge.dtos.LaboratoryDTO;
import com.agrotis.challenge.dtos.LaboratoryReportResponseDTO;
import com.agrotis.challenge.dtos.LaboratoryRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.ReportFilterDTO;
import com.agrotis.challenge.dtos.mappers.LaboratoryMapper;
import com.agrotis.challenge.entities.Laboratory;
import com.agrotis.challenge.repositories.LaboratoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LaboratoryService {

	private final LaboratoryRepository laboratoryRepository;
	private final LaboratoryMapper laboratoryMapper;

	private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);
	private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

	@Transactional(readOnly = true)
	public List<LaboratoryReportResponseDTO> generateReport(ReportFilterDTO filter) {
		
	    String search = filter.getSearch();
	    String searchPattern = (search == null || search.isBlank())
	            ? "%"
	            : "%" + search.trim().toLowerCase() + "%";

	    return laboratoryRepository.generateLaboratoryReport(
	            orDefault(filter.getInitialDateInit(), MIN_DATE),
	            orDefault(filter.getInitialDateEnd(), MAX_DATE),
	            orDefault(filter.getFinalDateInit(), MIN_DATE),
	            orDefault(filter.getFinalDateEnd(), MAX_DATE),
	            searchPattern,
	            filter.getMinGrowerQty() != null ? filter.getMinGrowerQty() : 0L);
	}

	private LocalDate orDefault(LocalDate value, LocalDate defaultValue) {
	    return value != null ? value : defaultValue;
	}


	@Transactional(readOnly = true)
	public PaginatedResponse<LaboratoryDTO> getAll(@NonNull Pageable pageable) {
		Page<Laboratory> page = laboratoryRepository.findAll(pageable);

		return PaginatedResponse.from(page, laboratoryMapper::toDTO);
	}

	@Transactional(readOnly = true)
	public LaboratoryDTO findById(@NonNull UUID id) {
		Laboratory entity = laboratoryRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Laboratório não encontrado com o ID: " + id));
		return laboratoryMapper.toDTO(entity);
	}

	@Transactional
	public LaboratoryDTO create(LaboratoryRequestDTO request) {
		Laboratory entity = Objects.requireNonNull(laboratoryMapper.toEntity(request));
		Laboratory saved = laboratoryRepository.save(entity);
		return laboratoryMapper.toDTO(saved);
	}

	@Transactional
	public LaboratoryDTO update(@NonNull UUID id, LaboratoryRequestDTO request) {
		Laboratory entity = Objects.requireNonNull(laboratoryRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Laboratório não encontrado com o ID: " + id)));

		laboratoryMapper.updateEntityFromDTO(request, entity);
		Laboratory updated = laboratoryRepository.save(entity);
		return laboratoryMapper.toDTO(updated);
	}

	@Transactional
	public void delete(@NonNull UUID id) {
		if (!laboratoryRepository.existsById(id)) {
			throw new EntityNotFoundException("Laboratório não encontrado com o ID: " + id);
		}
		laboratoryRepository.deleteById(id);
	}
}
