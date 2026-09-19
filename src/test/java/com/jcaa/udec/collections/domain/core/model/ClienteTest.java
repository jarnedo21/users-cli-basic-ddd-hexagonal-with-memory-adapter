package com.jcaa.udec.collections.domain.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClienteTest {
  private static final String CODIGO_VALIDO = "CLI001";
  private static final String NOMBRE_VALIDO = "Moda Andina SAS";
  private static final String DIRECCION_VALIDA = "Calle 30 # 17-45";
  private static final String TELEFONO_VALIDO = "3001234567";
  private static final String PERSONA_CONTACTO_VALIDA = "Laura Gomez";
  private static final String TIPO_ACTIVIDAD_VALIDO = "MODA";
  private static final String MENSAJE_DATOS_INVALIDOS = "Los datos del cliente son invalidos.";

  @Test
  void deberiaCrearClienteConDatosValidos() {
    // Arrange
    // Act
    Cliente cliente =
        new Cliente(
            CODIGO_VALIDO,
            NOMBRE_VALIDO,
            DIRECCION_VALIDA,
            TELEFONO_VALIDO,
            PERSONA_CONTACTO_VALIDA,
            TIPO_ACTIVIDAD_VALIDO);

    // Assert
    assertThat(cliente)
        .extracting(
            Cliente::getCodigo,
            Cliente::getNombre,
            Cliente::getDireccion,
            Cliente::getTelefono,
            Cliente::getPersonaContacto,
            Cliente::getTipoActividad)
        .containsExactly(
            CODIGO_VALIDO,
            NOMBRE_VALIDO,
            DIRECCION_VALIDA,
            TELEFONO_VALIDO,
            PERSONA_CONTACTO_VALIDA,
            TipoActividad.MODA);
  }

  @Test
  void deberiaCrearClienteConBuilder() {
    // Arrange
    // Act
    Cliente cliente =
        Cliente.builder()
            .codigo(CODIGO_VALIDO)
            .nombre(NOMBRE_VALIDO)
            .direccion(DIRECCION_VALIDA)
            .telefono(TELEFONO_VALIDO)
            .personaContacto(PERSONA_CONTACTO_VALIDA)
            .tipoActividad("PUBLICIDAD_CINE")
            .build();

    // Assert
    assertThat(cliente)
        .extracting(Cliente::getCodigo, Cliente::getTipoActividad)
        .containsExactly(CODIGO_VALIDO, TipoActividad.PUBLICIDAD_CINE);
  }

  @ParameterizedTest
  @MethodSource("datosInvalidos")
  void deberiaRechazarClienteConDatosInvalidos(
      String codigo,
      String nombre,
      String direccion,
      String telefono,
      String personaContacto,
      String tipoActividad) {
    // Arrange
    // Act
    // Assert
    assertThatThrownBy(
            () -> new Cliente(codigo, nombre, direccion, telefono, personaContacto, tipoActividad))
        .isInstanceOf(ClienteInvalidoException.class)
        .hasMessage(MENSAJE_DATOS_INVALIDOS);
  }

  private static Stream<Arguments> datosInvalidos() {
    return Stream.of(
        Arguments.of("C1", NOMBRE_VALIDO, DIRECCION_VALIDA, TELEFONO_VALIDO,
            PERSONA_CONTACTO_VALIDA, TIPO_ACTIVIDAD_VALIDO),
        Arguments.of(CODIGO_VALIDO, "Mo", DIRECCION_VALIDA, TELEFONO_VALIDO,
            PERSONA_CONTACTO_VALIDA, TIPO_ACTIVIDAD_VALIDO),
        Arguments.of(CODIGO_VALIDO, NOMBRE_VALIDO, "Cll", TELEFONO_VALIDO,
            PERSONA_CONTACTO_VALIDA, TIPO_ACTIVIDAD_VALIDO),
        Arguments.of(CODIGO_VALIDO, NOMBRE_VALIDO, DIRECCION_VALIDA, "12345",
            PERSONA_CONTACTO_VALIDA, TIPO_ACTIVIDAD_VALIDO),
        Arguments.of(CODIGO_VALIDO, NOMBRE_VALIDO, DIRECCION_VALIDA, TELEFONO_VALIDO,
            "La", TIPO_ACTIVIDAD_VALIDO),
        Arguments.of(CODIGO_VALIDO, NOMBRE_VALIDO, DIRECCION_VALIDA, TELEFONO_VALIDO,
            PERSONA_CONTACTO_VALIDA, "TEATRO"));
  }
}
