package com.jcaa.udec.collections.adapter.persistence.memory;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.ActualizarClientePort;

import java.util.List;
import java.util.Objects;

public class ActualizarClienteAdapter implements ActualizarClientePort {
    private final List<Cliente> clientes = ClientesMemoria.obtenerClientes();

    @Override
    public void actualizar(Cliente cliente) {
        for (int indice = 0; indice < clientes.size(); indice++) {
            if (Objects.equals(clientes.get(indice).getCodigo(), cliente.getCodigo())) {
                clientes.set(indice, cliente);
                return;
            }
        }
        throw new ClienteNoExisteException();
    }
}
