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
        System.out.println("Escoje el experimento (1..8)");
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        switch (n) {
            case 1:
                try {
                    System.out.println("Elige el conjunto de operadores (1 o 2):");
                    int co = s.nextInt();
                    System.out.println("Experimento: Conjunto de Operadores");
                    Estaciones estaciones = new Estaciones(25, 1250, 0, (int)System.nanoTime());
                    Estado estado = new Estado(1, 5, estaciones, (int)System.nanoTime());
                    Problem problem = (co == 1)? new Problem(estado, new FuncionSucesoraHillClimbing(), new GoalTest(), new FuncionHeuristicaC1()):new Problem(estado, new FuncionSucesoraHillClimbing2(), new GoalTest(), new FuncionHeuristicaC1());
                    Search search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);
                    System.out.print("Acciones\n");
                    printActions(agent.getActions());
                    System.out.print("Instrumentación\n");
                    printInstrumentation(agent.getInstrumentation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    System.out.println("Elige el generador inicial (1 o 2):");
                    int gi = s.nextInt();
                    System.out.println("Experimento: Generador Inicial");
                    Estaciones estaciones = new Estaciones(25, 1250, 0, (int)System.nanoTime());
                    Estado estado = new Estado(gi, 5, estaciones, (int)System.nanoTime());
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
            case 3:
                try {
                    System.out.println("Elige el número de pasos total:");
                    int p = s.nextInt();
                    System.out.println("Elige el número de pasos por grado de temperatura:");
                    int pgt = s.nextInt();
                    System.out.println("Elige la variable k:");
                    int k = s.nextInt();
                    System.out.println("Elige la variable lambda:");
                    while (!s.hasNextFloat())
                    {
                        System.out.print("Bad value");
                        s.nextLine();
                    }
                    float lambda = s.nextFloat();
                    System.out.println("Experimento: Simulated Annealing");
                    Estaciones estaciones = new Estaciones(25, 1250, 0, 1234);
                    Estado estado = new Estado(1,5, estaciones, 1234);
                    Problem problem = new Problem(estado, new FuncionSucesoraSimulatedAnnealing(), new GoalTest(), new FuncionHeuristicaC1());
                    Search search = new SimulatedAnnealingSearch(p,pgt,k,lambda);
                    SearchAgent agent = new SearchAgent(problem, search);
                    //System.out.print("Acciones\n");
                    //printActions(agent.getActions());
                    System.out.print("Instrumentación\n");
                    printInstrumentation(agent.getInstrumentation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Lanzar Experimento2
                break;
            case 4:
                try {
                    System.out.println("Elige el generador inicial (1 o 2):");
                    int gi = s.nextInt();
                    System.out.println("Elige el número de estaciones:");
                    int ne = s.nextInt();
                    System.out.println("Elige el tipo de escenario (0 o 1):");
                    int te = s.nextInt();
                    Estaciones estaciones = new Estaciones(ne, ne*50, te, 1234);
                    Estado estado = new Estado(gi, ne/5, estaciones, 1234);
                    System.out.println("Experimento: Generador Inicial");
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
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
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
