package com.fidecompro.modelo;

public class Cliente extends Persona implements Comparable<Cliente> {

    private static final long serialVersionUID = 1L;

    private String direccion;
    private TipoCliente tipoCliente;

    public Cliente(String cedula, String nombre, String correo, String telefono,
            String direccion, TipoCliente tipoCliente) {
        super(cedula, nombre, correo, telefono);
        this.direccion = direccion;
        this.tipoCliente = tipoCliente;
    }

    public double calcularDescuento(double monto) {
        return monto * tipoCliente.getPorcentajeDescuento();
    }

    // Orden natural: alfabetico por nombre (la lista de clientes se muestra asi)
    @Override
    public int compareTo(Cliente otro) {
        return getNombre().compareToIgnoreCase(otro.getNombre());
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}
