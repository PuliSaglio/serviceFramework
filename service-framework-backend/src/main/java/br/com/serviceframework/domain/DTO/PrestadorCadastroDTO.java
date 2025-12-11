package br.com.serviceframework.domain.DTO;

public record PrestadorCadastroDTO(
        String nome,
        String email,
        String senha,
        String telefone,
        String descricao,
        String cpfCnpj
) {
}
