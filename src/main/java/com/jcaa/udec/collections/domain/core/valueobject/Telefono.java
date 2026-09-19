package com.jcaa.udec.collections.domain.core.valueobject;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import java.util.Objects;

public record Telefono(String valor) {
    private static final int LONGITUD_MINIMA = 7;
    private static final int LONGITUD_MAXIMA = 10;

    public Telefono {
        if (!esValido(valor)) {
            throw new ClienteInvalidoException();
        }
    }

    private static boolean esValido(String valor) {
        if (Objects.isNull(valor)
                || valor.length() < LONGITUD_MINIMA
                || valor.length() > LONGITUD_MAXIMA) {
            return false;
        }

        for (int indice = 0; indice < valor.length(); indice++) {
            if (!Character.isDigit(valor.charAt(indice))) {
                return false;
            }
        }
        return true;
    }
}
