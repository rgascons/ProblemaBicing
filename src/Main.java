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
            Estaciones estaciones = new Estaciones(20, 60, 0, (int)System.currentTimeMillis());
            Estado estado = new Estado(15, estaciones);
            System.out.println("Hello World!");
            Problem problem = new Problem(estado, new FuncionSucesoraHillClimbing(), new GoalTest(), new FuncionHeuristica());
            Search search = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(problem, search);
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ControladorExperimentos c = new ControladorExperimentos();
        //c.lanzadorExperimentos();

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
