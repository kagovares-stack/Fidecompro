package com.fidecompro.datos;

import com.fidecompro.excepciones.CredencialesInvalidasException;
import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.modelo.Administrador;
import com.fidecompro.modelo.Usuario;
import com.fidecompro.modelo.Vendedor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaUsuarios {

    private static final String ARCHIVO = "usuarios.dat";
    private static final int LARGO_MINIMO_CONTRASENA = 6;

    private final ArrayList<Usuario> usuarios;

    public ListaUsuarios() throws IOException, ClassNotFoundException {
        usuarios = GestorArchivos.cargar(ARCHIVO);
        if (usuarios.isEmpty()) {
            usuarios.add(new Administrador("1-1111-1111", "María Rojas", "admin", "admin123"));
            usuarios.add(new Vendedor("1-0987-0654", "José Mora", "jmora", "vende123"));
            guardar();
        }
    }

    // HU-01: valida las credenciales contra los usuarios guardados en el archivo
    public Usuario autenticar(String nombreUsuario, String contrasena) throws CredencialesInvalidasException {
        for (Usuario u : usuarios) {
            if (u.validarCredenciales(nombreUsuario, contrasena)) {
                if (!u.isActivo()) {
                    throw new CredencialesInvalidasException("El usuario está inactivo. Consulte con un administrador.");
                }
                return u;
            }
        }
        throw new CredencialesInvalidasException("Usuario o contraseña inválidos");
    }

    // HU-03: crea un usuario con nombre de usuario unico y contrasena de al menos 6 caracteres
    public void agregar(Usuario nuevo) throws DatoInvalidoException, IOException {
        Validar.requerido(nuevo.getCedula(), "Cédula");
        Validar.requerido(nuevo.getNombre(), "Nombre completo");
        Validar.requerido(nuevo.getNombreUsuario(), "Usuario");
        if (nuevo.getContrasena().length() < LARGO_MINIMO_CONTRASENA) {
            throw new DatoInvalidoException("La contraseña debe tener al menos " + LARGO_MINIMO_CONTRASENA + " caracteres.");
        }
        if (usuarios.contains(nuevo)) {
            throw new DatoInvalidoException("El nombre de usuario '" + nuevo.getNombreUsuario() + "' ya existe.");
        }
        usuarios.add(nuevo);
        guardar();
    }

    public void cambiarEstado(Usuario usuario, boolean activo) throws IOException {
        usuario.setActivo(activo);
        guardar();
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    private void guardar() throws IOException {
        GestorArchivos.guardar(usuarios, ARCHIVO);
    }
}
