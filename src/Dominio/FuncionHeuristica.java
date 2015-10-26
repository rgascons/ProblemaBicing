package Dominio; /**
 * Función heurística
 * Created by falc on 7/10/15.
 */

import IA.Bicing.Estacion;
import Presentacion.MainInterficie;
import aima.search.framework.HeuristicFunction;
import java.util.ArrayList;
import java.util.HashMap;

public class FuncionHeuristica implements HeuristicFunction{
    public double getHeuristicValue(Object n)
    {
        Estado state = (Estado) n;
        ArrayList<Furgoneta> furg = state.getFurgonetas();
        double suma;
        HashMap<Estacion, Boolean> mapp = new HashMap<>();
        double sum_cost = 0;
        double sum_acord = 0;
        double distancia = 0;
        for (Furgoneta f : furg) {
            if (!f.estaVacia()) {
                int trayecto1 = Math.abs(f.getOrigen().getCoordX() - f.getPrimerDestino().getCoordX()) + Math.abs(f.getOrigen().getCoordY() - f.getPrimerDestino().getCoordY());
                int trayecto2 = (f.getSegundoDestino() != null) ? Math.abs(f.getPrimerDestino().getCoordX() - f.getSegundoDestino().getCoordX()) + Math.abs(f.getPrimerDestino().getCoordY() - f.getSegundoDestino().getCoordY()) : 0;
                int nb1 = f.getBicisEstacionOrigen();
                int nb2 = f.getBicisEstacionOrigen() - f.getBicisPrimeraEstacion();
                int e_k1 = ((nb1 + 9) / 10);
                int e_k2 = ((nb2 + 9) / 10);
                sum_cost += e_k1 * trayecto1 + e_k2 * trayecto2;

                distancia += trayecto1+trayecto2;

                int eur = 0;
                Estacion o = f.getOrigen();
                if (!mapp.containsKey(o))
                {
                    mapp.put(o, true);
                    if (o.getNumBicicletasNext() - state.getBicisE().get(Estado.getM().get(o)) < o.getDemanda())
                        eur = (o.getNumBicicletasNext() - f.getBicisEstacionOrigen()) - o.getDemanda();
                }

                Estacion d1 = f.getPrimerDestino();
                if (!mapp.containsKey(d1)) {
                    mapp.put(d1,true);
                    if (d1.getDemanda() >= d1.getNumBicicletasNext() + f.getBicisPrimeraEstacion())
                        eur += f.getBicisPrimeraEstacion();
                }

                if (f.getSegundoDestino() != null)
                {
                    Estacion d2 = f.getSegundoDestino();
                    if(!mapp.containsKey(d2)) {
                        mapp.put(d2, true);
                        if (d2.getDemanda() >= d2.getNumBicicletasNext() + f.getBicisSegundaEstacion())
                            eur += f.getBicisSegundaEstacion();
                    }
                }
                sum_acord += eur;
            }

        }

        suma = sum_acord - sum_cost;
        MainInterficie.setFinalCost(sum_acord);
        System.out.print(sum_acord+"€, "+sum_cost+"€, "+distancia+" m \n");
        return -suma;
    }
}
