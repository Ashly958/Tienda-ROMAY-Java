/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

public record ItemCarrito(String nombre, double precio, int cantidad) {
    
    public double getSubtotal() {
        return precio * cantidad;
    }

    @Override
    public String toString() {
        return String.format("%s x%d = %.0f COP", nombre, cantidad, getSubtotal());
    }
}