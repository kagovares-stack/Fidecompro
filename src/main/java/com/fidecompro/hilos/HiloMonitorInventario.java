package com.fidecompro.hilos;

import com.fidecompro.datos.Inventario;
import com.fidecompro.modelo.Producto;
import java.util.List;
import java.util.function.Consumer;

// HU-08: revisa el inventario en segundo plano cada cierto tiempo y avisa
// cuales productos estan por debajo de su stock minimo
public class HiloMonitorInventario implements Runnable {

    private final Inventario inventario;
    private final int intervaloSegundos;
    private final Consumer<List<Producto>> alRevisar;
    private volatile boolean activo = true;
    private volatile Thread hilo;

    // alRevisar recibe la lista de productos con stock bajo en cada revision
    public HiloMonitorInventario(Inventario inventario, int intervaloSegundos, Consumer<List<Producto>> alRevisar) {
        this.inventario = inventario;
        this.intervaloSegundos = intervaloSegundos;
        this.alRevisar = alRevisar;
    }

    @Override
    public void run() {
        hilo = Thread.currentThread();
        while (activo) {
            // listarStockBajo es synchronized: no choca con los cambios que hacen las ventanas
            alRevisar.accept(inventario.listarStockBajo());
            try {
                Thread.sleep(intervaloSegundos * 1000L);
            } catch (InterruptedException e) {
                // detener() interrumpe la espera para terminar de inmediato
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void detener() {
        activo = false;
        if (hilo != null) {
            hilo.interrupt();
        }
    }

    public boolean isActivo() {
        return activo;
    }
}
