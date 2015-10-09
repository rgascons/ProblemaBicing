package Experimentos;

import java.util.Scanner;

public class ControladorExperimentos {
    public void lanzadorExperimentos() {
        Scanner in = new Scanner(System.in);
        int i = 0;
        for (NombreExperimentos n : NombreExperimentos.values()) {
            System.out.println("Experimento " + i + ": " + n);
        }
        System.out.println("Escoje el experimento");
        //TODO: acceptar input de l'usuari
    }
}
