import IA.Bicing.Estaciones;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            Estaciones estaciones = new Estaciones(25, 1250, 0, 1234);
            Estado estado = new Estado(2,5, estaciones, 1234);
            estado.writeEstado();
            /*System.out.println("Hello World!");
            Problem problem = new Problem(estado, new FuncionSucesoraHillClimbing(), new GoalTest(), new FuncionHeuristicaC1());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);
            System.out.print("Acciones\n");
            printActions(agent.getActions());
            System.out.print("Instrumentación\n");
            printInstrumentation(agent.getInstrumentation());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        ControladorExperimentos c = new ControladorExperimentos();
        c.lanzadorExperimentos();

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
