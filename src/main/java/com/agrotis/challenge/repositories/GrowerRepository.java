package com.agrotis.challenge.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.agrotis.challenge.entities.Grower;

public interface GrowerRepository extends JpaRepository<Grower,UUID> {
	
	@EntityGraph(attributePaths = {"laboratory"})
	@NonNull
    Page<Grower> findAll(@NonNull Pageable pageable);
	
}
