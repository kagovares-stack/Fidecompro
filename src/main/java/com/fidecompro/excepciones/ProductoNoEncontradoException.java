package com.fidecompro.excepciones;

public class ProductoNoEncontradoException extends Exception {

    public ProductoNoEncontradoException(String codigo) {
        super("Producto no encontrado: " + codigo);
    }
}
