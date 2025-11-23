/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

public class ProductoFactory {
    private static int contadorId = 1;

    // Metodo Factory para crear productos segun tipo
    public static Producto crearProducto(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "lapiz" -> new Producto(contadorId++, "Lapiz", 1000, 50);
            case "cuaderno" -> new Producto(contadorId++, "Cuaderno", 2500, 20);
            case "borrador" -> new Producto(contadorId++, "Borrador", 800, 30);
            case "regla" -> new Producto(contadorId++, "Regla", 1500, 25);
            case "tijeras" -> new Producto(contadorId++, "Tijeras", 3000, 15);
            case "marcador" -> new Producto(contadorId++, "Marcador", 2000, 40);
            case "colores" -> new Producto(contadorId++, "Colores", 5000, 10);
            default -> throw new IllegalArgumentException("Tipo de producto no valido: " + tipo);
        };
    }

    // Metodo para crear producto personalizado
    public static Producto crearProductoPersonalizado(int id, String nombre, double precio, int cantidad) {
        return new Producto(id, nombre, precio, cantidad);
    }

    // Reiniciar contador (util para testing)
    public static void reiniciarContador() {
        contadorId = 1;
    }
}