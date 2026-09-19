package com.jcaa.udec.collections.adapter.persistence.memory;

import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.ObtenerClientesPort;

import java.util.List;
import java.util.Objects;

public class ObtenerClientesAdapter implements ObtenerClientesPort {
    private final List<Cliente> clientes = ClientesMemoria.obtenerClientes();

    @Override
    public List<Cliente> obtenerTodos() {
        return List.copyOf(clientes);
    }

    @Override
    public Cliente buscarPorCodigo(String codigo) {
        for (Cliente cliente : clientes) {
            if (Objects.equals(cliente.getCodigo(), codigo)) {
                return cliente;
            }
        }
        throw new ClienteNoExisteException();
    }
}
