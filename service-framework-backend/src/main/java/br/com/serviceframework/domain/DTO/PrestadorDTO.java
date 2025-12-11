package br.com.serviceframework.domain.DTO;

public record PrestadorDTO(
        Long id,
        Boolean active,
        Long userId,
        String nome,
        String email,
        String biografia
) {
}
