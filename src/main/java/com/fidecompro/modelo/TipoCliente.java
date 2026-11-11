package com.fidecompro.modelo;

// El tipo de cliente define el porcentaje de descuento en sus facturas
public enum TipoCliente {

    MAYORISTA("Mayorista", 0.05),
    DETALLISTA("Detallista", 0.0);

    private final String nombre;
    private final double porcentajeDescuento;

    TipoCliente(String nombre, double porcentajeDescuento) {
        this.nombre = nombre;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    // Texto que se muestra en el combo de la ventana de clientes
    @Override
    public String toString() {
        return nombre + " (" + Math.round(porcentajeDescuento * 100) + " % descuento)";
    }
}
