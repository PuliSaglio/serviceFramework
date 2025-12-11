package br.com.serviceframework.domain.converter;

import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CategoriaConverter implements AttributeConverter<ICategoriaServicos, Long> {

    // 1. Convertendo Objeto para Banco de Dados (DB)
    @Override
    public Long convertToDatabaseColumn(ICategoriaServicos attribute) {
        if (attribute == null) {
            return null;
        }
        // O JPA salva apenas o ID Long no campo 'categoria_id'
        return attribute.getIdCategoria();
    }

    // 2. Convertendo Banco de Dados (DB) para Objeto
    @Override
    public ICategoriaServicos convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }

        // O Core não sabe qual classe concreta de categoria criar (CategoriaVendas, CategoriaDomestico).

        // Retorna uma implementação Dummy/Proxy que só contém o ID.
        // A aplicação fará a injeção ou busca real do nome se precisar.
        return new ICategoriaServicos() {
            @Override
            public Long getIdCategoria() {
                return dbData;
            }
            @Override
            public String getNomeCategoria() {
                // Nome não disponível na camada Core!
                return "ID: " + dbData;
            }
        };
    }
}
