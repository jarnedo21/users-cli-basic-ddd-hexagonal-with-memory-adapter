package com.jcaa.udec.collections.entrypoint.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClienteResponseTest {

    @Test
    void deberiaMostrarCadaDatoEnSuEtiqueta() {
        // Arrange
        ClienteResponse response = ClienteResponse.builder()
                .codigo("CLI001")
                .nombre("Moda Andina SAS")
                .direccion("Calle 30 # 17-45")
                .telefono("3001234567")
                .personaContacto("Laura Gomez")
                .tipoActividad("Moda")
                .build();

        // Act
        String texto = response.toString();

        // Assert
        assertThat(texto).isEqualTo("""
                CODIGO: CLI001
                NOMBRE: Moda Andina SAS
                DIRECCION: Calle 30 # 17-45
                TELEFONO: 3001234567
                PERSONA DE CONTACTO: Laura Gomez
                TIPO DE ACTIVIDAD: Moda
                """);
    }
}
