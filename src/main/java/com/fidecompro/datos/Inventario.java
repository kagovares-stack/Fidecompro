package com.fidecompro.datos;

import com.fidecompro.excepciones.DatoInvalidoException;
import com.fidecompro.excepciones.ProductoNoEncontradoException;
import com.fidecompro.modelo.Producto;
import com.fidecompro.modelo.ProductoAlimenticio;
import com.fidecompro.modelo.ProductoElectronico;
import com.fidecompro.modelo.ProductoLimpieza;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Coleccion de productos. Los metodos son synchronized porque el hilo
// HiloMonitorInventario la consulta mientras las ventanas la modifican.
public class Inventario {

    private static final String ARCHIVO = "productos.dat";

    private final ArrayList<Producto> productos;

    public Inventario() throws IOException, ClassNotFoundException {
        productos = GestorArchivos.cargar(ARCHIVO);
        if (productos.isEmpty()) {
            productos.add(new ProductoAlimenticio("A-1001", "Arroz Tío Pelón 2 kg", 2150, 340, 100,
                    LocalDate.of(2027, 6, 30), true));
            productos.add(new ProductoAlimenticio("A-1014", "Aceite Clover 1 L", 1980, 11, 30,
                    LocalDate.of(2027, 3, 15), true));
            productos.add(new ProductoLimpieza("L-2005", "Cloro Irex 3.8 L", 1890, 6, 20, "3.8 L", true));
            productos.add(new ProductoLimpieza("L-2011", "Jabón en polvo Xedex 5 kg", 7650, 58, 15, "5 kg", false));
            productos.add(new ProductoElectronico("E-3002", "Licuadora Oster 600 W", 32500, 4, 5, "Oster", 12));
            productos.add(new ProductoElectronico("E-3010", "Arrocera Black+Decker 1.2 L", 18900, 22, 5, "Black+Decker", 6));
            guardar();
        }
    }

    // HU-06: registra un producto nuevo con codigo unico
    public synchronized void agregarProducto(Producto nuevo) throws DatoInvalidoException, IOException {
        Validar.requerido(nuevo.getCodigo(), "Código");
        Validar.requerido(nuevo.getNombre(), "Nombre");
        Validar.positivo(nuevo.getPrecio(), "Precio");
        Validar.noNegativo(nuevo.getStock(), "Stock inicial");
        Validar.noNegativo(nuevo.getStockMinimo(), "Stock mínimo");
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(nuevo.getCodigo())) {
                throw new DatoInvalidoException("Ya existe un producto con el código " + nuevo.getCodigo() + ".");
            }
        }
        productos.add(nuevo);
        guardar();
    }

    public synchronized Producto buscarPorCodigo(String codigo) throws ProductoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException(codigo);
    }

    // HU-07: suma unidades al stock de un producto existente
    public synchronized void registrarEntrada(String codigo, int cantidad)
            throws ProductoNoEncontradoException, DatoInvalidoException, IOException {
        buscarPorCodigo(codigo).aumentarStock(cantidad);
        guardar();
    }

    // HU-07: cambia el precio de un producto existente
    public synchronized void cambiarPrecio(String codigo, double nuevoPrecio)
            throws ProductoNoEncontradoException, DatoInvalidoException, IOException {
        buscarPorCodigo(codigo).setPrecio(nuevoPrecio);
        guardar();
    }

    // HU-08: productos por debajo de su stock minimo
    public synchronized List<Producto> listarStockBajo() {
        ArrayList<Producto> bajos = new ArrayList<>();
        for (Producto p : productos) {
            if (p.tieneStockBajo()) {
                bajos.add(p);
            }
        }
        return bajos;
    }

    // Copia ordenada por codigo (Comparator) para que la tabla no dependa de la coleccion interna
    public synchronized List<Producto> getProductos() {
        ArrayList<Producto> copia = new ArrayList<>(productos);
        copia.sort(Comparator.comparing(Producto::getCodigo));
        return copia;
    }

    public synchronized int cantidad() {
        return productos.size();
    }

    private void guardar() throws IOException {
        GestorArchivos.guardar(productos, ARCHIVO);
    }
}
