package com.fidecompro.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// Colores y componentes comunes para que todas las ventanas sigan el prototipo
public class Estilo {

    public static final Color AZUL = new Color(0x305BAB);
    public static final Color AZUL_CLARO = new Color(0xC6DCFF);
    public static final Color AZUL_FONDO = new Color(0xF0F5FD);
    public static final Color ROJO = new Color(0xBD0A0A);
    public static final Color ROJO_CLARO = new Color(0xFFC6C6);
    public static final Color ROJO_FONDO = new Color(0xFFF0F0);
    public static final Color VERDE = new Color(0x067429);
    public static final Color VERDE_FONDO = new Color(0xEAF9EF);
    public static final Color FONDO = new Color(0xF7F7F7);
    public static final Color BORDE = new Color(0xE7E7E7);
    public static final Color BORDE_CAMPO = new Color(0xB0B0B0);
    public static final Color TEXTO = new Color(0x1A1A1A);
    public static final Color TEXTO_SECUNDARIO = new Color(0x595959);

    private static final String FUENTE = "SansSerif";

    // El look and feel multiplataforma (Metal) respeta los colores de fondo de los botones
    public static void aplicarLookAndFeel() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo aplicar el look and feel: " + e.getMessage());
        }
        Font base = fuente(Font.PLAIN, 14);
        String[] claves = {"Label.font", "Button.font", "TextField.font", "PasswordField.font", "ComboBox.font",
            "CheckBox.font", "ToggleButton.font", "Table.font", "TableHeader.font", "OptionPane.messageFont",
            "OptionPane.buttonFont", "List.font"};
        for (String clave : claves) {
            UIManager.put(clave, base);
        }
    }

    public static Font fuente(int estilo, int tamano) {
        return new Font(FUENTE, estilo, tamano);
    }

    public static JLabel titulo(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuente(Font.BOLD, tamano));
        etiqueta.setForeground(TEXTO);
        return etiqueta;
    }

    public static JLabel textoSecundario(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuente(Font.PLAIN, 13));
        etiqueta.setForeground(TEXTO_SECUNDARIO);
        return etiqueta;
    }

    public static JButton botonPrimario(String texto) {
        return boton(texto, AZUL, Color.WHITE, AZUL);
    }

    public static JButton botonSecundario(String texto) {
        return boton(texto, Color.WHITE, AZUL, AZUL);
    }

    private static JButton boton(String texto, Color fondo, Color letra, Color borde) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente(Font.BOLD, 14));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde),
                BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    // Panel blanco con borde, usado como "tarjeta" en todas las pantallas
    public static JPanel tarjeta(Color borde) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)));
        return panel;
    }

    // Barra azul superior con el nombre de la aplicacion y una accion a la derecha
    public static JPanel encabezado(String textoDerecha, JButton boton) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(AZUL);
        barra.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));

        JLabel marca = new JLabel("Fidecompro");
        marca.setFont(fuente(Font.BOLD, 20));
        marca.setForeground(Color.WHITE);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        derecha.setOpaque(false);
        if (textoDerecha != null) {
            JLabel texto = new JLabel(textoDerecha);
            texto.setForeground(Color.WHITE);
            derecha.add(texto);
        }
        derecha.add(boton);

        barra.add(marca, BorderLayout.WEST);
        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    public static JTextField campoTexto() {
        JTextField campo = new JTextField(20);
        estilizarCampo(campo);
        return campo;
    }

    public static void estilizarCampo(JComponent campo) {
        campo.setFont(fuente(Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_CAMPO),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    // Etiqueta en negrita encima de un componente de entrada
    public static JPanel campo(String etiqueta, JComponent componente) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel texto = new JLabel(etiqueta);
        texto.setFont(fuente(Font.BOLD, 13));
        texto.setForeground(TEXTO);
        panel.add(texto, BorderLayout.NORTH);
        panel.add(componente, BorderLayout.CENTER);
        return panel;
    }

    // Apila componentes de arriba hacia abajo ocupando todo el ancho
    public static JPanel columna(int espacio, Component... componentes) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        for (int i = 0; i < componentes.length; i++) {
            c.gridy = i;
            c.insets = new Insets(0, 0, i < componentes.length - 1 ? espacio : 0, 0);
            panel.add(componentes[i], c);
        }
        // Relleno final para que el contenido quede pegado arriba
        c.gridy = componentes.length;
        c.weighty = 1;
        panel.add(new JPanel() {
            {
                setOpaque(false);
            }
        }, c);
        return panel;
    }

    public static DefaultTableModel modeloTabla(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    public static JTable tabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(BORDE);
        tabla.setSelectionBackground(AZUL_FONDO);
        tabla.setSelectionForeground(TEXTO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.setDefaultRenderer(Object.class, new RenderizadorCelda());
        JTableHeader cabecera = tabla.getTableHeader();
        if (cabecera.getDefaultRenderer() instanceof DefaultTableCellRenderer renderizador) {
            renderizador.setHorizontalAlignment(SwingConstants.LEFT);
        }
        cabecera.setFont(fuente(Font.BOLD, 13));
        cabecera.setBackground(Color.WHITE);
        cabecera.setForeground(TEXTO_SECUNDARIO);
        cabecera.setReorderingAllowed(false);
        return tabla;
    }

    // Celda con margen interno; las ventanas la extienden para colorear filas o columnas
    public static class RenderizadorCelda extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionado, foco, fila, columna);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 4));
            setFont(fuente(Font.PLAIN, 14));
            setForeground(TEXTO);
            setBackground(seleccionado ? AZUL_FONDO : Color.WHITE);
            return this;
        }
    }

    public static <T> JComboBox<T> combo(T[] opciones) {
        JComboBox<T> combo = new JComboBox<>(opciones);
        combo.setBackground(Color.WHITE);
        combo.setFont(fuente(Font.PLAIN, 14));
        return combo;
    }

    public static JScrollPane desplazable(JComponent componente) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public static String moneda(double monto) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(' ');
        simbolos.setDecimalSeparator(',');
        return new DecimalFormat("₡#,##0.00", simbolos).format(monto);
    }

    public static void exito(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Fidecompro", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void aviso(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Revise los datos", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
