/**
 * Función heurística
 * Created by falc on 7/10/15.
 */

import IA.Bicing.Estacion;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;

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
            int eur;
            if (e.getDemanda() >= e.getNumBicicletasNext())
            {
                if (e.getDemanda() >= e.getNumBicicletasNext() + bicis_llevadas)
                    eur = bicis_llevadas;
                else eur = 0; // o puede ser e.getDemanda()-e.getNumBicicletasNext;
            }
            else
            {
                if (e.getNumBicicletasNext()+bicis_llevadas >= e.getDemanda()) eur = 0;
                else eur = (e.getNumBicicletasNext()+bicis_llevadas) - e.getDemanda();
            }
            sum_acord += eur;
        }
        System.out.print(sum_acord+"\n");
        return -sum_acord;
    }
}
