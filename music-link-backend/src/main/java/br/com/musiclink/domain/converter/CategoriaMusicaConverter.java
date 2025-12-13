package br.com.musiclink.domain.converter;

import br.com.musiclink.enumerations.CategoriaMusica;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoriaMusicaConverter implements AttributeConverter<CategoriaMusica, Long> {

    @Override
    public Long convertToDatabaseColumn(CategoriaMusica attribute) {
        if (attribute == null) {
            return null;
        }
        // Salva o ID personalizado (1, 2, 999...) no banco
        return attribute.getIdCategoria();
    }

    @Override
    public CategoriaMusica convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        // Usa o seu método estático ofId para recuperar o Enum
        return CategoriaMusica.ofId(dbData);
    }
}