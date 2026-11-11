package com.fidecompro.modelo;

public class ProductoElectronico extends Producto {

    private static final long serialVersionUID = 1L;

    private String marca;
    private int garantiaMeses;

    public ProductoElectronico(String codigo, String nombre, double precio, int stock, int stockMinimo,
            String marca, int garantiaMeses) {
        super(codigo, nombre, precio, stock, stockMinimo);
        this.marca = marca;
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public double getTasaImpuesto() {
        return 0.13;
    }

    @Override
    public String getTipo() {
        return "Electrónico";
    }

    @Override
    public String getDetalle() {
        return marca + ", garantía " + garantiaMeses + " meses";
    }

    public String getMarca() {
        return marca;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }
}
