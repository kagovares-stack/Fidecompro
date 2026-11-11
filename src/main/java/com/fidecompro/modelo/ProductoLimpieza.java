package com.fidecompro.modelo;

public class ProductoLimpieza extends Producto {

    private static final long serialVersionUID = 1L;

    private String presentacion;
    private boolean esToxico;

    public ProductoLimpieza(String codigo, String nombre, double precio, int stock, int stockMinimo,
            String presentacion, boolean esToxico) {
        super(codigo, nombre, precio, stock, stockMinimo);
        this.presentacion = presentacion;
        this.esToxico = esToxico;
    }

    @Override
    public double getTasaImpuesto() {
        return 0.13;
    }

    @Override
    public String getTipo() {
        return "Limpieza";
    }

    @Override
    public String getDetalle() {
        return esToxico ? presentacion + ", tóxico" : presentacion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public boolean isEsToxico() {
        return esToxico;
    }
}
