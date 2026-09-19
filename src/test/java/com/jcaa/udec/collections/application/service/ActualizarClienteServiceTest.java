package com.jcaa.udec.collections.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.application.service.dto.command.ActualizarClienteComando;
import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import com.jcaa.udec.collections.domain.port.out.ActualizarClientePort;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ActualizarClienteServiceTest {
  private static final String CODIGO = "CLI001";
  private static final String NOMBRE = "Cine Caribe Producciones";
  private static final String DIRECCION = "Avenida Pedro de Heredia 45";
  private static final String TELEFONO = "6056543210";
  private static final String PERSONA_CONTACTO = "Carlos Ruiz";
  private static final String TIPO_ACTIVIDAD = "PUBLICIDAD_CINE";

  @Test
  void deberiaMapearYActualizarCliente() {
    // Arrange
    ActualizarClientePortStub actualizarClientePort = new ActualizarClientePortStub();
    ActualizarClienteService service = new ActualizarClienteService(actualizarClientePort);
    ActualizarClienteComando comando =
        new ActualizarClienteComando(
            CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, TIPO_ACTIVIDAD);

    // Act
    service.actualizar(comando);

    // Assert
    assertThat(actualizarClientePort.getClientesActualizados())
        .singleElement()
        .extracting(
            Cliente::getCodigo,
            Cliente::getNombre,
            Cliente::getDireccion,
            Cliente::getTelefono,
            Cliente::getPersonaContacto,
            Cliente::getTipoActividad)
        .containsExactly(
            CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, TipoActividad.PUBLICIDAD_CINE);
  }

  @Test
  void noDeberiaActualizarClienteConDatosInvalidos() {
    // Arrange
    ActualizarClientePortStub actualizarClientePort = new ActualizarClientePortStub();
    ActualizarClienteService service = new ActualizarClienteService(actualizarClientePort);
    ActualizarClienteComando comando =
        new ActualizarClienteComando(
            CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, "TEATRO");

    // Act
    // Assert
    assertThatThrownBy(() -> service.actualizar(comando))
        .isInstanceOf(ClienteInvalidoException.class);
    assertThat(actualizarClientePort.getClientesActualizados()).isEmpty();
  }

  private static final class ActualizarClientePortStub implements ActualizarClientePort {
    private final List<Cliente> clientesActualizados = new ArrayList<>();

    @Override
    public void actualizar(Cliente cliente) {
      clientesActualizados.add(cliente);
    }

    private List<Cliente> getClientesActualizados() {
      return List.copyOf(clientesActualizados);
    }
  }
}
