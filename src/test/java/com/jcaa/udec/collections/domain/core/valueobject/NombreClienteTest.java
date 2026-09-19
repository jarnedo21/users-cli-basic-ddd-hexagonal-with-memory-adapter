package com.jcaa.udec.collections.domain.core.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NombreClienteTest {
    private static final String NOMBRE_VALIDO = "Moda Andina SAS";

    @Test
    void deberiaCrearNombreValido() {
        // Arrange
        // Act
        NombreCliente nombreCliente = new NombreCliente(NOMBRE_VALIDO);

        // Assert
        assertThat(nombreCliente.valor()).isEqualTo(NOMBRE_VALIDO);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "Mo"})
    void deberiaRechazarNombreInvalido(String valor) {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> new NombreCliente(valor))
                .isInstanceOf(ClienteInvalidoException.class);
    }
}
