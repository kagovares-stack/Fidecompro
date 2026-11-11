# Fidecompro

Proyecto final del curso Programación Cliente/Servidor Concurrente, hecho por Kristel Amanda Garcia Ovares.

Es una aplicación de escritorio en Java Swing para Fidecompro, una cadena de venta al por mayor. La idea es poder facturar y llevar el control del inventario desde el mismo programa.


## Cómo correrlo

Se abre la carpeta del proyecto en NetBeans (File > Open Project) y se le da Run. La clase principal es com.fidecompro.Main. La primera vez que se ejecuta, el programa crea una carpeta llamada datos con algunos datos de ejemplo para poder probarlo de una vez.

Para entrar se puede usar el usuario admin con la contraseña admin123, que tiene rol de administrador, o el usuario jmora con la contraseña vende123, que entra como vendedor.


## Cómo está organizado

El código está dividido en cinco paquetes. En modelo están las clases del dominio (personas, usuarios, clientes y productos), y ahí es donde se aplica la herencia y el polimorfismo. En excepciones van las excepciones propias de la aplicación. El paquete datos se encarga de las colecciones y de guardar la información usando serialización. En hilos está el monitor de inventario, que corre en segundo plano mientras se usa el programa. Y en vista están todas las ventanas hechas con Swing.


## Avance

Para el avance 2 ya quedaron listas las historias de usuario HU-01 a HU-08: iniciar y cerrar sesión, que el administrador pueda crear y desactivar usuarios, registrar clientes y también buscarlos y editarlos, registrar productos según su tipo, las entradas de mercadería con los cambios de precio, y la alerta automática cuando un producto queda con poco stock, que funciona con un hilo.

Lo que falta para la entrega final (HU-09 a HU-12) es toda la parte de facturación: crear la factura, validar que haya existencias suficientes al momento de facturar, exportar la factura a un archivo y llevar un historial de las facturas hechas.