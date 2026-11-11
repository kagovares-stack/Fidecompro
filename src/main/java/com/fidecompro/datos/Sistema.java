package com.fidecompro.datos;

import java.io.IOException;

// Agrupa las colecciones de la aplicacion para compartirlas entre las ventanas
public class Sistema {

    private final ListaUsuarios usuarios;
    private final ListaClientes clientes;
    private final Inventario inventario;

    public Sistema() throws IOException, ClassNotFoundException {
        usuarios = new ListaUsuarios();
        clientes = new ListaClientes();
        inventario = new Inventario();
    }

    public ListaUsuarios getUsuarios() {
        return usuarios;
    }

    public ListaClientes getClientes() {
        return clientes;
    }

    public Inventario getInventario() {
        return inventario;
    }
}
