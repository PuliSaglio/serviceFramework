package br.com.serviceframework.domain.DTO;

public record ClienteDTO(
        Long id,
        Long userId,
        String nome,
        String email
) {
}
