package com.fidecompro.vista;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

// Ventana que se abre desde el menu principal y tiene el boton "Volver al menu"
public abstract class VentanaModulo extends VentanaBase {

    private final Runnable alVolver;

    protected VentanaModulo(String titulo, Runnable alVolver) {
        super(titulo);
        this.alVolver = alVolver;
        setLayout(new BorderLayout());

        JButton botonVolver = Estilo.botonSecundario("Volver al menú");
        botonVolver.addActionListener(evt -> volver());
        add(Estilo.encabezado(null, botonVolver), BorderLayout.NORTH);

        // Cerrar la ventana con la X tambien regresa al menu
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                volver();
            }
        });
    }

    protected void setContenido(JComponent contenido) {
        contenido.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        add(contenido, BorderLayout.CENTER);
    }

    private void volver() {
        dispose();
        alVolver.run();
    }
}
