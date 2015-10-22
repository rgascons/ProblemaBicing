import IA.Bicing.Estaciones;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.List;
import java.util.Properties;
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
                try {
                    System.out.println("Elige el generador inicial (1 o 2):");
                    int gi = s.nextInt();
                    System.out.println("Elige el número de estaciones:");
                    int ne = s.nextInt();
                    System.out.println("Elige el tipo de escenario (0 o 1):");
                    int te = s.nextInt();
                    Estaciones estaciones = new Estaciones(ne, ne*50, te, 1234);
                    Estado estado = new Estado(gi, ne/5, estaciones, 1234);
                    System.out.println("Hello World!");
                    Problem problem = new Problem(estado, new FuncionSucesoraHillClimbing(), new GoalTest(), new FuncionHeuristicaC1());
                    Search search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);
                    System.out.print("Acciones\n");
                    printActions(agent.getActions());
                    System.out.print("Instrumentación\n");
                    printInstrumentation(agent.getInstrumentation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Lanzar Experimento1
                break;
            case 2:
                try {
                    Estaciones estaciones = new Estaciones(25, 1250, 0, 1234);
                    Estado estado = new Estado(1, 5, estaciones, 1234);
                    System.out.println("Hello World!");
                    Problem problem = new Problem(estado, new FuncionSucesoraSimulatedAnnealing(), new GoalTest(), new FuncionHeuristica());
                    Search search = new SimulatedAnnealingSearch(2000,100,5,0.001D);
                    SearchAgent agent = new SearchAgent(problem, search);
                    System.out.print("Acciones\n");
                    //printActions(agent.getActions());
                    System.out.print("Instrumentación\n");
                    printInstrumentation(agent.getInstrumentation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Lanzar Experimento2
                break;
            default:
                System.out.println("Esto no es un experimento. Cerrando el programa...");
                break;
        }
    }

    private static void printActions(List actions) {
        for (Object action1 : actions) {
            String action = (String) action1;
            System.out.println(action);
        }

    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }
}
