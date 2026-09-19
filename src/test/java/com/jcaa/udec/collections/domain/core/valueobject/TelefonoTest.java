package com.jcaa.udec.collections.domain.core.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TelefonoTest {
    private static final String TELEFONO_VALIDO = "3001234567";

    @Test
    void deberiaCrearTelefonoValido() {
        // Arrange
        // Act
        Telefono telefono = new Telefono(TELEFONO_VALIDO);

        // Assert
        assertThat(telefono.valor()).isEqualTo(TELEFONO_VALIDO);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "123456", "30012345678", "300-123456", "300abc4567"})
    void deberiaRechazarTelefonoInvalido(String valor) {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> new Telefono(valor))
                .isInstanceOf(ClienteInvalidoException.class);
    }
}
