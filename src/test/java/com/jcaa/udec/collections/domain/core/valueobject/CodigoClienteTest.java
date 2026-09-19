package com.jcaa.udec.collections.domain.core.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CodigoClienteTest {
    private static final String CODIGO_VALIDO = "CLI001";

    @Test
    void deberiaCrearCodigoValido() {
        // Arrange
        // Act
        CodigoCliente codigoCliente = new CodigoCliente(CODIGO_VALIDO);

        // Assert
        assertThat(codigoCliente.valor()).isEqualTo(CODIGO_VALIDO);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "AB", "CLIENTE0001", "CLI-01", "CLI 01"})
    void deberiaRechazarCodigoInvalido(String valor) {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> new CodigoCliente(valor))
                .isInstanceOf(ClienteInvalidoException.class);
    }
}
