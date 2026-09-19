package com.jcaa.udec.collections.domain.core.model;

import com.jcaa.udec.collections.domain.core.valueobject.CodigoCliente;
import com.jcaa.udec.collections.domain.core.valueobject.Direccion;
import com.jcaa.udec.collections.domain.core.valueobject.NombreCliente;
import com.jcaa.udec.collections.domain.core.valueobject.PersonaContacto;
import com.jcaa.udec.collections.domain.core.valueobject.Telefono;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import lombok.Builder;

public class Cliente {
    private final CodigoCliente codigo;
    private final NombreCliente nombre;
    private final Direccion direccion;
    private final Telefono telefono;
    private final PersonaContacto personaContacto;
    private final TipoActividad tipoActividad;

    @Builder
    public Cliente(
            String codigo,
            String nombre,
            String direccion,
            String telefono,
            String personaContacto,
            String tipoActividad) {
        this.codigo = new CodigoCliente(codigo);
        this.nombre = new NombreCliente(nombre);
        this.direccion = new Direccion(direccion);
        this.telefono = new Telefono(telefono);
        this.personaContacto = new PersonaContacto(personaContacto);
        this.tipoActividad = TipoActividad.desde(tipoActividad);
    }

    public String getCodigo() {
        return codigo.valor();
    }

    public String getNombre() {
        return nombre.valor();
    }

    public String getDireccion() {
        return direccion.valor();
    }

    public String getTelefono() {
        return telefono.valor();
    }

    public String getPersonaContacto() {
        return personaContacto.valor();
    }

    public TipoActividad getTipoActividad() {
        return tipoActividad;
    }
}
