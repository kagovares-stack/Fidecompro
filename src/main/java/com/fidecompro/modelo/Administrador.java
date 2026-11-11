package com.fidecompro.modelo;

public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;

    public Administrador(String cedula, String nombre, String nombreUsuario, String contrasena) {
        super(cedula, nombre, nombreUsuario, contrasena);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }

    @Override
    public boolean puedeGestionarUsuarios() {
        return true;
    }

    @Override
    public boolean puedeGestionarInventario() {
        return true;
    }
}
