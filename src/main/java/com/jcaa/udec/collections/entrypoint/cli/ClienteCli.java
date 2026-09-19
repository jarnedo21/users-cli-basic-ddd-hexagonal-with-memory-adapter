package com.jcaa.udec.collections.entrypoint.cli;

import com.jcaa.udec.collections.domain.core.exception.ClienteInvalidoException;
import com.jcaa.udec.collections.domain.core.exception.ClienteNoExisteException;
import com.jcaa.udec.collections.domain.core.exception.ClienteYaExisteException;
import com.jcaa.udec.collections.domain.core.valueobject.CodigoCliente;
import com.jcaa.udec.collections.domain.core.valueobject.Direccion;
import com.jcaa.udec.collections.domain.core.valueobject.NombreCliente;
import com.jcaa.udec.collections.domain.core.valueobject.PersonaContacto;
import com.jcaa.udec.collections.domain.core.valueobject.Telefono;
import com.jcaa.udec.collections.domain.core.valueobject.TipoActividad;
import com.jcaa.udec.collections.entrypoint.controller.ClienteControlador;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.ActualizarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.request.RegistrarClientePeticion;
import com.jcaa.udec.collections.entrypoint.controller.dto.response.ObtenerClienteResponse;

import java.util.Scanner;

public class ClienteCli {
    private static final int OPCION_REGISTRAR = 1;
    private static final int OPCION_BUSCAR = 2;
    private static final int OPCION_LISTAR = 3;
    private static final int OPCION_ACTUALIZAR = 4;
    private static final int OPCION_ELIMINAR = 5;
    private static final int OPCION_VOLVER = 6;
    private static final String OPCION_TIPO_MODA = "1";
    private static final String OPCION_TIPO_PUBLICIDAD_CINE = "2";
    private static final String TEXTO_TITULO = "** GESTION DE CLIENTES - AGENCIA DE CASTINGS **";
    private static final String TITULO_REGISTRO = "** INGRESE LOS DATOS DEL NUEVO CLIENTE **";
    private static final String TITULO_ACTUALIZACION = "** INGRESE LOS NUEVOS DATOS DEL CLIENTE **";
    private static final String SEPARADOR = "- - - - - - - - - ";
    private static final String OPCIONES = "Opciones:";
    private static final String TEXTO_OPCION_REGISTRAR = "1 - Registrar cliente";
    private static final String TEXTO_OPCION_BUSCAR = "2 - Buscar cliente por codigo";
    private static final String TEXTO_OPCION_LISTAR = "3 - Listar clientes";
    private static final String TEXTO_OPCION_ACTUALIZAR = "4 - Actualizar cliente";
    private static final String TEXTO_OPCION_ELIMINAR = "5 - Eliminar cliente";
    private static final String TEXTO_OPCION_VOLVER = "6 - Volver al menu principal";
    private static final String TEXTO_SOLICITUD_OPCION = "Ingrese el numero de la opcion: ";
    private static final String SOLICITUD_CODIGO = "CODIGO: ";
    private static final String SOLICITUD_NOMBRE = "NOMBRE: ";
    private static final String SOLICITUD_DIRECCION = "DIRECCION: ";
    private static final String SOLICITUD_TELEFONO = "TELEFONO: ";
    private static final String SOLICITUD_PERSONA_CONTACTO = "PERSONA DE CONTACTO: ";
    private static final String SOLICITUD_TIPO_ACTIVIDAD =
            "TIPO DE ACTIVIDAD (1 - Moda, 2 - Publicidad y cine): ";
    private static final String MENSAJE_OPCION_INVALIDA = "Opcion [%s] invalida";
    private static final String MENSAJE_CODIGO_INVALIDO =
            "CODIGO INVALIDO: entre 3 y 10 letras o numeros, sin espacios";
    private static final String MENSAJE_NOMBRE_INVALIDO = "NOMBRE INVALIDO: minimo 3 caracteres";
    private static final String MENSAJE_DIRECCION_INVALIDA = "DIRECCION INVALIDA: minimo 5 caracteres";
    private static final String MENSAJE_TELEFONO_INVALIDO =
            "TELEFONO INVALIDO: solo numeros, entre 7 y 10 digitos";
    private static final String MENSAJE_PERSONA_CONTACTO_INVALIDA =
            "PERSONA DE CONTACTO INVALIDA: minimo 3 caracteres";
    private static final String MENSAJE_TIPO_ACTIVIDAD_INVALIDO = "TIPO DE ACTIVIDAD INVALIDO: ingrese 1 o 2";
    private static final String MENSAJE_ERROR = "ERROR: ";
    private static final String MENSAJE_REGISTRO_EXITOSO = "Cliente registrado correctamente.";
    private static final String MENSAJE_ACTUALIZACION_EXITOSA = "Cliente actualizado correctamente.";
    private static final String MENSAJE_ELIMINACION_EXITOSA = "Cliente eliminado correctamente.";
    private static final String MENSAJE_LISTA_VACIA = "No hay clientes registrados.";
    private static final String MARCA_ORDEN_BYTES = "﻿";
    private static final String TEXTO_VACIO = "";
    private final ClienteControlador clienteControlador;
    private final Scanner entrada;

    ClienteCli(ClienteControlador clienteControlador, Scanner entrada) {
        this.clienteControlador = clienteControlador;
        this.entrada = entrada;
    }

    public void ejecutarAccion() {
        boolean continuar = true;
        while (continuar) {
            int opcion = obtenerOpcionMenu();
            try {
                switch (opcion) {
                    case OPCION_REGISTRAR -> registrarCliente();
                    case OPCION_BUSCAR -> mostrarClientePorCodigo();
                    case OPCION_LISTAR -> mostrarTodosLosClientes();
                    case OPCION_ACTUALIZAR -> actualizarCliente();
                    case OPCION_ELIMINAR -> eliminarCliente();
                    case OPCION_VOLVER -> continuar = false;
                }
            } catch (ClienteInvalidoException
                    | ClienteNoExisteException
                    | ClienteYaExisteException exception) {
                System.out.println(MENSAJE_ERROR + exception.getMessage());
            }
        }
    }

    int obtenerOpcionMenu() {
        do {
            mostrarMenu();
            String valorIngresado = limpiarEntrada(entrada.nextLine());
            try {
                int opcion = Integer.parseInt(valorIngresado);
                if (opcion >= OPCION_REGISTRAR && opcion <= OPCION_VOLVER) {
                    return opcion;
                }
            } catch (NumberFormatException exception) {
                // El flujo informa el valor invalido y vuelve a mostrar el menu.
            }
            System.out.printf(MENSAJE_OPCION_INVALIDA + "%n", valorIngresado);
        } while (true);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println(TEXTO_TITULO);
        System.out.println(SEPARADOR);
        System.out.println(OPCIONES);
        System.out.println(SEPARADOR);
        System.out.println(TEXTO_OPCION_REGISTRAR);
        System.out.println(TEXTO_OPCION_BUSCAR);
        System.out.println(TEXTO_OPCION_LISTAR);
        System.out.println(TEXTO_OPCION_ACTUALIZAR);
        System.out.println(TEXTO_OPCION_ELIMINAR);
        System.out.println(TEXTO_OPCION_VOLVER);
        System.out.print(TEXTO_SOLICITUD_OPCION);
    }

    private void registrarCliente() {
        System.out.println();
        System.out.println(TITULO_REGISTRO);
        clienteControlador.registrar(new RegistrarClientePeticion(
                capturarCodigo(),
                capturarNombre(),
                capturarDireccion(),
                capturarTelefono(),
                capturarPersonaContacto(),
                capturarTipoActividad()));
        System.out.println(MENSAJE_REGISTRO_EXITOSO);
    }

    private void mostrarClientePorCodigo() {
        System.out.println(clienteControlador.obtenerPorCodigo(capturarCodigo()));
    }

    private void mostrarTodosLosClientes() {
        ObtenerClienteResponse response = clienteControlador.obtenerTodos();
        if (response.estaVacia()) {
            System.out.println(MENSAJE_LISTA_VACIA);
            return;
        }
        System.out.println(response);
    }

    private void actualizarCliente() {
        System.out.println();
        System.out.println(TITULO_ACTUALIZACION);
        clienteControlador.actualizar(new ActualizarClientePeticion(
                capturarCodigo(),
                capturarNombre(),
                capturarDireccion(),
                capturarTelefono(),
                capturarPersonaContacto(),
                capturarTipoActividad()));
        System.out.println(MENSAJE_ACTUALIZACION_EXITOSA);
    }

    private void eliminarCliente() {
        clienteControlador.eliminar(capturarCodigo());
        System.out.println(MENSAJE_ELIMINACION_EXITOSA);
    }

    private String capturarCodigo() {
        do {
            System.out.print(SOLICITUD_CODIGO);
            String codigo = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new CodigoCliente(codigo))) {
                return codigo;
            }
            System.out.println(MENSAJE_CODIGO_INVALIDO);
        } while (true);
    }

    private String capturarNombre() {
        do {
            System.out.print(SOLICITUD_NOMBRE);
            String nombre = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new NombreCliente(nombre))) {
                return nombre;
            }
            System.out.println(MENSAJE_NOMBRE_INVALIDO);
        } while (true);
    }

    private String capturarDireccion() {
        do {
            System.out.print(SOLICITUD_DIRECCION);
            String direccion = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new Direccion(direccion))) {
                return direccion;
            }
            System.out.println(MENSAJE_DIRECCION_INVALIDA);
        } while (true);
    }

    private String capturarTelefono() {
        do {
            System.out.print(SOLICITUD_TELEFONO);
            String telefono = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new Telefono(telefono))) {
                return telefono;
            }
            System.out.println(MENSAJE_TELEFONO_INVALIDO);
        } while (true);
    }

    private String capturarPersonaContacto() {
        do {
            System.out.print(SOLICITUD_PERSONA_CONTACTO);
            String personaContacto = limpiarEntrada(entrada.nextLine());
            if (esValido(() -> new PersonaContacto(personaContacto))) {
                return personaContacto;
            }
            System.out.println(MENSAJE_PERSONA_CONTACTO_INVALIDA);
        } while (true);
    }

    private String capturarTipoActividad() {
        do {
            System.out.print(SOLICITUD_TIPO_ACTIVIDAD);
            String opcion = limpiarEntrada(entrada.nextLine());
            if (OPCION_TIPO_MODA.equals(opcion)) {
                return TipoActividad.MODA.name();
            }
            if (OPCION_TIPO_PUBLICIDAD_CINE.equals(opcion)) {
                return TipoActividad.PUBLICIDAD_CINE.name();
            }
            System.out.println(MENSAJE_TIPO_ACTIVIDAD_INVALIDO);
        } while (true);
    }

    private static String limpiarEntrada(String valor) {
        return valor.replace(MARCA_ORDEN_BYTES, TEXTO_VACIO).trim();
    }

    private static boolean esValido(Runnable validacion) {
        try {
            validacion.run();
            return true;
        } catch (ClienteInvalidoException exception) {
            return false;
        }
    }
}
