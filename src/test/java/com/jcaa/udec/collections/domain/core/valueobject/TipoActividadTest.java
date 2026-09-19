package com.jcaa.udec.collections.domain.core.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TipoActividadTest {

    @ParameterizedTest
    @CsvSource({"MODA, MODA", "moda, MODA", "PUBLICIDAD_CINE, PUBLICIDAD_CINE", "publicidad_cine, PUBLICIDAD_CINE"})
    void deberiaConvertirTextoEnTipoActividad(String valor, TipoActividad esperado) {
        // Arrange
        // Act
        TipoActividad tipoActividad = TipoActividad.desde(valor);

        // Assert
        assertThat(tipoActividad).isEqualTo(esperado);
    }

    @Test
    void deberiaExponerDescripcionLegible() {
        // Arrange
        // Act
        // Assert
        assertThat(TipoActividad.MODA.getDescripcion()).isEqualTo("Moda");
        assertThat(TipoActividad.PUBLICIDAD_CINE.getDescripcion()).isEqualTo("Publicidad y cine");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "CINE", "PUBLICIDAD"})
    void deberiaRechazarTipoActividadInvalido(String valor) {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> TipoActividad.desde(valor))
                .isInstanceOf(ClienteInvalidoException.class);
    }
}
