package com.agrotis.challenge.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agrotis.challenge.entities.Farmstead;

public interface FarmsteadRepository extends JpaRepository<Farmstead,UUID> {}