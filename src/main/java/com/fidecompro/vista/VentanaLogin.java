package com.fidecompro.vista;

import com.fidecompro.datos.Sistema;
import com.fidecompro.excepciones.CredencialesInvalidasException;
import com.fidecompro.modelo.Usuario;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// HU-01: inicio de sesion con usuario y contrasena
public class VentanaLogin extends VentanaBase {

    private final Sistema sistema;

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JLabel etiquetaError;
    private JButton botonIngresar;

    public VentanaLogin(Sistema sistema) {
        super("Inicio de sesión");
        this.sistema = sistema;
        initComponents();
    }

    private void initComponents() {
        getContentPane().setBackground(Estilo.AZUL_FONDO);
        setLayout(new GridBagLayout());

        JLabel marca = Estilo.titulo("Fidecompro", 30);
        marca.setForeground(Estilo.AZUL);

        campoUsuario = Estilo.campoTexto();
        campoContrasena = new JPasswordField(20);
        Estilo.estilizarCampo(campoContrasena);

        botonIngresar = Estilo.botonPrimario("Ingresar");
        botonIngresar.addActionListener(evt -> botonIngresarActionPerformed());

        etiquetaError = new JLabel();
        etiquetaError.setOpaque(true);
        etiquetaError.setBackground(Estilo.ROJO_FONDO);
        etiquetaError.setForeground(Estilo.ROJO);
        etiquetaError.setFont(Estilo.fuente(Font.PLAIN, 13));
        etiquetaError.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        etiquetaError.setVisible(false);

        JPanel tarjeta = Estilo.tarjeta(Estilo.AZUL_CLARO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.AZUL_CLARO),
                BorderFactory.createEmptyBorder(36, 40, 36, 40)));
        tarjeta.setLayout(new java.awt.BorderLayout());
        tarjeta.add(Estilo.columna(18,
                Estilo.columna(4, marca, Estilo.textoSecundario("Sistema de facturación e inventario")),
                Estilo.campo("Usuario", campoUsuario),
                Estilo.campo("Contraseña", campoContrasena),
                botonIngresar,
                etiquetaError));

        add(tarjeta);
        getRootPane().setDefaultButton(botonIngresar);
    }

    private void botonIngresarActionPerformed() {
        String nombreUsuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Ingrese su usuario y contraseña.");
            return;
        }
        try {
            Usuario usuario = sistema.getUsuarios().autenticar(nombreUsuario, contrasena);
            new VentanaMenuPrincipal(sistema, usuario).setVisible(true);
            dispose();
        } catch (CredencialesInvalidasException e) {
            mostrarError(e.getMessage());
            campoContrasena.setText("");
            campoContrasena.requestFocus();
        }
    }

    private void mostrarError(String mensaje) {
        etiquetaError.setText(mensaje);
        etiquetaError.setVisible(true);
        revalidate();
    }
}
