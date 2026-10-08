package com.agrotis.challenge.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agrotis.challenge.dtos.LaboratoryReportResponseDTO;
import com.agrotis.challenge.entities.Laboratory;

public interface LaboratoryRepository extends JpaRepository<Laboratory, UUID> {

	@Query("""
			SELECT
			    lab.id AS laboratoryCode,
			    UPPER(lab.name) AS laboratoryName,
			    COUNT(DISTINCT grower.id) AS totalLinkedGrowers,
			    (lab.operationCost * lab.operationFee) AS financialValueCalculated
			FROM Laboratory lab
			JOIN lab.growers grower
			WHERE (:initialDateInit IS NULL OR grower.operationInitialDate >= :initialDateInit)
			  AND (:initialDateEnd IS NULL OR grower.operationInitialDate <= :initialDateEnd)
			  AND (:finalDateInit IS NULL OR grower.operationFinalDate >= :finalDateInit)
			  AND (:finalDateEnd IS NULL OR grower.operationFinalDate <= :finalDateEnd)
			  AND (:search IS NULL OR LOWER(grower.observations) LIKE LOWER(CONCAT('%', :search, '%')))
			GROUP BY lab.id, lab.name, lab.operationCost, lab.operationFee
			HAVING COUNT(DISTINCT grower.id) >= :minGrowerQty
			ORDER BY
			    COUNT(DISTINCT grower.id) DESC,
			    CASE
			        WHEN (:initialDateInit IS NOT NULL OR :initialDateEnd IS NOT NULL)
			        THEN MIN(grower.operationInitialDate)
			        ELSE NULL
			    END ASC
			""")
	List<LaboratoryReportResponseDTO> generateLaboratoryReport(
			@Param("initialDateInit") LocalDate initialDateInit,
			@Param("initialDateEnd") LocalDate initialDateEnd,
			@Param("finalDateInit") LocalDate finalDateInit,
			@Param("finalDateEnd") LocalDate finalDateEnd,
			@Param("search") String search,
			@Param("minGrowerQty") Long minGrowerQty);
}
