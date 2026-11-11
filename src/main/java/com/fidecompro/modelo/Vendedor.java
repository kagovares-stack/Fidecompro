package com.fidecompro.modelo;

public class Vendedor extends Usuario {

    private static final long serialVersionUID = 1L;

    // Se usara cuando se implemente la facturacion (HU-09)
    private int facturasEmitidas;

    public Vendedor(String cedula, String nombre, String nombreUsuario, String contrasena) {
        super(cedula, nombre, nombreUsuario, contrasena);
    }

    @Override
    public String getRol() {
        return "Vendedor";
    }

    @Override
    public boolean puedeGestionarUsuarios() {
        return false;
    }

    @Override
    public boolean puedeGestionarInventario() {
        return false;
    }

    public int getFacturasEmitidas() {
        return facturasEmitidas;
    }

    public void registrarFactura() {
        facturasEmitidas++;
    }
}
