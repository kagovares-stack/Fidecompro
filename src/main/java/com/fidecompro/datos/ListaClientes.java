package com.fidecompro.datos;

import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.modelo.Cliente;
import com.fidecompro.modelo.TipoCliente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaClientes {

    private static final String ARCHIVO = "clientes.dat";

    private final ArrayList<Cliente> clientes;

    public ListaClientes() throws IOException, ClassNotFoundException {
        clientes = GestorArchivos.cargar(ARCHIVO);
        if (clientes.isEmpty()) {
            clientes.add(new Cliente("1-1234-0567", "Ana Castro Vargas", "ana.castro@correo.cr", "8812-4450",
                    "San José, Curridabat", TipoCliente.DETALLISTA));
            clientes.add(new Cliente("3-101-458963", "Distribuidora La Esquina S.A.", "compras@laesquina.cr", "2245-8812",
                    "Heredia, San Pablo, 200 m norte de la iglesia", TipoCliente.MAYORISTA));
            clientes.add(new Cliente("3-101-702114", "Minisúper El Roble", "elroble@correo.cr", "2440-1187",
                    "Alajuela, El Roble", TipoCliente.MAYORISTA));
            clientes.add(new Cliente("2-0789-0312", "Pedro Solís Mora", "psolis@correo.cr", "7015-9932",
                    "Alajuela, Grecia", TipoCliente.DETALLISTA));
            clientes.add(new Cliente("3-101-655870", "Pulpería Doña Rosa", "donarosa@correo.cr", "2556-0041",
                    "Cartago, Paraíso", TipoCliente.MAYORISTA));
            guardar();
        }
    }

    // HU-04: registra un cliente nuevo; la cedula no se puede repetir
    public void agregar(Cliente nuevo) throws DatoInvalidoException, IOException {
        validar(nuevo);
        if (buscarPorCedula(nuevo.getCedula()) != null) {
            throw new DatoInvalidoException("Ya existe un cliente con la cédula " + nuevo.getCedula() + ".");
        }
        clientes.add(nuevo);
        guardar();
    }

    // HU-05: reemplaza los datos del cliente que tiene la misma cedula
    public void actualizar(Cliente editado) throws DatoInvalidoException, IOException {
        validar(editado);
        Cliente actual = buscarPorCedula(editado.getCedula());
        if (actual == null) {
            throw new DatoInvalidoException("No existe un cliente con la cédula " + editado.getCedula() + ".");
        }
        actual.setNombre(editado.getNombre());
        actual.setCorreo(editado.getCorreo());
        actual.setTelefono(editado.getTelefono());
        actual.setDireccion(editado.getDireccion());
        actual.setTipoCliente(editado.getTipoCliente());
        guardar();
    }

    // HU-05: busca por cedula o nombre; el resultado sale ordenado por nombre (Comparable)
    public List<Cliente> buscar(String texto) {
        String filtro = texto.trim().toLowerCase();
        ArrayList<Cliente> resultado = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.getCedula().toLowerCase().contains(filtro) || c.getNombre().toLowerCase().contains(filtro)) {
                resultado.add(c);
            }
        }
        Collections.sort(resultado);
        return resultado;
    }

    public Cliente buscarPorCedula(String cedula) {
        for (Cliente c : clientes) {
            if (c.getCedula().equalsIgnoreCase(cedula.trim())) {
                return c;
            }
        }
        return null;
    }

    public int cantidad() {
        return clientes.size();
    }

    private void validar(Cliente c) throws DatoInvalidoException {
        Validar.requerido(c.getCedula(), "Cédula");
        Validar.requerido(c.getNombre(), "Nombre");
        Validar.correo(c.getCorreo());
        Validar.requerido(c.getTelefono(), "Teléfono");
        Validar.requerido(c.getDireccion(), "Dirección");
        if (c.getTipoCliente() == null) {
            throw new DatoInvalidoException("Seleccione el tipo de cliente.");
        }
    }

    private void guardar() throws IOException {
        GestorArchivos.guardar(clientes, ARCHIVO);
    }
}
