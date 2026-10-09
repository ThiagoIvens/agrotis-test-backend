package com.agrotis.challenge.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.agrotis.challenge.entities.Grower;

public interface GrowerRepository extends JpaRepository<Grower, UUID> {

	@EntityGraph(attributePaths = { "laboratory" })
	@NonNull
	Page<Grower> findAll(@NonNull Pageable pageable);

	@Query("SELECT DISTINCT g FROM Grower g " +
	           "LEFT JOIN g.laboratory l " +
	           "LEFT JOIN g.farmsteads f " +
	           "WHERE (:search IS NULL OR :search = '' OR " +
	           "LOWER(g.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
	           "LOWER(g.registration) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<Grower> findAllWithRelations(@Param("search") String search, Pageable pageable);

	@Query("SELECT g FROM Grower g LEFT JOIN FETCH g.farmsteads LEFT JOIN FETCH g.laboratory WHERE g.id = :id")
	Optional<Grower> findByIdWithRelations(@Param("id") UUID id);
}
