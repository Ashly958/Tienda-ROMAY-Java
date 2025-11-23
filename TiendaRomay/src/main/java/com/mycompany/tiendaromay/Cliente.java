/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Cliente {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<ItemCarrito> carrito = new ArrayList<>();
    private static final JsonManager jsonManager = JsonManager.getInstance();

    public static void tienda() {
        var productos = new ArrayList<>(jsonManager.cargarProductos());
        
        System.out.println("Bienvenido a la Tienda Romay");
        
        mostrarProductos(productos);
        carrito.clear();

        while (true) {
            System.out.print("\nQue producto desea comprar? (ID o 'salir'): ");
            var opcion = scanner.nextLine().toLowerCase().trim();
            
            if (opcion.equals("salir")) {
                System.out.println("Compra cancelada.\n");
                return;
            }

            try {
                var productoId = Integer.parseInt(opcion);
                var productoOpt = buscarProducto(productos, productoId);
                
                if (productoOpt.isPresent()) {
                    var producto = productoOpt.get();
                    while (true) {
                        try {
                            System.out.print("Cuantas unidades?: ");
                            var cantidad = Integer.parseInt(scanner.nextLine());
                            
                            if (cantidad <= 0) {
                                System.out.println("Cantidad debe ser mayor a cero.");
                            } else if (cantidad <= producto.cantidad()) {
                                agregarAlCarrito(productos, producto, cantidad);
                                break;
                            } else {
                                System.out.println("No hay suficiente stock.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ingrese un numero.");
                        }
                    }
                } else {
                    System.out.println("Producto no encontrado.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no valida.");
            }

            String continuar;
            while (true) {
                System.out.print("Desea agregar mas productos? (s/n): ");
                continuar = scanner.nextLine().toLowerCase().trim();
                if (continuar.equals("s") || continuar.equals("n")) {
                    break;
                } else {
                    System.out.println("Seleccione (s/n).");
                }
            }

            if (continuar.equals("n")) {
                break;
            }
        }

        if (!carrito.isEmpty()) {
            var ticket = verTicket();
            System.out.print("Desea pagar? (s/n): ");
            var pagar = scanner.nextLine().toLowerCase().trim();
            
            if (pagar.equals("s")) {
                jsonManager.guardarTicket(ticket);
                jsonManager.guardarProductos(productos);
                System.out.println("Gracias por su compra.\n");
            } else {
                System.out.println("Compra cancelada.\n");
                devolverStock(productos);
            }
        } else {
            System.out.println("No se agrego ningun producto.");
        }
    }

    private static void mostrarProductos(List<Producto> productos) {
        System.out.println("""
                Lista de Productos:
                ID  | Nombre          |Precio/u| Cantidad
                -------------------------------------------
                """);
        productos.forEach(System.out::println);
    }

    private static Optional<Producto> buscarProducto(List<Producto> productos, int id) {
        return productos.stream()
                .filter(p -> p.id() == id)
                .findFirst();
    }

    private static void agregarAlCarrito(List<Producto> productos, Producto producto, int cantidad) {
        carrito.add(new ItemCarrito(producto.nombre(), producto.precio(), cantidad));
        
        var index = productos.indexOf(producto);
        productos.set(index, producto.conCantidad(producto.cantidad() - cantidad));
        
        System.out.printf("%d unidad(es) de %s aniadidas al carrito.%n", 
                cantidad, producto.nombre());
    }

    private static Ticket verTicket() {
        var total = carrito.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
        
        var fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        var ticket = new Ticket(carrito, total, fecha);
        ticket.mostrar();
        return ticket;
    }

    private static void devolverStock(List<Producto> productos) {
        for (var item : carrito) {
            for (int i = 0; i < productos.size(); i++) {
                var p = productos.get(i);
                if (p.nombre().equals(item.nombre())) {
                    productos.set(i, p.conCantidad(p.cantidad() + item.cantidad()));
                    break;
                }
            }
        }
    }
}