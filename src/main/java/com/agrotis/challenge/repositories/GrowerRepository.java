package com.agrotis.challenge.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agrotis.challenge.entities.Grower;

public interface GrowerRepository extends JpaRepository<Grower,UUID> {

}
