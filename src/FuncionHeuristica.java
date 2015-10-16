/**
 * Función heurística
 * Created by falc on 7/10/15.
 */

import IA.Bicing.Estacion;
import aima.search.framework.HeuristicFunction;
import java.util.ArrayList;

public class FuncionHeuristica implements HeuristicFunction{
    public double getHeuristicValue(Object n)
    {
        Estado state = (Estado) n;
        ArrayList<Furgoneta> furg = state.getFurgonetas();
        double suma;

        double sum_cost = 0;
        for (Furgoneta f : furg) {
            int trayecto1 = Math.abs(f.getOrigen().getCoordX() - f.getPrimerDestino().getCoordX()) + Math.abs(f.getOrigen().getCoordY() - f.getPrimerDestino().getCoordY());
            int trayecto2 = Math.abs(f.getPrimerDestino().getCoordX() - f.getSegundoDestino().getCoordX()) + Math.abs(f.getPrimerDestino().getCoordY() - f.getSegundoDestino().getCoordY());
            int nb1 = f.getBicisEstacionOrigen();
            int nb2 = f.getBicisEstacionOrigen() - f.getBicisPrimeraEstacion();
            int e_k1 = ((nb1 + 9) / 10);
            int e_k2 = ((nb2 + 9) / 10);
            sum_cost += e_k1 * trayecto1 + e_k2 * trayecto2;
        }

        double sum_acord = 0;


        ArrayList<Estacion> est = state.getEstaciones();
        for (int i = 0; i < est.size(); ++i)
        {
            Estacion e = est.get(i);
            int bicis_llevadas = state.getBicisE().get(i);
            int eur = (e.getDemanda() >= e.getNumBicicletasNext()+bicis_llevadas)? bicis_llevadas: -bicis_llevadas;
            sum_acord += eur;
        }


        suma = sum_cost + sum_acord;
        return -suma;
    }
}
