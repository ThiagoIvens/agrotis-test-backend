package com.agrotis.challenge.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.agrotis.challenge.dtos.FarmsteadDTO;
import com.agrotis.challenge.dtos.FarmsteadRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.mappers.FarmsteadMapper;
import com.agrotis.challenge.entities.Farmstead;
import com.agrotis.challenge.repositories.FarmsteadRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FarmsteadServiceTest {

	@Mock
	private FarmsteadRepository farmsteadRepository;

	@Mock
	private FarmsteadMapper farmsteadMapper;

	@InjectMocks
	private FarmsteadService farmsteadService;

	private UUID farmsteadId;
	private Farmstead farmstead;
	private FarmsteadRequestDTO requestDTO;
	private FarmsteadDTO responseDTO;

	@BeforeEach
	void setUp() {
		farmsteadId = UUID.randomUUID();
		farmstead = new Farmstead();
		farmstead.setId(farmsteadId);
		requestDTO = new FarmsteadRequestDTO();
		responseDTO = new FarmsteadDTO();
	}

	@Test
	@DisplayName("Deve retornar uma lista paginada de propriedades rurais com sucesso")
	void getAll_ShouldReturnPaginatedResponse() {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10);
		Page<Farmstead> farmsteadPage = new PageImpl<>(List.of(farmstead), pageable, 1);

		when(farmsteadRepository.findAll(pageable)).thenReturn(farmsteadPage);
		when(farmsteadMapper.toDTO(any(Farmstead.class))).thenReturn(responseDTO);

		// Act
		PaginatedResponse<FarmsteadDTO> result = farmsteadService.getAll(pageable);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1);
		verify(farmsteadRepository, times(1)).findAll(pageable);
	}

	@Test
	@DisplayName("Deve retornar o DTO da propriedade rural quando o ID existir")
	void findById_ShouldReturnDto_WhenIdExists() {
		// Arrange
		responseDTO.setId(farmsteadId);
		
		when(farmsteadRepository.findById(farmsteadId)).thenReturn(Optional.of(farmstead));
		when(farmsteadMapper.toDTO(farmstead)).thenReturn(responseDTO);

		// Act
		FarmsteadDTO result = farmsteadService.findById(farmsteadId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(farmsteadId);
		verify(farmsteadRepository, times(1)).findById(farmsteadId);
	}

	@Test
	@DisplayName("Deve lançar EntityNotFoundException quando o ID da propriedade rural não for encontrado")
	void findById_ShouldThrowException_WhenIdNotFound() {
		// Arrange
		when(farmsteadRepository.findById(farmsteadId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> farmsteadService.findById(farmsteadId)).isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("Propriedade rural não encontrada com o ID: " + farmsteadId);

		verify(farmsteadRepository, times(1)).findById(farmsteadId);
	}

	@Test
	@DisplayName("Deve persistir e retornar o DTO da propriedade rural criado com sucesso")
	void create_ShouldPersistAndReturnDto() {
		// Arrange
		when(farmsteadMapper.toEntity(requestDTO)).thenReturn(farmstead);
		when(farmsteadRepository.save(any(Farmstead.class))).thenReturn(farmstead);
		when(farmsteadMapper.toDTO(farmstead)).thenReturn(responseDTO);

		// Act
		FarmsteadDTO result = farmsteadService.create(requestDTO);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo(requestDTO.getName());
		verify(farmsteadRepository, times(1)).save(any(Farmstead.class));
	}

	@Test
	@DisplayName("Deve atualizar e retornar o DTO quando a propriedade rural existir")
	void update_ShouldModifyAndReturnDto_WhenExists() {
		// Arrange
		when(farmsteadRepository.findById(farmsteadId)).thenReturn(Optional.of(farmstead));
		when(farmsteadRepository.save(any(Farmstead.class))).thenReturn(farmstead);
		when(farmsteadMapper.toDTO(farmstead)).thenReturn(responseDTO);

		// Act
		FarmsteadDTO result = farmsteadService.update(farmsteadId, requestDTO);

		// Assert
		assertThat(result).isNotNull();
		verify(farmsteadRepository, times(1)).findById(farmsteadId);
		verify(farmsteadRepository, times(1)).save(any(Farmstead.class));
	}

	@Test
	@DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar uma propriedade rural inexistente")
	void update_ShouldThrowException_WhenNotFound() {
		// Arrange
		when(farmsteadRepository.findById(farmsteadId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> farmsteadService.update(farmsteadId, requestDTO))
				.isInstanceOf(EntityNotFoundException.class);

		verify(farmsteadRepository, never()).save(any(Farmstead.class));
	}

	@Test
	@DisplayName("Deve remover a propriedade rural com sucesso quando o ID existir")
	void delete_ShouldRemoveEntity_WhenExists() {
		// Arrange
		when(farmsteadRepository.existsById(farmsteadId)).thenReturn(true);
		doNothing().when(farmsteadRepository).deleteById(farmsteadId);

		// Act
		farmsteadService.delete(farmsteadId);

		// Assert
		verify(farmsteadRepository, times(1)).existsById(farmsteadId);
		verify(farmsteadRepository, times(1)).deleteById(farmsteadId);
	}

	@Test
	@DisplayName("Deve lançar EntityNotFoundException ao tentar deletar uma propriedade rural inexistente")
	void delete_ShouldThrowException_WhenNotFound() {
		// Arrange
		when(farmsteadRepository.existsById(farmsteadId)).thenReturn(false);

		// Act & Assert
		assertThatThrownBy(() -> farmsteadService.delete(farmsteadId)).isInstanceOf(EntityNotFoundException.class);

		verify(farmsteadRepository, never()).deleteById(any());
	}
}