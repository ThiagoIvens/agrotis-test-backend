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
		    WHERE grower.operationInitialDate >= :initialDateInit
		      AND grower.operationInitialDate <= :initialDateEnd
		      AND grower.operationFinalDate   >= :finalDateInit
		      AND grower.operationFinalDate   <= :finalDateEnd
		      AND LOWER(CAST(grower.observations AS string)) LIKE :searchPattern
		    GROUP BY lab.id, lab.name, lab.operationCost, lab.operationFee
		    HAVING COUNT(DISTINCT grower.id) >= :minGrowerQty
		    ORDER BY
		        COUNT(DISTINCT grower.id) DESC,
		        MIN(grower.operationInitialDate) ASC
		""")
		List<LaboratoryReportResponseDTO> generateLaboratoryReport(
		        @Param("initialDateInit") LocalDate initialDateInit,
		        @Param("initialDateEnd") LocalDate initialDateEnd,
		        @Param("finalDateInit") LocalDate finalDateInit,
		        @Param("finalDateEnd") LocalDate finalDateEnd,
		        @Param("searchPattern") String searchPattern,
		        @Param("minGrowerQty") Long minGrowerQty);
}
