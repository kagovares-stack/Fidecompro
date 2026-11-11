package com.fidecompro.vista;

import java.awt.Dimension;
import javax.swing.JFrame;

// Configuracion comun de todas las ventanas de la aplicacion
public abstract class VentanaBase extends JFrame {

    protected VentanaBase(String titulo) {
        super("Fidecompro - " + titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(1050, 680));
        getContentPane().setBackground(Estilo.FONDO);
        setLocationRelativeTo(null);
    }
}
