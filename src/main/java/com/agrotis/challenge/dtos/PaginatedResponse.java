package com.agrotis.challenge.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Envelope genérico de resposta paginada para APIs REST.
 *
 * @param <T> Tipo do conteúdo/DTO da resposta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Representação paginada genérica de dados da API")
public record PaginatedResponse<T>(

    @Schema(description = "Lista de elementos da página atual")
    List<T> content,

    @Schema(description = "Número da página atual (0-indexed)", example = "0")
    int pageNumber,

    @Schema(description = "Quantidade máxima de elementos por página", example = "20")
    int pageSize,

    @Schema(description = "Total absoluto de elementos em todas as páginas", example = "105")
    long totalElements,

    @Schema(description = "Total de páginas disponíveis", example = "6")
    int totalPages,

    @Schema(description = "Indica se a página atual é a primeira", example = "true")
    boolean isFirst,

    @Schema(description = "Indica se a página atual é a última", example = "false")
    boolean isLast,

    @Schema(description = "Indica se existe uma próxima página", example = "true")
    boolean hasNext,

    @Schema(description = "Indica se existe uma página anterior", example = "false")
    boolean hasPrevious
) {

    /**
     * Cria um PaginatedResponse a partir de um Page do Spring Data sem transformação de tipo.
     *
     * @param page Objeto Page original
     * @param <T>  Tipo do conteúdo
     * @return PaginatedResponse preenchido
     */
    public static <T> PaginatedResponse<T> from(Page<T> page) {
        Objects.requireNonNull(page, "O objeto Page não pode ser nulo.");
        return new PaginatedResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious()
        );
    }

    /**
     * Cria um PaginatedResponse a partir de um Page do Spring Data aplicando um mapper/conversor de tipo (Entidade -> DTO).
     *
     * @param page   Objeto Page contendo o tipo de origem (ex: Entidade)
     * @param mapper Função de mapeamento para o tipo de destino (ex: DTO)
     * @param <U>    Tipo de origem
     * @param <T>    Tipo de destino
     * @return PaginatedResponse do tipo mapeado
     */
    public static <U, T> PaginatedResponse<T> from(Page<U> page, Function<U, T> mapper) {
        Objects.requireNonNull(page, "O objeto Page não pode ser nulo.");
        Objects.requireNonNull(mapper, "A função de mapeamento (mapper) não pode ser nula.");
        
        Page<T> mappedPage = page.map(mapper);
        return from(mappedPage);
    }
}