package com.fidecompro.modelo;

import java.io.Serializable;

// Datos comunes de cualquier persona del sistema (usuarios y clientes)
public abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cedula;
    private String nombre;
    private String correo;
    private String telefono;

    public Persona(String cedula, String nombre, String correo, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return nombre + " (" + cedula + ")";
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
