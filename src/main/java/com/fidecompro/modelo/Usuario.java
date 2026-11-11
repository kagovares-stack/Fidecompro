package com.fidecompro.modelo;

import java.util.Objects;

// Persona que puede ingresar al sistema. Cada rol define sus permisos (polimorfismo).
public abstract class Usuario extends Persona {

    private static final long serialVersionUID = 1L;

    private String nombreUsuario;
    private String contrasena;
    private boolean activo = true;

    public Usuario(String cedula, String nombre, String nombreUsuario, String contrasena) {
        super(cedula, nombre, "", "");
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public boolean validarCredenciales(String usuario, String clave) {
        return nombreUsuario.equalsIgnoreCase(usuario) && contrasena.equals(clave);
    }

    public abstract String getRol();

    public abstract boolean puedeGestionarUsuarios();

    public abstract boolean puedeGestionarInventario();

    // Dos usuarios son el mismo si tienen el mismo nombre de usuario
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return nombreUsuario.equalsIgnoreCase(((Usuario) o).nombreUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreUsuario.toLowerCase());
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
