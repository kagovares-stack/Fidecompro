package com.fidecompro.modelo;

import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.excepciones.StockInsuficienteException;
import java.io.Serializable;

// Producto generico del inventario. Cada tipo define su impuesto y sus datos propios.
public abstract class Producto implements Serializable, Comparable<Producto> {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private int stockMinimo;

    public Producto(String codigo, String nombre, double precio, int stock, int stockMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    // Tasa de IVA segun el tipo de producto
    public abstract double getTasaImpuesto();

    public abstract String getTipo();

    // Resumen de los datos propios de cada tipo, para mostrar en la tabla
    public abstract String getDetalle();

    public double calcularImpuesto() {
        return precio * getTasaImpuesto();
    }

    public void aumentarStock(int cantidad) throws DatoInvalidoException {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que cero.");
        }
        stock += cantidad;
    }

    public void reducirStock(int cantidad) throws StockInsuficienteException, DatoInvalidoException {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > stock) {
            throw new StockInsuficienteException(nombre, stock);
        }
        stock -= cantidad;
    }

    public boolean tieneStockBajo() {
        return stock < stockMinimo;
    }

    @Override
    public int compareTo(Producto otro) {
        return nombre.compareToIgnoreCase(otro.nombre);
    }

    @Override
    public String toString() {
        return codigo + " " + nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) throws DatoInvalidoException {
        if (precio <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor que cero.");
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}
