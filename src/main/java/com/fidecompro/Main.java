package com.fidecompro;

import com.fidecompro.datos.Sistema;
import com.fidecompro.vista.Estilo;
import com.fidecompro.vista.VentanaLogin;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estilo.aplicarLookAndFeel();
            try {
                new VentanaLogin(new Sistema()).setVisible(true);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "No se pudieron cargar los datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
