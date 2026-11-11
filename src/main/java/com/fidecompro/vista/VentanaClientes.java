package com.fidecompro.vista;

import com.fidecompro.datos.Sistema;
import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.modelo.Cliente;
import com.fidecompro.modelo.TipoCliente;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

// HU-04: registrar clientes. HU-05: buscarlos y editarlos.
public class VentanaClientes extends VentanaModulo {

    private final Sistema sistema;
    private List<Cliente> clientesMostrados = new ArrayList<>();
    private boolean editando = false;

    private JLabel tituloFormulario;
    private JTextField campoCedula;
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JTextField campoTelefono;
    private JTextField campoDireccion;
    private JComboBox<TipoCliente> comboTipo;
    private JTextField campoBuscar;
    private DefaultTableModel modeloTabla;
    private JTable tablaClientes;

    public VentanaClientes(Sistema sistema, Runnable alVolver) {
        super("Clientes", alVolver);
        this.sistema = sistema;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        tituloFormulario = Estilo.titulo("Registrar cliente", 22);
        campoCedula = Estilo.campoTexto();
        campoNombre = Estilo.campoTexto();
        campoCorreo = Estilo.campoTexto();
        campoTelefono = Estilo.campoTexto();
        campoDireccion = Estilo.campoTexto();
        comboTipo = Estilo.combo(TipoCliente.values());

        JButton botonGuardar = Estilo.botonPrimario("Guardar");
        botonGuardar.addActionListener(evt -> botonGuardarActionPerformed());
        JButton botonLimpiar = Estilo.botonSecundario("Limpiar");
        botonLimpiar.addActionListener(evt -> limpiarFormulario());
        JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
        botones.setOpaque(false);
        botones.add(botonGuardar);
        botones.add(botonLimpiar);

        JPanel formulario = Estilo.tarjeta(Estilo.BORDE);
        formulario.setLayout(new BorderLayout());
        formulario.setPreferredSize(new Dimension(400, 0));
        formulario.add(Estilo.columna(12,
                tituloFormulario,
                Estilo.campo("Cédula", campoCedula),
                Estilo.campo("Nombre", campoNombre),
                Estilo.campo("Correo", campoCorreo),
                Estilo.campo("Teléfono", campoTelefono),
                Estilo.campo("Dirección", campoDireccion),
                Estilo.campo("Tipo de cliente", comboTipo),
                botones));

        campoBuscar = new JTextField(22);
        Estilo.estilizarCampo(campoBuscar);
        campoBuscar.setToolTipText("Buscar por cédula o nombre");
        campoBuscar.addActionListener(evt -> cargarTabla());
        JButton botonBuscar = Estilo.botonPrimario("Buscar");
        botonBuscar.addActionListener(evt -> cargarTabla());
        JPanel busqueda = new JPanel(new BorderLayout(8, 0));
        busqueda.setOpaque(false);
        busqueda.add(campoBuscar, BorderLayout.CENTER);
        busqueda.add(botonBuscar, BorderLayout.EAST);

        JPanel cabecera = Estilo.columna(14,
                Estilo.titulo("Clientes registrados", 22),
                Estilo.campo("Buscar por cédula o nombre", busqueda));

        modeloTabla = Estilo.modeloTabla("Cédula", "Nombre", "Teléfono", "Tipo");
        tablaClientes = Estilo.tabla(modeloTabla);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(280);
        tablaClientes.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                tablaClientesSeleccion();
            }
        });

        JPanel listado = Estilo.tarjeta(Estilo.BORDE);
        listado.setLayout(new BorderLayout(0, 16));
        listado.add(cabecera, BorderLayout.NORTH);
        listado.add(Estilo.desplazable(tablaClientes), BorderLayout.CENTER);
        listado.add(Estilo.textoSecundario("Seleccione un cliente para editarlo. Lista ordenada por nombre. "
                + "La cédula no se puede repetir."), BorderLayout.SOUTH);

        JPanel contenido = new JPanel(new BorderLayout(24, 0));
        contenido.setOpaque(false);
        contenido.add(formulario, BorderLayout.WEST);
        contenido.add(listado, BorderLayout.CENTER);
        setContenido(contenido);
    }

    // HU-05: la busqueda filtra por cedula o nombre y ordena por nombre
    private void cargarTabla() {
        clientesMostrados = sistema.getClientes().buscar(campoBuscar.getText());
        modeloTabla.setRowCount(0);
        for (Cliente c : clientesMostrados) {
            modeloTabla.addRow(new Object[]{c.getCedula(), c.getNombre(), c.getTelefono(),
                c.getTipoCliente().getNombre()});
        }
    }

    private void tablaClientesSeleccion() {
        int fila = tablaClientes.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Cliente c = clientesMostrados.get(fila);
        campoCedula.setText(c.getCedula());
        campoNombre.setText(c.getNombre());
        campoCorreo.setText(c.getCorreo());
        campoTelefono.setText(c.getTelefono());
        campoDireccion.setText(c.getDireccion());
        comboTipo.setSelectedItem(c.getTipoCliente());
        // La cedula identifica al cliente, por eso no se edita
        campoCedula.setEditable(false);
        tituloFormulario.setText("Editar cliente");
        editando = true;
    }

    private void botonGuardarActionPerformed() {
        Cliente cliente = new Cliente(campoCedula.getText().trim(), campoNombre.getText().trim(),
                campoCorreo.getText().trim(), campoTelefono.getText().trim(), campoDireccion.getText().trim(),
                (TipoCliente) comboTipo.getSelectedItem());
        try {
            if (editando) {
                sistema.getClientes().actualizar(cliente);
                Estilo.exito(this, "Cliente '" + cliente.getNombre() + "' actualizado.");
            } else {
                sistema.getClientes().agregar(cliente);
                Estilo.exito(this, "Cliente '" + cliente.getNombre() + "' registrado.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (DatoInvalidoException e) {
            Estilo.aviso(this, e.getMessage());
        } catch (IOException e) {
            Estilo.error(this, "No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        campoCedula.setText("");
        campoNombre.setText("");
        campoCorreo.setText("");
        campoTelefono.setText("");
        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        campoCedula.setEditable(true);
        tituloFormulario.setText("Registrar cliente");
        editando = false;
        tablaClientes.clearSelection();
        campoCedula.requestFocus();
    }
}
