package com.fidecompro.vista;

import com.fidecompro.datos.Sistema;
import com.fidecompro.datos.Validar;
import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.excepciones.ProductoNoEncontradoException;
import com.fidecompro.modelo.Producto;
import com.fidecompro.modelo.ProductoAlimenticio;
import com.fidecompro.modelo.ProductoElectronico;
import com.fidecompro.modelo.ProductoLimpieza;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

// HU-06: registrar productos por tipo. HU-07: entradas de mercaderia y cambio de precios.
public class VentanaProductos extends VentanaModulo {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ALIMENTICIO = "Alimenticio";
    private static final String LIMPIEZA = "Limpieza";
    private static final String ELECTRONICO = "Electrónico";

    private final Sistema sistema;
    private List<Producto> productosMostrados = new ArrayList<>();

    private JToggleButton botonAlimenticio;
    private JToggleButton botonLimpieza;
    private JToggleButton botonElectronico;
    private JTextField campoCodigo;
    private JTextField campoPrecio;
    private JTextField campoNombre;
    private JTextField campoStock;
    private JTextField campoStockMinimo;
    private JPanel panelEspecifico;
    private JLabel tituloEspecifico;
    private JTextField campoVencimiento;
    private JCheckBox checkCanastaBasica;
    private JTextField campoPresentacion;
    private JCheckBox checkToxico;
    private JTextField campoMarca;
    private JTextField campoGarantia;

    private DefaultTableModel modeloTabla;
    private JTable tablaInventario;
    private JTextField campoCodigoMovimiento;
    private JTextField campoCantidad;
    private JTextField campoNuevoPrecio;

    public VentanaProductos(Sistema sistema, Runnable alVolver) {
        super("Productos e inventario", alVolver);
        this.sistema = sistema;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        JPanel contenido = new JPanel(new BorderLayout(24, 0));
        contenido.setOpaque(false);
        contenido.add(crearFormulario(), BorderLayout.WEST);
        contenido.add(crearPanelInventario(), BorderLayout.CENTER);
        setContenido(contenido);
        seleccionarTipo(ALIMENTICIO);
    }

    private JPanel crearFormulario() {
        botonAlimenticio = crearBotonTipo(ALIMENTICIO);
        botonLimpieza = crearBotonTipo(LIMPIEZA);
        botonElectronico = crearBotonTipo(ELECTRONICO);
        ButtonGroup grupo = new ButtonGroup();
        JPanel tipos = new JPanel(new GridLayout(1, 3));
        tipos.setOpaque(false);
        for (JToggleButton b : new JToggleButton[]{botonAlimenticio, botonLimpieza, botonElectronico}) {
            grupo.add(b);
            tipos.add(b);
        }

        campoCodigo = Estilo.campoTexto();
        campoPrecio = Estilo.campoTexto();
        campoNombre = Estilo.campoTexto();
        campoStock = Estilo.campoTexto();
        campoStockMinimo = Estilo.campoTexto();

        JPanel codigoPrecio = dosColumnas(Estilo.campo("Código", campoCodigo), Estilo.campo("Precio (₡)", campoPrecio));
        JPanel existencias = dosColumnas(Estilo.campo("Stock inicial", campoStock),
                Estilo.campo("Stock mínimo", campoStockMinimo));

        JButton botonGuardar = Estilo.botonPrimario("Guardar producto");
        botonGuardar.addActionListener(evt -> botonGuardarActionPerformed());

        JPanel formulario = Estilo.tarjeta(Estilo.BORDE);
        formulario.setLayout(new BorderLayout());
        formulario.setPreferredSize(new Dimension(420, 0));
        formulario.add(Estilo.columna(14,
                Estilo.titulo("Registrar producto", 22),
                Estilo.campo("Tipo de producto", tipos),
                codigoPrecio,
                Estilo.campo("Nombre", campoNombre),
                existencias,
                crearPanelEspecifico(),
                botonGuardar));
        return formulario;
    }

    // Campos propios de cada tipo de producto; se cambia de tarjeta segun el tipo elegido
    private JPanel crearPanelEspecifico() {
        campoVencimiento = Estilo.campoTexto();
        campoVencimiento.setToolTipText("dd/mm/aaaa");
        checkCanastaBasica = crearCheck("Canasta básica (IVA 1 %)");
        campoPresentacion = Estilo.campoTexto();
        campoPresentacion.setToolTipText("Ejemplo: 3.8 L o 5 kg");
        checkToxico = crearCheck("Producto tóxico");
        campoMarca = Estilo.campoTexto();
        campoGarantia = Estilo.campoTexto();

        panelEspecifico = new JPanel(new CardLayout());
        panelEspecifico.setOpaque(false);
        panelEspecifico.add(Estilo.columna(6, Estilo.campo("Vencimiento (dd/mm/aaaa)", campoVencimiento),
                checkCanastaBasica), ALIMENTICIO);
        panelEspecifico.add(Estilo.columna(6, Estilo.campo("Presentación", campoPresentacion),
                checkToxico), LIMPIEZA);
        panelEspecifico.add(dosColumnas(Estilo.campo("Marca", campoMarca),
                Estilo.campo("Garantía (meses)", campoGarantia)), ELECTRONICO);

        tituloEspecifico = new JLabel();
        tituloEspecifico.setFont(Estilo.fuente(Font.BOLD, 14));
        tituloEspecifico.setForeground(Estilo.AZUL);

        JPanel contenedor = new JPanel(new BorderLayout(0, 10));
        contenedor.setBackground(Estilo.AZUL_FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        contenedor.add(tituloEspecifico, BorderLayout.NORTH);
        contenedor.add(panelEspecifico, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearPanelInventario() {
        modeloTabla = Estilo.modeloTabla("Código", "Nombre", "Tipo", "Precio", "IVA", "Stock", "Mínimo");
        tablaInventario = Estilo.tabla(modeloTabla);
        tablaInventario.setDefaultRenderer(Object.class, new RenderizadorInventario());
        int[] anchos = {85, 190, 100, 105, 55, 55, 60};
        for (int i = 0; i < anchos.length; i++) {
            tablaInventario.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        tablaInventario.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                tablaInventarioSeleccion();
            }
        });

        JPanel inventario = Estilo.tarjeta(Estilo.BORDE);
        inventario.setLayout(new BorderLayout(0, 14));
        inventario.add(Estilo.titulo("Inventario", 22), BorderLayout.NORTH);
        inventario.add(Estilo.desplazable(tablaInventario), BorderLayout.CENTER);
        inventario.add(Estilo.textoSecundario("En rojo: stock por debajo del mínimo. "
                + "Seleccione una fila para registrar una entrada."), BorderLayout.SOUTH);

        campoCodigoMovimiento = Estilo.campoTexto();
        campoCantidad = Estilo.campoTexto();
        campoNuevoPrecio = Estilo.campoTexto();
        JButton botonSumar = Estilo.botonPrimario("Sumar al stock");
        botonSumar.addActionListener(evt -> botonSumarActionPerformed());
        JButton botonPrecio = Estilo.botonSecundario("Actualizar precio");
        botonPrecio.addActionListener(evt -> botonPrecioActionPerformed());

        JPanel campos = new JPanel(new GridLayout(1, 3, 12, 0));
        campos.setOpaque(false);
        campos.add(Estilo.campo("Código", campoCodigoMovimiento));
        campos.add(Estilo.campo("Unidades que entran", campoCantidad));
        campos.add(Estilo.campo("Nuevo precio (₡)", campoNuevoPrecio));
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.setOpaque(false);
        acciones.add(botonPrecio);
        acciones.add(Box.createHorizontalStrut(12));
        acciones.add(botonSumar);

        JPanel movimientos = Estilo.tarjeta(Estilo.BORDE);
        movimientos.setLayout(new BorderLayout(0, 12));
        movimientos.add(Estilo.titulo("Entrada de mercadería y precios", 18), BorderLayout.NORTH);
        movimientos.add(campos, BorderLayout.CENTER);
        movimientos.add(acciones, BorderLayout.SOUTH);

        JPanel derecha = new JPanel(new BorderLayout(0, 20));
        derecha.setOpaque(false);
        derecha.add(inventario, BorderLayout.CENTER);
        derecha.add(movimientos, BorderLayout.SOUTH);
        return derecha;
    }

    private void cargarTabla() {
        productosMostrados = sistema.getInventario().getProductos();
        modeloTabla.setRowCount(0);
        for (Producto p : productosMostrados) {
            modeloTabla.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getTipo(), Estilo.moneda(p.getPrecio()),
                Math.round(p.getTasaImpuesto() * 100) + " %", p.getStock(), p.getStockMinimo()});
        }
    }

    private void seleccionarTipo(String tipo) {
        botonAlimenticio.setSelected(ALIMENTICIO.equals(tipo));
        botonLimpieza.setSelected(LIMPIEZA.equals(tipo));
        botonElectronico.setSelected(ELECTRONICO.equals(tipo));
        for (JToggleButton b : new JToggleButton[]{botonAlimenticio, botonLimpieza, botonElectronico}) {
            b.setBackground(b.isSelected() ? Estilo.AZUL : Color.WHITE);
            b.setForeground(b.isSelected() ? Color.WHITE : Estilo.TEXTO);
        }
        tituloEspecifico.setText("Datos de producto " + tipo.toLowerCase());
        ((CardLayout) panelEspecifico.getLayout()).show(panelEspecifico, tipo);
    }

    // HU-06: se crea la subclase de Producto que corresponde al tipo seleccionado
    private Producto crearProducto() throws DatoInvalidoException {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        double precio = leerDecimal(campoPrecio, "Precio");
        int stock = leerEntero(campoStock, "Stock inicial");
        int minimo = leerEntero(campoStockMinimo, "Stock mínimo");

        if (botonAlimenticio.isSelected()) {
            return new ProductoAlimenticio(codigo, nombre, precio, stock, minimo,
                    leerFecha(campoVencimiento), checkCanastaBasica.isSelected());
        }
        if (botonLimpieza.isSelected()) {
            Validar.requerido(campoPresentacion.getText(), "Presentación");
            return new ProductoLimpieza(codigo, nombre, precio, stock, minimo,
                    campoPresentacion.getText().trim(), checkToxico.isSelected());
        }
        Validar.requerido(campoMarca.getText(), "Marca");
        int garantia = leerEntero(campoGarantia, "Garantía");
        Validar.noNegativo(garantia, "Garantía");
        return new ProductoElectronico(codigo, nombre, precio, stock, minimo, campoMarca.getText().trim(), garantia);
    }

    private void botonGuardarActionPerformed() {
        try {
            Producto nuevo = crearProducto();
            sistema.getInventario().agregarProducto(nuevo);
            Estilo.exito(this, "Producto '" + nuevo.getNombre() + "' registrado como " + nuevo.getTipo().toLowerCase() + ".");
            limpiarFormulario();
            cargarTabla();
        } catch (DatoInvalidoException e) {
            Estilo.aviso(this, e.getMessage());
        } catch (IOException e) {
            Estilo.error(this, "No se pudo guardar el producto: " + e.getMessage());
        }
    }

    // HU-07: suma unidades al stock del producto indicado
    private void botonSumarActionPerformed() {
        String codigo = campoCodigoMovimiento.getText().trim();
        try {
            Validar.requerido(codigo, "Código");
            int cantidad = leerEntero(campoCantidad, "Unidades que entran");
            sistema.getInventario().registrarEntrada(codigo, cantidad);
            Producto p = sistema.getInventario().buscarPorCodigo(codigo);
            Estilo.exito(this, "Se sumaron " + cantidad + " unidades a " + p.getNombre() + ". Stock actual: " + p.getStock() + ".");
            campoCantidad.setText("");
            cargarTabla();
        } catch (ProductoNoEncontradoException | DatoInvalidoException e) {
            Estilo.aviso(this, e.getMessage());
        } catch (IOException e) {
            Estilo.error(this, "No se pudo guardar la entrada: " + e.getMessage());
        }
    }

    // HU-07: cambia el precio del producto indicado
    private void botonPrecioActionPerformed() {
        String codigo = campoCodigoMovimiento.getText().trim();
        try {
            Validar.requerido(codigo, "Código");
            double precio = leerDecimal(campoNuevoPrecio, "Nuevo precio");
            sistema.getInventario().cambiarPrecio(codigo, precio);
            Estilo.exito(this, "Precio de " + codigo + " actualizado a " + Estilo.moneda(precio) + ".");
            cargarTabla();
        } catch (ProductoNoEncontradoException | DatoInvalidoException e) {
            Estilo.aviso(this, e.getMessage());
        } catch (IOException e) {
            Estilo.error(this, "No se pudo guardar el precio: " + e.getMessage());
        }
    }

    private void tablaInventarioSeleccion() {
        int fila = tablaInventario.getSelectedRow();
        if (fila >= 0) {
            Producto p = productosMostrados.get(fila);
            campoCodigoMovimiento.setText(p.getCodigo());
            campoNuevoPrecio.setText(String.valueOf(p.getPrecio()));
        }
    }

    private void limpiarFormulario() {
        for (JTextField campo : new JTextField[]{campoCodigo, campoPrecio, campoNombre, campoStock, campoStockMinimo,
            campoVencimiento, campoPresentacion, campoMarca, campoGarantia}) {
            campo.setText("");
        }
        checkCanastaBasica.setSelected(false);
        checkToxico.setSelected(false);
        campoCodigo.requestFocus();
    }

    private double leerDecimal(JTextField campo, String nombre) throws DatoInvalidoException {
        String texto = campo.getText().trim().replace("₡", "").replace(" ", "").replace(",", ".");
        Validar.requerido(texto, nombre);
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El campo " + nombre + " debe ser un número.");
        }
    }

    private int leerEntero(JTextField campo, String nombre) throws DatoInvalidoException {
        String texto = campo.getText().trim();
        Validar.requerido(texto, nombre);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El campo " + nombre + " debe ser un número entero.");
        }
    }

    private LocalDate leerFecha(JTextField campo) throws DatoInvalidoException {
        Validar.requerido(campo.getText(), "Vencimiento");
        try {
            return LocalDate.parse(campo.getText().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException("La fecha de vencimiento debe tener el formato dd/mm/aaaa.");
        }
    }

    private JToggleButton crearBotonTipo(String tipo) {
        JToggleButton boton = new JToggleButton(tipo);
        boton.setFont(Estilo.fuente(Font.BOLD, 13));
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_CAMPO),
                BorderFactory.createEmptyBorder(8, 4, 8, 4)));
        boton.addActionListener(evt -> seleccionarTipo(tipo));
        return boton;
    }

    private JCheckBox crearCheck(String texto) {
        JCheckBox check = new JCheckBox(texto);
        check.setOpaque(false);
        check.setFont(Estilo.fuente(Font.PLAIN, 13));
        return check;
    }

    private JPanel dosColumnas(Component izquierda, Component derecha) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setOpaque(false);
        panel.add(izquierda);
        panel.add(derecha);
        return panel;
    }

    // Pinta en rojo las filas de productos con stock bajo
    private class RenderizadorInventario extends Estilo.RenderizadorCelda {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionado, foco, fila, columna);
            if (productosMostrados.get(fila).tieneStockBajo()) {
                if (!seleccionado) {
                    setBackground(Estilo.ROJO_FONDO);
                }
                if (columna == 5) {
                    setForeground(Estilo.ROJO);
                    setFont(Estilo.fuente(Font.BOLD, 14));
                }
            }
            return this;
        }
    }
}
