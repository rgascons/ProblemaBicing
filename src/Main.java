import IA.Bicing.Estaciones;
import aima.search.framework.*;
import aima.search.informed.AStarSearch;

public class Main {

    public static void main(String[] args) {

        Estaciones estaciones = new Estaciones(20, 60, 0, 1234);
        System.out.println("Hello World!");
        Problem problem = new Problem(estaciones, new FuncionSucesora(), new GoalTest(), new FuncionHeuristica());
        Search search = new AStarSearch(new GraphSearch());
        try {
            SearchAgent agent = new SearchAgent(problem, search);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
