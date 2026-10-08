package com.agrotis.challenge.dtos.mappers;

import java.util.List;

public interface BaseMapper<E, DTO, REQ> {
    DTO toDTO(E entity);
    E toEntity(REQ request);
    void updateEntityFromDTO(REQ request, E entity);

    default List<DTO> toDTOList(List<E> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }
}