package br.com.serviceframework.domain.converter;

import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CategoriaConverter implements AttributeConverter<ICategoriaServicos, String> {

    // 1. Convertendo Objeto para Banco de Dados (DB)
    @Override
    public String convertToDatabaseColumn(ICategoriaServicos attribute) {
        if (attribute == null) {
            return null;
        }
        Long id = attribute.getIdCategoria();
        // Armazena "id|nome" quando id existir, mantendo compatibilidade com o formato antigo (apenas nome)
        return id != null ? id + "|" + attribute.getNomeCategoria() : attribute.getNomeCategoria();
    }

    // 2. Convertendo Banco de Dados (DB) para Objeto
    @Override
    public ICategoriaServicos convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        // O Core não sabe qual classe concreta de categoria criar (CategoriaVendas, CategoriaDomestico),
        // então devolve um proxy simples com nome e, se disponível, id (persistido como "id|nome").
        return new ICategoriaServicos() {
            private final String nome;
            private final Long id;
            {
                String[] partes = dbData.split("\\|", 2);
                if (partes.length == 2) {
                    nome = partes[1];
                    Long idConvertido;
                    try {
                        idConvertido = Long.valueOf(partes[0]);
                    } catch (NumberFormatException e) {
                        idConvertido = null;
                    }
                    id = idConvertido;
                } else {
                    nome = dbData;
                    id = null;
                }
            }

            @Override
            public Long getIdCategoria() {
                return id;
            }
            @Override
            public String getNomeCategoria() {
                return nome;
            }
        };
    }
}
