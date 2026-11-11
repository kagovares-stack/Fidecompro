package com.fidecompro.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProductoAlimenticio extends Producto {

    private static final long serialVersionUID = 1L;

    private LocalDate fechaVencimiento;
    private boolean canastaBasica;

    public ProductoAlimenticio(String codigo, String nombre, double precio, int stock, int stockMinimo,
            LocalDate fechaVencimiento, boolean canastaBasica) {
        super(codigo, nombre, precio, stock, stockMinimo);
        this.fechaVencimiento = fechaVencimiento;
        this.canastaBasica = canastaBasica;
    }

    // Los productos de la canasta basica pagan 1 % de IVA; el resto 13 %
    @Override
    public double getTasaImpuesto() {
        return canastaBasica ? 0.01 : 0.13;
    }

    @Override
    public String getTipo() {
        return "Alimenticio";
    }

    @Override
    public String getDetalle() {
        String vence = "Vence " + fechaVencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return canastaBasica ? vence + ", canasta básica" : vence;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public boolean isCanastaBasica() {
        return canastaBasica;
    }
}
