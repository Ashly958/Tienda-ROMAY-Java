/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

import java.util.*;
import java.util.stream.Collectors;

public class Administrador {
    private static final Scanner scanner = new Scanner(System.in);
    private static final JsonManager jsonManager = JsonManager.getInstance();

    public static boolean autenticarUsuario() {
        var intentos = 3;
        var usuarios = jsonManager.cargarUsuarios();

        while (intentos > 0) {
            System.out.println("""
                    --- INGRESE SUS CREDENCIALES ---
                    Recuerde ingresar los espacios correctamente.
                    """);
            
            System.out.print("Usuario: ");
            var usuario = scanner.nextLine().toLowerCase().trim();
            System.out.print("Contrasena: ");
            var contrasena = scanner.nextLine().trim();

            if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contrasena)) {
                System.out.println("\n--- BIENVENIDO AL SISTEMA DE ADMINISTRACION ---");
                return true;
            }

            intentos--;
            System.out.println("\nCredenciales incorrectas.");
            if (intentos > 0) {
                System.out.println("Intentos restantes: " + intentos);
            } else {
                System.out.println("\nHa agotado sus intentos. Saliendo del sistema...");
            }
        }
        return false;
    }

    public static void menuAdmin() {
        while (true) {
            System.out.print("""
                     --- MENU DE ADMINISTRADOR ---
                     1. Ver inventario.
                     2. Ventas.
                     3. Administrar informacion de inicio de sesion.
                     4. SALIR
                     Seleccione una opcion:\s""");
            
            var opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> menuInventario();
                case "2" -> menuVentas();
                case "3" -> menuUsuarios();
                case "4" -> {
                    System.out.println("Saliendo del programa...");
                    return;
                }
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuInventario() {
        mostrarInventario();
        System.out.print("""
                 --- MENU DE INVENTARIO ---
                 1. Registrar nueva mercancia.
                 2. Actualizar cantidades.
                 3. Actualizar precios.
                 4. Eliminar productos.
                 5. Volver a menu de admin.
                 Seleccione una opcion:\s""");
        
        var opcion = scanner.nextLine().trim();

        switch (opcion) {
            case "1" -> agregarNuevaMercancia();
            case "2" -> actualizarCantidades();
            case "3" -> actualizarPrecios();
            case "4" -> eliminarProductos();
            case "5" -> System.out.println("Saliendo del menu de inventario...");
            default -> System.out.println("Opcion no valida.");
        }
    }

    private static void menuVentas() {
        System.out.println("\n --- VENTAS ---");
        mostrarVentas();
    }

    private static void menuUsuarios() {
        mostrarUsuarios();
        System.out.print("""
                
                 --- Menu de administracion de inicio de sesion ---
                 1. Agregar usuario.
                 2. Cambiar clave.
                 3. Eliminar usuario.
                 Seleccione una opcion:\s""");
        
        var opcion = scanner.nextLine().trim();

        switch (opcion) {
            case "1" -> agregarUsuario();
            case "2" -> cambiarClave();
            case "3" -> eliminarUsuario();
            default -> System.out.println("Opcion no valida.");
        }
    }

    private static void mostrarInventario() {
        System.out.println("\n----- INVENTARIO DE PRODUCTOS DE LA TIENDA ROMAY ----\n");
        var productos = jsonManager.cargarProductos();
        productos.forEach(p -> 
            System.out.printf("Id: %d | Nombre: %s | Precio: %.0f | Cantidad: %d%n",
                    p.id(), p.nombre(), p.precio(), p.cantidad())
        );
    }

    private static void agregarNuevaMercancia() {
        System.out.println("\n ----- AGREGAR NUEVA MERCANCIA -----\n");
        System.out.print("Ingrese el nombre del producto: ");
        var nombre = scanner.nextLine().trim();
        System.out.print("Ingrese el precio del producto: ");
        var precio = Double.parseDouble(scanner.nextLine());
        System.out.print("Ingrese la cantidad del producto: ");
        var cantidad = Integer.parseInt(scanner.nextLine());

        var productos = new ArrayList<>(jsonManager.cargarProductos());
        var nuevoId = productos.stream()
                .mapToInt(Producto::id)
                .max()
                .orElse(0) + 1;
        
        productos.add(ProductoFactory.crearProductoPersonalizado(nuevoId, nombre, precio, cantidad));
        jsonManager.guardarProductos(productos);
        
        System.out.println("Producto " + nombre + " agregado exitosamente al inventario.\n");
    }

    private static void actualizarCantidades() {
        System.out.print("""
                
                 ----- ACTUALIZAR CANTIDADES DE LOS PRODUCTOS -----
                
                1. Actualizar cantidades de todos los productos
                2. Actualizar cantidad de un producto especifico
                Seleccione una opcion:\s""");
        
        var opcion = scanner.nextLine().trim();
        var productos = new ArrayList<>(jsonManager.cargarProductos());

        if (opcion.equals("1")) {
            var productosActualizados = productos.stream()
                    .map(p -> {
                        System.out.printf("Ingrese la nueva cantidad para %s (actual: %d): ", 
                                p.nombre(), p.cantidad());
                        var nuevaCantidad = Integer.parseInt(scanner.nextLine());
                        return p.conCantidad(nuevaCantidad);
                    })
                    .collect(Collectors.toList());
            
            jsonManager.guardarProductos(productosActualizados);
            System.out.println("Cantidades actualizadas exitosamente.");
            
        } else if (opcion.equals("2")) {
            System.out.print("Ingrese el nombre del producto: ");
            var nombre = scanner.nextLine().trim();
            var encontrado = false;
            
            for (int i = 0; i < productos.size(); i++) {
                var p = productos.get(i);
                if (p.nombre().equalsIgnoreCase(nombre)) {
                    System.out.printf("Ingrese la nueva cantidad para %s (actual: %d): ", 
                            p.nombre(), p.cantidad());
                    var nuevaCantidad = Integer.parseInt(scanner.nextLine());
                    productos.set(i, p.conCantidad(nuevaCantidad));
                    encontrado = true;
                    break;
                }
            }
            
            if (encontrado) {
                jsonManager.guardarProductos(productos);
                System.out.println("Cantidad actualizada exitosamente.");
            } else {
                System.out.println("Producto no encontrado en el inventario.");
            }
        } else {
            System.out.println("Opcion no valida. Por favor, intente de nuevo.");
        }
    }

    private static void actualizarPrecios() {
        System.out.print("""
                
                  ----- ACTUALIZAR PRECIO DE LOS PRODUCTOS -----
                
                1. Actualizar los precios de todos los productos
                2. Actualizar el precio de un producto especifico
                Seleccione una opcion:\s""");
        
        var opcion = scanner.nextLine().trim();
        var productos = new ArrayList<>(jsonManager.cargarProductos());

        if (opcion.equals("1")) {
            var productosActualizados = productos.stream()
                    .map(p -> {
                        System.out.printf("Ingrese el nuevo precio para %s (actual: %.0f): ", 
                                p.nombre(), p.precio());
                        var nuevoPrecio = Double.parseDouble(scanner.nextLine());
                        return p.conPrecio(nuevoPrecio);
                    })
                    .collect(Collectors.toList());
            
            jsonManager.guardarProductos(productosActualizados);
            System.out.println("Precios actualizados exitosamente.");
            
        } else if (opcion.equals("2")) {
            System.out.print("Ingrese el nombre del producto: ");
            var nombre = scanner.nextLine().trim();
            var encontrado = false;
            
            for (int i = 0; i < productos.size(); i++) {
                var p = productos.get(i);
                if (p.nombre().equalsIgnoreCase(nombre)) {
                    System.out.printf("Ingrese el nuevo precio para %s (actual: %.0f): ", 
                            p.nombre(), p.precio());
                    var nuevoPrecio = Double.parseDouble(scanner.nextLine());
                    productos.set(i, p.conPrecio(nuevoPrecio));
                    encontrado = true;
                    break;
                }
            }
            
            if (encontrado) {
                jsonManager.guardarProductos(productos);
                System.out.println("Precio actualizado exitosamente.");
            } else {
                System.out.println("Producto no encontrado en el inventario.");
            }
        } else {
            System.out.println("Opcion no valida. Por favor, intente de nuevo.");
        }
    }

    private static void eliminarProductos() {
        System.out.print("Ingrese el nombre del producto que desea eliminar: ");
        var nombre = scanner.nextLine().trim();
        var productos = new ArrayList<>(jsonManager.cargarProductos());

        var productosFiltrados = productos.stream()
                .filter(p -> !p.nombre().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());

        if (productosFiltrados.size() < productos.size()) {
            jsonManager.guardarProductos(productosFiltrados);
            System.out.println("Producto eliminado exitosamente.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void registrarVenta() {
        System.out.println("\n ----- REGISTRO DE VENTAS AHORA -----\n");
        var productos = new ArrayList<>(jsonManager.cargarProductos());
        
        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Producto", "Precio", "Stock");
        System.out.println("---------------------------------------------");
        productos.forEach(p -> 
            System.out.printf("%-5d %-20s $%-9.0f %-10d%n", 
                    p.id(), p.nombre(), p.precio(), p.cantidad())
        );

        try {
            System.out.print("\nIngrese el nombre del producto vendido: ");
            var nombreProducto = scanner.nextLine().trim().toLowerCase();
            System.out.print("Ingrese la cantidad vendida: ");
            var cantidad = Integer.parseInt(scanner.nextLine());

            var productoOpt = productos.stream()
                    .filter(p -> p.nombre().toLowerCase().equals(nombreProducto))
                    .findFirst();

            if (productoOpt.isEmpty()) {
                System.out.println("\nError: Producto no encontrado");
                return;
            }

            var producto = productoOpt.get();

            if (producto.cantidad() < cantidad) {
                System.out.println("\nError: No hay suficiente stock");
                System.out.println("Stock disponible: " + producto.cantidad());
                return;
            }

            if (cantidad <= 0) {
                System.out.println("\nError: La cantidad debe ser mayor a cero");
                return;
            }

            var index = productos.indexOf(producto);
            productos.set(index, producto.conCantidad(producto.cantidad() - cantidad));
            jsonManager.guardarProductos(productos);

            System.out.printf("""
                    
                    Venta registrada: %d unidades de %s
                    Stock restante: %d
                    """, cantidad, producto.nombre(), producto.cantidad() - cantidad);
            
        } catch (NumberFormatException e) {
            System.out.println("\nError: Ingrese una cantidad valida");
        }
    }

    private static void mostrarVentas() {
        System.out.println("\n --- REGISTRO DE VENTAS ---\n");
        var tickets = jsonManager.cargarTickets();
        
        tickets.forEach(ticket -> {
            System.out.println("Fecha: " + ticket.fecha() + " | Total: " + ticket.total());
            System.out.println("Productos vendidos:");
            ticket.items().forEach(item -> 
                System.out.printf(" - %s | Precio: %.0f | Cantidad: %d%n", 
                        item.nombre(), item.precio(), item.cantidad())
            );
            System.out.println("--------------------------------------------------");
        });
        
        registrarVenta();
    }

    private static void mostrarUsuarios() {
        System.out.println("\n Lista de usuarios\n");
        var usuarios = jsonManager.cargarUsuarios();
        usuarios.forEach((user, pass) -> 
            System.out.println(" - Usuario: " + user + ", Contrasena: " + pass)
        );
    }

    private static void agregarUsuario() {
        System.out.print("\nIngrese el nombre del nuevo usuario: ");
        var usuario = scanner.nextLine().trim();
        var usuarios = jsonManager.cargarUsuarios();

        if (usuarios.containsKey(usuario)) {
            System.out.println("El usuario ya existe.");
        } else {
            System.out.print("Ingrese la contrasena: ");
            var contrasena = scanner.nextLine().trim();
            usuarios.put(usuario, contrasena);
            jsonManager.guardarUsuarios(usuarios);
            System.out.println("Usuario agregado exitosamente.");
        }
    }

    private static void cambiarClave() {
        System.out.print("\nIngrese su usuario: ");
        var usuario = scanner.nextLine().trim();
        var usuarios = jsonManager.cargarUsuarios();

        if (usuarios.containsKey(usuario)) {
            System.out.print("Ingrese su nueva contrasena: ");
            var nuevaClave = scanner.nextLine().trim();
            usuarios.put(usuario, nuevaClave);
            jsonManager.guardarUsuarios(usuarios);
            System.out.println("\nContrasena actualizada.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    private static void eliminarUsuario() {
        System.out.print("Ingrese el nombre del usuario a eliminar: ");
        var usuario = scanner.nextLine().trim();
        var usuarios = jsonManager.cargarUsuarios();

        if (usuarios.containsKey(usuario)) {
            usuarios.remove(usuario);
            jsonManager.guardarUsuarios(usuarios);
            System.out.println("Usuario eliminado.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }
}