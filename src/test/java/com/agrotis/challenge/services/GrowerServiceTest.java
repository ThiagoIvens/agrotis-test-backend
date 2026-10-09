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

import com.agrotis.challenge.dtos.GrowerDTO;
import com.agrotis.challenge.dtos.GrowerRequestDTO;
import com.agrotis.challenge.dtos.PaginatedResponse;
import com.agrotis.challenge.dtos.mappers.GrowerMapper;
import com.agrotis.challenge.entities.Grower;
import com.agrotis.challenge.repositories.GrowerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrowerServiceTest {

	@Mock
	private GrowerRepository growerRepository;

	@Mock
	private GrowerMapper growerMapper;

	@InjectMocks
	private GrowerService growerService;

	private UUID growerId;
	private Grower grower;
	private GrowerRequestDTO requestDTO;
	private GrowerDTO responseDTO;

	@BeforeEach
	void setUp() {
		growerId = UUID.randomUUID();
		grower = new Grower();
        grower.setId(growerId);
        requestDTO = new GrowerRequestDTO();
        responseDTO = new GrowerDTO();
	}

	@Test
	@DisplayName("Deve retornar uma lista paginada de produtores com sucesso")
	void getAll_ShouldReturnPaginatedResponse() {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10);
		Page<Grower> growerPage = new PageImpl<>(List.of(grower), pageable, 1);

		when(growerRepository.findAllWithRelations(pageable)).thenReturn(growerPage);
		when(growerMapper.toDTO(any(Grower.class))).thenReturn(responseDTO);

		// Act
		PaginatedResponse<GrowerDTO> result = growerService.getAll(pageable);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1);
		verify(growerRepository, times(1)).findAllWithRelations(pageable);
	}

	@Test
	@DisplayName("Deve retornar o DTO do produtor quando o ID existir")
	void findById_ShouldReturnDto_WhenIdExists() {
		// Arrange
		responseDTO.setId(growerId);
		
		when(growerRepository.findByIdWithRelations(growerId)).thenReturn(Optional.of(grower));
		when(growerMapper.toDTO(grower)).thenReturn(responseDTO);

		// Act
		GrowerDTO result = growerService.findById(growerId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(growerId);
		verify(growerRepository, times(1)).findByIdWithRelations(growerId);
	}

	@Test
	@DisplayName("Deve lançar EntityNotFoundException quando o ID do produtor não for encontrado")
	void findById_ShouldThrowException_WhenIdNotFound() {
		// Arrange
		when(growerRepository.findByIdWithRelations(growerId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> growerService.findById(growerId))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("Produtor não encontrado com o ID: " + growerId);

		verify(growerRepository, times(1)).findByIdWithRelations(growerId);
	}

	@Test
	@DisplayName("Deve persistir e retornar o DTO do produtor criado com sucesso")
	void create_ShouldPersistAndReturnDto() {
		// Arrange
		when(growerMapper.toEntity(requestDTO)).thenReturn(grower);
		when(growerRepository.save(any(Grower.class))).thenReturn(grower);
		when(growerMapper.toDTO(grower)).thenReturn(responseDTO);

		// Act
		GrowerDTO result = growerService.create(requestDTO);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo(requestDTO.getName());
		verify(growerRepository, times(1)).save(any(Grower.class));
	}

	@Test
	@DisplayName("Deve atualizar e retornar o DTO quando o produtor existir")
	void update_ShouldModifyAndReturnDto_WhenExists() {
		// Arrange
		when(growerRepository.findById(growerId)).thenReturn(Optional.of(grower));
		when(growerRepository.save(any(Grower.class))).thenReturn(grower);
		when(growerMapper.toDTO(grower)).thenReturn(responseDTO);

		// Act
		GrowerDTO result = growerService.update(growerId, requestDTO);

		// Assert
		assertThat(result).isNotNull();
		verify(growerRepository, times(1)).findById(growerId);
		verify(growerRepository, times(1)).save(any(Grower.class));
	}

	@Test
	@DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar um produtor inexistente")
	void update_ShouldThrowException_WhenNotFound() {
		// Arrange
		when(growerRepository.findById(growerId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> growerService.update(growerId, requestDTO))
				.isInstanceOf(EntityNotFoundException.class);

		verify(growerRepository, never()).save(any(Grower.class));
	}

	@Test
	@DisplayName("Deve remover o produtor com sucesso quando o ID existir")
	void delete_ShouldRemoveEntity_WhenExists() {
		// Arrange
		when(growerRepository.existsById(growerId)).thenReturn(true);
		doNothing().when(growerRepository).deleteById(growerId);

		// Act
		growerService.delete(growerId);

		// Assert
		verify(growerRepository, times(1)).existsById(growerId);
		verify(growerRepository, times(1)).deleteById(growerId);
	}

	@Test
	@DisplayName("Deve lançar EntityNotFoundException ao tentar deletar um produtor inexistente")
	void delete_ShouldThrowException_WhenNotFound() {
		// Arrange
		when(growerRepository.existsById(growerId)).thenReturn(false);

		// Act & Assert
		assertThatThrownBy(() -> growerService.delete(growerId))
				.isInstanceOf(EntityNotFoundException.class);

		verify(growerRepository, never()).deleteById(any());
	}
}