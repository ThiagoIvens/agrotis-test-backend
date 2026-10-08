package com.agrotis.challenge.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agrotis.challenge.entities.Farmstead;

@Repository
public interface FarmsteadRepository extends JpaRepository<Farmstead,UUID> {

}