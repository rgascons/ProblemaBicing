package Dominio; /**
 * Función heurística C1
 * Created by falc on 7/10/15.
 */

import IA.Bicing.Estacion;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuncionHeuristicaC1 implements HeuristicFunction{
    public double getHeuristicValue(Object n)
    {
        Estado state = (Estado) n;
        double sum_acord = 0;
        double bicistotal = 0;
        double distancia = 0;
        Furgonetas fur = state.getFurgonetas();
        HashMap<Estacion, Boolean> mapp = new HashMap<>();
        for (int i = 0; i < fur.size(); ++i)
        {
            Furgoneta f = fur.get(i);
            if (!f.estaVacia()) {
                int trayecto1 = Math.abs(f.getOrigen().getCoordX() - f.getPrimerDestino().getCoordX()) + Math.abs(f.getOrigen().getCoordY() - f.getPrimerDestino().getCoordY());
                int trayecto2 = (f.getSegundoDestino() != null) ? Math.abs(f.getPrimerDestino().getCoordX() - f.getSegundoDestino().getCoordX()) + Math.abs(f.getPrimerDestino().getCoordY() - f.getSegundoDestino().getCoordY()) : 0;
                distancia += trayecto1 + trayecto2;

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

        /*
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
            bicistotal += bicis_llevadas+e.getNumBicicletasNext();
        }*/
        System.out.print(sum_acord+", "+distancia+"\n");
        return -sum_acord;
    }
}
