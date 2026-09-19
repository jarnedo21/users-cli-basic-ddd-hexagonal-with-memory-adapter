package com.jcaa.udec.collections.adapter.persistence.memory;

import com.jcaa.udec.collections.domain.core.exception.ClienteYaExisteException;
import com.jcaa.udec.collections.domain.core.model.Cliente;
import com.jcaa.udec.collections.domain.port.out.GuardarClientePort;

import java.util.List;
import java.util.Objects;

public class GuardarClienteAdapter implements GuardarClientePort {
    private final List<Cliente> clientes = ClientesMemoria.obtenerClientes();

    @Override
    public void guardar(Cliente cliente) {
        for (Cliente clienteRegistrado : clientes) {
            if (Objects.equals(clienteRegistrado.getCodigo(), cliente.getCodigo())) {
                throw new ClienteYaExisteException();
            }
        }
        clientes.add(cliente);
    }
}
