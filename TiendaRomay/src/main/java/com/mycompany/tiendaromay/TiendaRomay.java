/**
 *
 * @author ashly y santiago
 */
package com.mycompany.tiendaromay;

import java.util.Scanner;

public class TiendaRomay {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("""
                        ----- Sistema de Simulacion de Inventarios -----
                        
                         BIENVENIDO A LA TIENDA ROMAY
                        
                        Quien eres?
                        
                         1. Cliente
                         2. Administrador
                         3. Salir
                         Elige una opcion:\s""");
                
                var opcion = scanner.nextLine().trim();
                
                switch (opcion) {
                    case "1" -> Cliente.tienda();
                    case "2" -> {
                        if (Administrador.autenticarUsuario()) {
                            Administrador.menuAdmin();
                        }
                    }
                    case "3" -> {
                        System.out.println("Saliendo del programa...");
                        return;
                    }
                    default -> {
                        System.out.println("Opcion invalida. Bye!");
                        return;
                    }
                }
            }
        }
    }
}