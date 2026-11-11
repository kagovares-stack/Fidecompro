package com.fidecompro.excepciones;

public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(String producto, int disponible) {
        super("Stock insuficiente de " + producto + ": quedan " + disponible + " unidades");
    }
}
