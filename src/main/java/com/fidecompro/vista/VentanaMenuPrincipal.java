package com.fidecompro.vista;

import com.fidecompro.datos.Sistema;
import com.fidecompro.hilos.HiloMonitorInventario;
import com.fidecompro.modelo.Producto;
import com.fidecompro.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// HU-02: menu principal con cierre de sesion. Las opciones dependen del rol (polimorfismo).
// HU-08: los administradores ven las alertas de stock bajo que genera HiloMonitorInventario.
public class VentanaMenuPrincipal extends VentanaBase {

    private static final int INTERVALO_MONITOR_SEGUNDOS = 30;

    private final Sistema sistema;
    private final Usuario usuario;
    private HiloMonitorInventario monitor;

    private JLabel valorClientes;
    private JLabel valorProductos;
    private JLabel valorStockBajo;
    private JPanel listaAlertas;
    private JLabel etiquetaRevision;

    public VentanaMenuPrincipal(Sistema sistema, Usuario usuario) {
        super("Menú principal");
        this.sistema = sistema;
        this.usuario = usuario;
        initComponents();
        actualizarResumen();
        if (usuario.puedeGestionarInventario()) {
            iniciarMonitor();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JButton botonCerrarSesion = Estilo.botonSecundario("Cerrar sesión");
        botonCerrarSesion.addActionListener(evt -> botonCerrarSesionActionPerformed());
        add(Estilo.encabezado(usuario.getNombre() + " (" + usuario.getRol() + ")", botonCerrarSesion),
                BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(0, 2, 20, 20));
        opciones.setOpaque(false);
        opciones.add(crearOpcion("Nueva factura", "Facturar a un cliente y exportar el archivo", true,
                evt -> Estilo.exito(this, "La facturación (HU-09 a HU-11) estará disponible en el próximo avance.")));
        opciones.add(crearOpcion("Clientes", "Registrar, buscar y editar clientes", false,
                evt -> abrirModulo(new VentanaClientes(sistema, this::alVolverDelModulo))));
        if (usuario.puedeGestionarInventario()) {
            opciones.add(crearOpcion("Productos e inventario", "Catálogo por tipo y entradas de mercadería", false,
                    evt -> abrirModulo(new VentanaProductos(sistema, this::alVolverDelModulo))));
        }
        if (usuario.puedeGestionarUsuarios()) {
            opciones.add(crearOpcion("Usuarios", "Crear y desactivar usuarios del sistema", false,
                    evt -> abrirModulo(new VentanaUsuarios(sistema, usuario, this::alVolverDelModulo))));
        }

        valorClientes = new JLabel();
        valorProductos = new JLabel();
        valorStockBajo = new JLabel();
        JPanel resumen = new JPanel(new GridLayout(1, 3, 20, 0));
        resumen.setOpaque(false);
        resumen.add(crearIndicador("Clientes registrados", valorClientes));
        resumen.add(crearIndicador("Productos en inventario", valorProductos));
        resumen.add(crearIndicador("Productos con stock bajo", valorStockBajo));

        JPanel principal = Estilo.columna(24,
                Estilo.columna(4, Estilo.titulo("Hola, " + usuario.getNombre(), 28),
                        Estilo.textoSecundario("Elija una opción para comenzar.")),
                opciones,
                resumen);

        JPanel centro = new JPanel(new BorderLayout(24, 0));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        centro.add(principal, BorderLayout.CENTER);
        if (usuario.puedeGestionarInventario()) {
            centro.add(crearPanelAlertas(), BorderLayout.EAST);
        }
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearPanelAlertas() {
        listaAlertas = new JPanel();
        listaAlertas.setLayout(new BoxLayout(listaAlertas, BoxLayout.Y_AXIS));
        listaAlertas.setOpaque(false);

        etiquetaRevision = Estilo.textoSecundario("Revisando inventario...");

        JButton botonInventario = Estilo.botonSecundario("Ir a inventario");
        botonInventario.addActionListener(evt -> abrirModulo(new VentanaProductos(sistema, this::alVolverDelModulo)));

        JPanel panel = Estilo.tarjeta(Estilo.ROJO_CLARO);
        panel.setLayout(new BorderLayout(0, 14));
        panel.setPreferredSize(new Dimension(340, 0));
        panel.add(Estilo.columna(4, Estilo.titulo("Alertas de stock bajo", 18),
                Estilo.textoSecundario("Monitor de inventario (hilo), cada " + INTERVALO_MONITOR_SEGUNDOS + " s"),
                etiquetaRevision), BorderLayout.NORTH);
        panel.add(listaAlertas, BorderLayout.CENTER);
        panel.add(botonInventario, BorderLayout.SOUTH);
        return panel;
    }

    // HU-08: el hilo corre en segundo plano y entrega cada revision a la ventana
    private void iniciarMonitor() {
        monitor = new HiloMonitorInventario(sistema.getInventario(), INTERVALO_MONITOR_SEGUNDOS,
                productos -> SwingUtilities.invokeLater(() -> mostrarAlertas(productos)));
        Thread hilo = new Thread(monitor, "monitor-inventario");
        hilo.setDaemon(true);
        hilo.start();
    }

    // Se ejecuta en el hilo de eventos de Swing (invokeLater), nunca desde el hilo del monitor
    private void mostrarAlertas(List<Producto> productos) {
        listaAlertas.removeAll();
        if (productos.isEmpty()) {
            listaAlertas.add(crearAlerta("Todo en orden", "Ningún producto está bajo su stock mínimo.",
                    Estilo.VERDE, Estilo.VERDE_FONDO));
        }
        for (Producto p : productos) {
            listaAlertas.add(crearAlerta(p.getCodigo() + " " + p.getNombre(),
                    "Stock " + p.getStock() + ", mínimo " + p.getStockMinimo(), Estilo.ROJO, Estilo.ROJO_FONDO));
            listaAlertas.add(Box.createVerticalStrut(10));
        }
        valorStockBajo.setText(String.valueOf(productos.size()));
        etiquetaRevision.setText("Última revisión: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        listaAlertas.revalidate();
        listaAlertas.repaint();
    }

    private JPanel crearAlerta(String titulo, String detalle, Color colorTitulo, Color fondo) {
        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(Estilo.fuente(Font.BOLD, 14));
        etiquetaTitulo.setForeground(colorTitulo);

        JPanel alerta = new JPanel(new BorderLayout(0, 2));
        alerta.setBackground(fondo);
        alerta.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        alerta.add(etiquetaTitulo, BorderLayout.NORTH);
        alerta.add(Estilo.textoSecundario(detalle), BorderLayout.CENTER);
        alerta.setAlignmentX(LEFT_ALIGNMENT);
        alerta.setMaximumSize(new Dimension(Integer.MAX_VALUE, alerta.getPreferredSize().height));
        return alerta;
    }

    // Boton grande del menu; la opcion principal va en azul como en el prototipo
    private JButton crearOpcion(String titulo, String descripcion, boolean principal, ActionListener accion) {
        JButton opcion = principal ? Estilo.botonPrimario("") : Estilo.botonSecundario("");
        Color colorDescripcion = principal ? Estilo.AZUL_CLARO : Estilo.TEXTO_SECUNDARIO;
        String colorTitulo = principal ? "#ffffff" : "#1a1a1a";
        opcion.setText("<html><div style='font-size:17px;color:" + colorTitulo + "'><b>" + titulo + "</b></div>"
                + "<div style='font-size:12px;margin-top:8px;color:#" + Integer.toHexString(colorDescripcion.getRGB()).substring(2)
                + "'>" + descripcion + "</div></html>");
        opcion.setFont(Estilo.fuente(Font.PLAIN, 14));
        opcion.setHorizontalAlignment(SwingConstants.LEFT);
        opcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(principal ? Estilo.AZUL : Estilo.BORDE),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        opcion.addActionListener(accion);
        return opcion;
    }

    private JPanel crearIndicador(String titulo, JLabel valor) {
        valor.setFont(Estilo.fuente(Font.BOLD, 28));
        valor.setForeground(Estilo.TEXTO);
        JPanel tarjeta = Estilo.tarjeta(Estilo.BORDE);
        tarjeta.setLayout(new BorderLayout(0, 4));
        tarjeta.add(Estilo.textoSecundario(titulo), BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);
        return tarjeta;
    }

    // El menu se oculta mientras el modulo esta abierto y reaparece al volver
    private void abrirModulo(VentanaModulo modulo) {
        setVisible(false);
        modulo.setVisible(true);
    }

    private void alVolverDelModulo() {
        actualizarResumen();
        // Al volver de inventario las alertas se refrescan sin esperar la siguiente revision del hilo
        if (monitor != null) {
            mostrarAlertas(sistema.getInventario().listarStockBajo());
        }
        setVisible(true);
    }

    private void actualizarResumen() {
        valorClientes.setText(String.valueOf(sistema.getClientes().cantidad()));
        valorProductos.setText(String.valueOf(sistema.getInventario().cantidad()));
        valorStockBajo.setText(String.valueOf(sistema.getInventario().listarStockBajo().size()));
    }

    // HU-02: cierra el menu y vuelve al inicio de sesion con los campos vacios
    private void botonCerrarSesionActionPerformed() {
        if (monitor != null) {
            monitor.detener();
        }
        dispose();
        new VentanaLogin(sistema).setVisible(true);
    }
}
