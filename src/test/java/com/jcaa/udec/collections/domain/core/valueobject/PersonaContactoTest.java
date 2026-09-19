package com.jcaa.udec.collections.domain.core.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PersonaContactoTest {
    private static final String PERSONA_CONTACTO_VALIDO = "Laura Gomez";

    @Test
    void deberiaCrearPersonaContactoValido() {
        // Arrange
        // Act
        PersonaContacto personaContacto = new PersonaContacto(PERSONA_CONTACTO_VALIDO);

        // Assert
        assertThat(personaContacto.valor()).isEqualTo(PERSONA_CONTACTO_VALIDO);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "La"})
    void deberiaRechazarPersonaContactoInvalido(String valor) {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> new PersonaContacto(valor))
                .isInstanceOf(ClienteInvalidoException.class);
    }
}
