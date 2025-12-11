package br.com.serviceframework.domain.DTO;

public record AuthResponseDTO(
        UserDTO user,
        String token
) {

}
