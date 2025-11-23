/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

public record Producto(int id, String nombre, double precio, int cantidad) {
    
    public Producto {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }
    
    public Producto conCantidad(int nuevaCantidad) {
        return new Producto(this.id, this.nombre, this.precio, nuevaCantidad);
    }
    
    public Producto conPrecio(double nuevoPrecio) {
        return new Producto(this.id, this.nombre, nuevoPrecio, this.cantidad);
    }
    
    @Override
    public String toString() {
        return String.format("%-2d  | %-15s | %6.0f | %5d", id, nombre, precio, cantidad);
    }
}
