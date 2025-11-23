/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

import java.util.ArrayList;
import java.util.List;

public record Ticket(List<ItemCarrito> items, double total, String fecha) {
    
    public Ticket {
        items = new ArrayList<>(items);
    }

    public void mostrar() {
        System.out.println("""
                ----- TICKET DE COMPRA -----
                """);
        
        items.forEach(System.out::println);
        
        System.out.printf("""
                TOTAL: %.0f COP
                Fecha: %s
                -----------------------------
                """, total, fecha);
    }
}