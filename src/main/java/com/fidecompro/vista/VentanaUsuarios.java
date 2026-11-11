package com.fidecompro.vista;

import com.fidecompro.datos.Sistema;
import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.modelo.Administrador;
import com.fidecompro.modelo.Usuario;
import com.fidecompro.modelo.Vendedor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

// HU-03: el administrador crea usuarios y los activa o desactiva
public class VentanaUsuarios extends VentanaModulo {

    private static final String ROL_ADMINISTRADOR = "Administrador";
    private static final String ROL_VENDEDOR = "Vendedor";

    private final Sistema sistema;
    private final Usuario usuarioActual;
    private List<Usuario> usuariosMostrados = new ArrayList<>();

    private JTextField campoCedula;
    private JTextField campoNombre;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JComboBox<String> comboRol;
    private DefaultTableModel modeloTabla;
    private JTable tablaUsuarios;

    public VentanaUsuarios(Sistema sistema, Usuario usuarioActual, Runnable alVolver) {
        super("Usuarios", alVolver);
        this.sistema = sistema;
        this.usuarioActual = usuarioActual;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        campoCedula = Estilo.campoTexto();
        campoNombre = Estilo.campoTexto();
        campoUsuario = Estilo.campoTexto();
        campoContrasena = new JPasswordField(20);
        Estilo.estilizarCampo(campoContrasena);
        comboRol = Estilo.combo(new String[]{ROL_VENDEDOR, ROL_ADMINISTRADOR});

        JButton botonCrear = Estilo.botonPrimario("Crear usuario");
        botonCrear.addActionListener(evt -> botonCrearActionPerformed());

        JPanel formulario = Estilo.tarjeta(Estilo.BORDE);
        formulario.setLayout(new BorderLayout());
        formulario.setPreferredSize(new Dimension(380, 0));
        formulario.add(Estilo.columna(14,
                Estilo.titulo("Crear usuario", 22),
                Estilo.campo("Cédula", campoCedula),
                Estilo.campo("Nombre completo", campoNombre),
                Estilo.campo("Usuario", campoUsuario),
                Estilo.campo("Contraseña (mínimo 6 caracteres)", campoContrasena),
                Estilo.campo("Rol", comboRol),
                botonCrear));

        modeloTabla = Estilo.modeloTabla("Usuario", "Nombre", "Rol", "Estado");
        tablaUsuarios = Estilo.tabla(modeloTabla);
        tablaUsuarios.getColumnModel().getColumn(3).setCellRenderer(new RenderizadorEstado());

        JButton botonCambiarEstado = Estilo.botonSecundario("Activar / desactivar seleccionado");
        botonCambiarEstado.addActionListener(evt -> botonCambiarEstadoActionPerformed());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.setOpaque(false);
        acciones.add(botonCambiarEstado);

        JPanel listado = Estilo.tarjeta(Estilo.BORDE);
        listado.setLayout(new BorderLayout(0, 16));
        listado.add(Estilo.titulo("Usuarios del sistema", 22), BorderLayout.NORTH);
        listado.add(Estilo.desplazable(tablaUsuarios), BorderLayout.CENTER);
        listado.add(acciones, BorderLayout.SOUTH);

        JPanel contenido = new JPanel(new BorderLayout(24, 0));
        contenido.setOpaque(false);
        contenido.add(formulario, BorderLayout.WEST);
        contenido.add(listado, BorderLayout.CENTER);
        setContenido(contenido);
    }

    private void cargarTabla() {
        usuariosMostrados = new ArrayList<>(sistema.getUsuarios().getUsuarios());
        modeloTabla.setRowCount(0);
        for (Usuario u : usuariosMostrados) {
            modeloTabla.addRow(new Object[]{u.getNombreUsuario(), u.getNombre(), u.getRol(),
                u.isActivo() ? "Activo" : "Inactivo"});
        }
    }

    private void botonCrearActionPerformed() {
        String cedula = campoCedula.getText().trim();
        String nombre = campoNombre.getText().trim();
        String nombreUsuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        // Se crea la subclase que corresponde al rol elegido
        Usuario nuevo = ROL_ADMINISTRADOR.equals(comboRol.getSelectedItem())
                ? new Administrador(cedula, nombre, nombreUsuario, contrasena)
                : new Vendedor(cedula, nombre, nombreUsuario, contrasena);
        try {
            sistema.getUsuarios().agregar(nuevo);
            Estilo.exito(this, "Usuario '" + nombreUsuario + "' creado como " + nuevo.getRol() + ".");
            limpiarFormulario();
            cargarTabla();
        } catch (DatoInvalidoException e) {
            Estilo.aviso(this, e.getMessage());
        } catch (IOException e) {
            Estilo.error(this, "No se pudo guardar el usuario: " + e.getMessage());
        }
    }

    private void botonCambiarEstadoActionPerformed() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila < 0) {
            Estilo.aviso(this, "Seleccione un usuario de la tabla.");
            return;
        }
        Usuario seleccionado = usuariosMostrados.get(fila);
        if (seleccionado.equals(usuarioActual)) {
            Estilo.aviso(this, "No puede desactivar su propio usuario.");
            return;
        }
        try {
            sistema.getUsuarios().cambiarEstado(seleccionado, !seleccionado.isActivo());
            cargarTabla();
        } catch (IOException e) {
            Estilo.error(this, "No se pudo guardar el cambio: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        campoCedula.setText("");
        campoNombre.setText("");
        campoUsuario.setText("");
        campoContrasena.setText("");
        comboRol.setSelectedIndex(0);
        campoCedula.requestFocus();
    }

    // Muestra "Activo" en verde e "Inactivo" en gris, como en el prototipo
    private static class RenderizadorEstado extends Estilo.RenderizadorCelda {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionado, foco, fila, columna);
            setFont(Estilo.fuente(Font.BOLD, 13));
            setForeground("Activo".equals(valor) ? Estilo.VERDE : Estilo.TEXTO_SECUNDARIO);
            return this;
        }
    }
}
