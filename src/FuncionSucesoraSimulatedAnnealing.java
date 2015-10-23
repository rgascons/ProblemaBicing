import IA.Bicing.Estacion;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FuncionSucesoraSimulatedAnnealing implements SuccessorFunction {
    @Override
    public List getSuccessors(Object o) {
        ArrayList<Successor> retVal = new ArrayList<>();
        Estado state = (Estado) o;
        Random rand = new Random();
        boolean valid = false;
        while (!valid)
        {
            int n = rand.nextInt(state.getFurgonetas().size());
            Furgoneta f = state.getFurgonetas().get(n);
            int m = rand.nextInt(state.getEstaciones().size());
            Estacion e = state.getEstaciones().get(m);
            int opt = rand.nextInt(7);
            switch (opt) {
                case 0:
                    if (state.puedeCambiarEstacionOrigen(e, f)) {
                        Estado nuevoEstado = new Estado(state);
                        nuevoEstado.cambiarEstacionOrigen(e, f);
                        Estacion nove = nuevoEstado.getEstaciones().get(m);
                        retVal.add(new Successor(Estado.CAMBIAR_ESTACION_ORIGEN + " #" + n + "# -> " + Estado.getM().get(nove), nuevoEstado));
                        valid = true;
                    }
                    break;
                case 1:
                    if (state.puedeSustituirEstacion(f.getPrimerDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(state);
                        nuevoEstado.sustituirEstacion(f.getPrimerDestino(), e, f);
                        Estacion nove = nuevoEstado.getEstaciones().get(m);
                        retVal.add(new Successor(Estado.SUSTITUIR_ESTACION + " #" + n + "# " + Estado.getM().get(f.getPrimerDestino()) + " -> " + Estado.getM().get(nove), nuevoEstado));
                        valid = true;
                    }
                    break;
                case 2:
                    if (state.puedeSustituirEstacion(f.getSegundoDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(state);
                        nuevoEstado.sustituirEstacion(f.getSegundoDestino(), e, f);
                        Estacion nove = nuevoEstado.getEstaciones().get(m);
                        retVal.add(new Successor(Estado.SUSTITUIR_ESTACION + " #" + n + "# " + Estado.getM().get(f.getPrimerDestino()) + " -> " + Estado.getM().get(nove), nuevoEstado));
                        valid = true;
                    }
                    break;
                case 3 :
                    if (state.puedeQuitarEstacion(e, f))
                    {
                        Estado nuevoEstado = new Estado(state);
                        nuevoEstado.quitarEstacion(e, f);
                        Estacion nove = nuevoEstado.getEstaciones().get(m);
                        retVal.add(new Successor(Estado.QUITAR_ESTACION+" #"+n+"# "+Estado.getM().get(nove), nuevoEstado));
                        valid = true;
                    }
                    break;
                default:
                    break;
            }
        }
        //TODO Revisar número de operadores
        // TODO
        return retVal;
    }
}
