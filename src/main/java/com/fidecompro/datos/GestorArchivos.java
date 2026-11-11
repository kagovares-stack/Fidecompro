package com.fidecompro.datos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Serializa y deserializa las colecciones de la aplicacion en la carpeta "datos"
public class GestorArchivos {

    private static final File CARPETA = new File("datos");

    public static synchronized void guardar(ArrayList<?> lista, String nombreArchivo) throws IOException {
        if (!CARPETA.exists() && !CARPETA.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta " + CARPETA.getAbsolutePath());
        }
        try (ObjectOutputStream salida = new ObjectOutputStream(
                new FileOutputStream(new File(CARPETA, nombreArchivo)))) {
            salida.writeObject(lista);
        }
    }

    // Si el archivo todavia no existe devuelve una lista vacia
    @SuppressWarnings("unchecked")
    public static synchronized <T> ArrayList<T> cargar(String nombreArchivo) throws IOException, ClassNotFoundException {
        File archivo = new File(CARPETA, nombreArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ArrayList<T>) entrada.readObject();
        }
    }
}
