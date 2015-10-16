/**
 * Función heurística
 * Created by falc on 7/10/15.
 */

import java.util.ArrayList;

import IA.Bicing.Estacion;
import aima.search.framework.HeuristicFunction;

public class FuncionHeuristicaC1 implements HeuristicFunction{
    public double getHeuristicValue(Object n)
    {
        Estado state = (Estado) n;

        double sum_acord = 0;
        ArrayList<Estacion> est = state.getEstaciones();
        for (int i = 0; i < est.size(); ++i)
        {
            Estacion e = est.get(i);
            int bicis_llevadas = state.getBicisE().get(i);
            int eur = (e.getDemanda() >= e.getNumBicicletasNext()+bicis_llevadas)? bicis_llevadas: -bicis_llevadas;
            sum_acord += eur;
        }

        return -sum_acord;
    }
}
