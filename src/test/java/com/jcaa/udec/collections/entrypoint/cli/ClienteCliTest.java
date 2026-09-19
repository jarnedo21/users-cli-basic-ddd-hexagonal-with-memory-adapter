package com.jcaa.udec.collections.entrypoint.cli;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.entrypoint.controller.ClienteControlador;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.ActualizarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ClienteResponse;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class ClienteCliTest {
    private static final String CODIGO = "CLI001";
    private static final String NOMBRE = "Moda Andina SAS";
    private static final String DIRECCION = "Calle 30 # 17-45";
    private static final String TELEFONO = "3001234567";
    private static final String PERSONA_CONTACTO = "Laura Gomez";
    private static final String OPCION_VOLVER = "6";

    @Test
    void deberiaSolicitarOpcionHastaRecibirValorValido() {
        // Arrange
        ClienteCli clienteCli = crearClienteCli(new ClienteControladorStub(), "texto", "7", "3");

        // Act
        String salida = capturarSalida(
                () -> assertThat(clienteCli.obtenerOpcionMenu()).isEqualTo(3));

        // Assert
        assertThat(salida).contains("Opcion [texto] invalida", "Opcion [7] invalida");
    }

    @Test
    void deberiaRegistrarClienteLuegoDeCorregirDatosInvalidos() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        ClienteCli clienteCli = crearClienteCli(
                controlador,
                "1",
                "C-1",
                CODIGO,
                "Mo",
                NOMBRE,
                "Cll",
                DIRECCION,
                "300-12",
                TELEFONO,
                "La",
                PERSONA_CONTACTO,
                "3",
                "1",
                OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(controlador.getRegistros()).containsExactly(new RegistrarClientePeticion(
                CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, "MODA"));
        assertThat(salida).contains(
                "CODIGO INVALIDO",
                "NOMBRE INVALIDO",
                "DIRECCION INVALIDA",
                "TELEFONO INVALIDO",
                "PERSONA DE CONTACTO INVALIDA",
                "TIPO DE ACTIVIDAD INVALIDO",
                "Cliente registrado correctamente.");
    }

    @Test
    void deberiaMostrarClienteBuscado() {
        // Arrange
        ClienteCli clienteCli = crearClienteCli(
                new ClienteControladorStub(), "2", CODIGO, OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains("CODIGO: " + CODIGO, "NOMBRE: " + NOMBRE);
    }

    @Test
    void deberiaInformarCuandoNoHayClientesRegistrados() {
        // Arrange
        ClienteCli clienteCli = crearClienteCli(new ClienteControladorStub(), "3", OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains("No hay clientes registrados.");
    }

    @Test
    void deberiaMostrarTodosLosClientesRegistrados() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        controlador.registrar(new RegistrarClientePeticion(
                CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, "MODA"));
        ClienteCli clienteCli = crearClienteCli(controlador, "3", OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains("CODIGO: " + CODIGO, "NOMBRE: " + NOMBRE);
    }

    @Test
    void deberiaActualizarCliente() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        ClienteCli clienteCli = crearClienteCli(
                controlador,
                "4",
                CODIGO,
                NOMBRE,
                DIRECCION,
                TELEFONO,
                PERSONA_CONTACTO,
                "2",
                OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(controlador.getActualizaciones()).containsExactly(new ActualizarClientePeticion(
                CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, "PUBLICIDAD_CINE"));
        assertThat(salida).contains("Cliente actualizado correctamente.");
    }

    @Test
    void deberiaEliminarCliente() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        ClienteCli clienteCli = crearClienteCli(controlador, "5", CODIGO, OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(controlador.getEliminaciones()).containsExactly(CODIGO);
        assertThat(salida).contains("Cliente eliminado correctamente.");
    }

    @Test
    void deberiaInformarErrorDelControlador() {
        // Arrange
        ClienteControladorStub controlador = new ClienteControladorStub();
        controlador.reportarClienteInexistente();
        ClienteCli clienteCli = crearClienteCli(controlador, "2", CODIGO, OPCION_VOLVER);

        // Act
        String salida = capturarSalida(clienteCli::ejecutarAccion);

        // Assert
        assertThat(salida).contains("ERROR: El cliente no existe.");
    }

    private static ClienteCli crearClienteCli(ClienteControlador controlador, String... entradas) {
        String contenido = String.join(System.lineSeparator(), entradas) + System.lineSeparator();
        return new ClienteCli(controlador, new Scanner(contenido));
    }

    private static String capturarSalida(Runnable accion) {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
        try {
            accion.run();
            return salida.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(salidaOriginal);
        }
    }

    private static ClienteResponse crearClienteResponse() {
        return new ClienteResponse(CODIGO, NOMBRE, DIRECCION, TELEFONO, PERSONA_CONTACTO, "Moda");
    }

    private static final class ClienteControladorStub implements ClienteControlador {
        private final List<RegistrarClientePeticion> registros = new ArrayList<>();
        private final List<ActualizarClientePeticion> actualizaciones = new ArrayList<>();
        private final List<String> eliminaciones = new ArrayList<>();
        private boolean clienteInexistente;

        @Override
        public void registrar(RegistrarClientePeticion peticion) {
            registros.add(peticion);
        }

        @Override
        public ObtenerClienteResponse obtenerPorCodigo(String codigo) {
            if (clienteInexistente) {
                throw new ClienteNoExisteException();
            }
            return new ObtenerClienteResponse(List.of(crearClienteResponse()));
        }

        @Override
        public ObtenerClienteResponse obtenerTodos() {
            return new ObtenerClienteResponse(registros.stream()
                    .map(peticion -> crearClienteResponse())
                    .toList());
        }

        @Override
        public void actualizar(ActualizarClientePeticion peticion) {
            actualizaciones.add(peticion);
        }

        @Override
        public void eliminar(String codigo) {
            eliminaciones.add(codigo);
        }

        private List<RegistrarClientePeticion> getRegistros() {
            return List.copyOf(registros);
        }

        private List<ActualizarClientePeticion> getActualizaciones() {
            return List.copyOf(actualizaciones);
        }

        private List<String> getEliminaciones() {
            return List.copyOf(eliminaciones);
        }

        private void reportarClienteInexistente() {
            clienteInexistente = true;
        }
    }
}
