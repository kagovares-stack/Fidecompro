package com.fidecompro.datos;

import com.fidecompro.excepciones.DatoInvalidoException;

// Reglas de validacion compartidas por las listas de datos
public class Validar {

    public static void requerido(String valor, String campo) throws DatoInvalidoException {
        if (valor == null || valor.isBlank()) {
            throw new DatoInvalidoException("El campo " + campo + " es obligatorio.");
        }
    }

    public static void correo(String correo) throws DatoInvalidoException {
        requerido(correo, "Correo");
        if (!correo.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new DatoInvalidoException("El correo '" + correo + "' no es válido.");
        }
    }

    public static void positivo(double valor, String campo) throws DatoInvalidoException {
        if (valor <= 0) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser mayor que cero.");
        }
    }

    public static void noNegativo(int valor, String campo) throws DatoInvalidoException {
        if (valor < 0) {
            throw new DatoInvalidoException("El campo " + campo + " no puede ser negativo.");
        }
    }
}
