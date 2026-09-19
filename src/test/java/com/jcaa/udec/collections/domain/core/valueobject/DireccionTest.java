package com.jcaa.udec.collections.domain.core.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DireccionTest {
    private static final String DIRECCION_VALIDO = "Calle 30 # 17-45";

    @Test
    void deberiaCrearDireccionValida() {
        // Arrange
        // Act
        Direccion direccion = new Direccion(DIRECCION_VALIDO);

        // Assert
        assertThat(direccion.valor()).isEqualTo(DIRECCION_VALIDO);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "     ", "Cll"})
    void deberiaRechazarDireccionInvalida(String valor) {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> new Direccion(valor))
                .isInstanceOf(ClienteInvalidoException.class);
    }
}
