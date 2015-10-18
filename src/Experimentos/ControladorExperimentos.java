package Experimentos;

import java.util.Scanner;

public class ControladorExperimentos {
    public void lanzadorExperimentos() {
        int i = 1;
        for (NombreExperimentos n : NombreExperimentos.values()) {
            System.out.println("Experimento " + i++ + ": " + n);
        }
        System.out.println("Escoje el experimento");
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        switch (n) {
            case 1:
                //Lanzar Experimento1
                break;
            case 2:
                //Lanzar Experimento2
                break;
            default:
                System.out.println("Esto no es un experimento. Cerrando el programa...");
                break;
        }
    }
}
