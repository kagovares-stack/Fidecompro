package com.fidecompro.excepciones;

// Se lanza cuando un dato ingresado no cumple una regla (campo vacio, duplicado, valor negativo...)
public class DatoInvalidoException extends Exception {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
