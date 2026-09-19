package com.jcaa.udec.collections.domain.core.valueobject;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import java.util.Objects;

public record Direccion(String valor) {
    private static final int LONGITUD_MINIMA = 5;

    public Direccion {
        if (Objects.isNull(valor) || valor.isBlank() || valor.length() < LONGITUD_MINIMA) {
            throw new ClienteInvalidoException();
        }
    }
}
