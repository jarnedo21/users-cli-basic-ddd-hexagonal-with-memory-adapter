package com.jcaa.udec.collections.adapter.persistence.memory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.exception.ClienteYaExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientesAdapterTest {
    private static final AtomicInteger SECUENCIA_CODIGO = new AtomicInteger(100);
    private static final String CODIGO_INEXISTENTE = "NOEXISTE";
    private final GuardarClienteAdapter guardarClienteAdapter = new GuardarClienteAdapter();
    private final ObtenerClientesAdapter obtenerClientesAdapter = new ObtenerClientesAdapter();
    private final EliminarClienteAdapter eliminarClienteAdapter = new EliminarClienteAdapter();
    private final ActualizarClienteAdapter actualizarClienteAdapter = new ActualizarClienteAdapter();

    @BeforeEach
    void limpiarClientes() {
        ClientesMemoria.obtenerClientes().clear();
    }

    @Test
    void deberiaGuardarCliente() {
        // Arrange
        Cliente cliente = crearCliente();

        // Act
        guardarClienteAdapter.guardar(cliente);

        // Assert
        assertThat(ClientesMemoria.obtenerClientes()).containsExactly(cliente);
    }

    @Test
    void deberiaRechazarClienteDuplicado() {
        // Arrange
        Cliente cliente = crearCliente();
        guardarClienteAdapter.guardar(cliente);

        // Act
        // Assert
        assertThatThrownBy(() -> guardarClienteAdapter.guardar(cliente))
                .isInstanceOf(ClienteYaExisteException.class)
                .hasMessage("El cliente ya existe.");
    }

    @Test
    void deberiaBuscarClientePorCodigo() {
        // Arrange
        Cliente cliente = crearCliente();
        guardarClienteAdapter.guardar(crearCliente());
        guardarClienteAdapter.guardar(cliente);

        // Act
        Cliente clienteEncontrado = obtenerClientesAdapter.buscarPorCodigo(cliente.getCodigo());

        // Assert
        assertThat(clienteEncontrado).isSameAs(cliente);
    }

    @Test
    void deberiaReportarClienteInexistente() {
        // Arrange
        guardarClienteAdapter.guardar(crearCliente());

        // Act
        // Assert
        assertThatThrownBy(() -> obtenerClientesAdapter.buscarPorCodigo(CODIGO_INEXISTENTE))
                .isInstanceOf(ClienteNoExisteException.class)
                .hasMessage("El cliente no existe.");
    }

    @Test
    void deberiaObtenerTodosLosClientesGuardados() {
        // Arrange
        Cliente primerCliente = crearCliente();
        Cliente segundoCliente = crearCliente();
        guardarClienteAdapter.guardar(primerCliente);
        guardarClienteAdapter.guardar(segundoCliente);

        // Act
        List<Cliente> clientes = obtenerClientesAdapter.obtenerTodos();

        // Assert
        assertThat(clientes).containsExactly(primerCliente, segundoCliente);
    }

    @Test
    void deberiaRetornarUnaListaInmutable() {
        // Arrange
        List<Cliente> clientes = obtenerClientesAdapter.obtenerTodos();

        // Act
        // Assert
        assertThatThrownBy(() -> clientes.add(crearCliente()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void deberiaEliminarClientePorCodigo() {
        // Arrange
        Cliente clienteAEliminar = crearCliente();
        Cliente clienteQueSeConserva = crearCliente();
        guardarClienteAdapter.guardar(clienteAEliminar);
        guardarClienteAdapter.guardar(clienteQueSeConserva);

        // Act
        eliminarClienteAdapter.eliminar(clienteAEliminar.getCodigo());

        // Assert
        assertThat(obtenerClientesAdapter.obtenerTodos()).containsExactly(clienteQueSeConserva);
    }

    @Test
    void deberiaReemplazarClienteAlActualizarSinDuplicarlo() {
        // Arrange
        Cliente clienteOriginal = crearCliente();
        Cliente otroCliente = crearCliente();
        guardarClienteAdapter.guardar(clienteOriginal);
        guardarClienteAdapter.guardar(otroCliente);
        Cliente clienteActualizado = new Cliente(
                clienteOriginal.getCodigo(),
                "Cine Caribe Producciones",
                "Avenida Pedro de Heredia 45",
                "6056543210",
                "Carlos Ruiz",
                "PUBLICIDAD_CINE");

        // Act
        actualizarClienteAdapter.actualizar(clienteActualizado);

        // Assert
        assertThat(obtenerClientesAdapter.obtenerTodos())
                .containsExactly(clienteActualizado, otroCliente);
        assertThat(obtenerClientesAdapter.buscarPorCodigo(clienteOriginal.getCodigo()))
                .isSameAs(clienteActualizado);
    }

    @Test
    void deberiaReportarClienteInexistenteAlActualizar() {
        // Arrange
        Cliente clienteNoRegistrado = crearCliente();

        // Act
        // Assert
        assertThatThrownBy(() -> actualizarClienteAdapter.actualizar(clienteNoRegistrado))
                .isInstanceOf(ClienteNoExisteException.class)
                .hasMessage("El cliente no existe.");
    }

    private static Cliente crearCliente() {
        String codigo = "CLI" + SECUENCIA_CODIGO.incrementAndGet();
        return new Cliente(
                codigo, "Moda Andina SAS", "Calle 30 # 17-45", "3001234567", "Laura Gomez", "MODA");
    }
}
