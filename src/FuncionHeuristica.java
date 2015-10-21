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
            int trayecto2 = (f.getSegundoDestino() != null)? Math.abs(f.getPrimerDestino().getCoordX() - f.getSegundoDestino().getCoordX()) + Math.abs(f.getPrimerDestino().getCoordY() - f.getSegundoDestino().getCoordY()) : 0;
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
            int eur;
            if (e.getDemanda() >= e.getNumBicicletasNext())
            {
                if (e.getDemanda() >= e.getNumBicicletasNext() + bicis_llevadas)
                    eur = bicis_llevadas;
                else eur = 0;//e.getDemanda()-e.getNumBicicletasNext();
            }
            else
            {
                if (e.getNumBicicletasNext()+bicis_llevadas >= e.getDemanda()) eur = 0;
                else eur = (e.getNumBicicletasNext()+bicis_llevadas) - e.getDemanda();
            }
            sum_acord += eur;

        }

        suma = sum_cost + sum_acord;
        System.out.print(suma+"\n");
        return -suma;
    }
}
