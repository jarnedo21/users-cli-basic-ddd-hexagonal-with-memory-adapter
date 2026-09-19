package com.jcaa.udec.collections.application.service.mapper;

import com.jcaa.udec.collections.application.service.dto.command.CrearClienteComando;
import com.jcaa.udec.collections.domain.core.model.Cliente;

public final class ClienteMapper {
  private ClienteMapper() {}

  public static Cliente mapearACliente(CrearClienteComando comando) {
    return Cliente.builder()
        .codigo(comando.codigo())
        .nombre(comando.nombre())
        .direccion(comando.direccion())
        .telefono(comando.telefono())
        .personaContacto(comando.personaContacto())
        .tipoActividad(comando.tipoActividad())
        .build();
  }
}
