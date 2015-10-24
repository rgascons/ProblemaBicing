import IA.Bicing.Estacion;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FuncionSucesoraSimulatedAnnealing implements SuccessorFunction {

    private static int DENOMINADOR_OPERADORES = 283;
    private static int MAX_ITERACIONES = DENOMINADOR_OPERADORES * 4;

    @Override
    public List getSuccessors(Object o) {
        ArrayList<Successor> retVal = new ArrayList<>();
        Estado state = (Estado) o;
        Random rand = new Random();
        boolean valid = false;
        int iteraciones = 0;
        while (!valid) {
            int n = rand.nextInt(state.getFurgonetas().size());
            Furgoneta f = state.getFurgonetas().get(n);
            int m = rand.nextInt(state.getEstaciones().size());
            Estacion e = state.getEstaciones().get(m);
            int opt = rand.nextInt(DENOMINADOR_OPERADORES);
            if (opt < 87) {
                if (state.puedeCambiarEstacionOrigen(e, f)) {
                    Estado nuevoEstado = new Estado(state);
                    nuevoEstado.cambiarEstacionOrigen(e, f);
                    Estacion nove = nuevoEstado.getEstaciones().get(m);
                    retVal.add(new Successor(Estado.CAMBIAR_ESTACION_ORIGEN + " #" + n + "# -> " + Estado.getM().get(nove), nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 87 && opt < 174) {
                if (state.puedeSustituirEstacion(f.getPrimerDestino(), e, f)) {
                    Estado nuevoEstado = new Estado(state);
                    nuevoEstado.sustituirEstacion(f.getPrimerDestino(), e, f);
                    Estacion nove = nuevoEstado.getEstaciones().get(m);
                    retVal.add(new Successor(Estado.SUSTITUIR_ESTACION + " #" + n + "# " + Estado.getM().get(f.getPrimerDestino()) + " -> " + Estado.getM().get(nove), nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 174 && opt < 261) {
                if (state.puedeSustituirEstacion(f.getSegundoDestino(), e, f)) {
                    Estado nuevoEstado = new Estado(state);
                    nuevoEstado.sustituirEstacion(f.getSegundoDestino(), e, f);
                    Estacion nove = nuevoEstado.getEstaciones().get(m);
                    retVal.add(new Successor(Estado.SUSTITUIR_ESTACION + " #" + n + "# " + Estado.getM().get(f.getPrimerDestino()) + " -> " + Estado.getM().get(nove), nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 261 && opt < 264) {
                if (state.puedeQuitarEstacion(f.getPrimerDestino(), f)) {
                    Estado nuevoEstado = new Estado(state);
                    Furgoneta neo = nuevoEstado.getFurgonetas().get(n);
                    nuevoEstado.quitarEstacion(neo.getPrimerDestino(), f);
                    Estacion nove = nuevoEstado.getEstaciones().get(m);
                    retVal.add(new Successor(Estado.QUITAR_ESTACION + " #" + n + "# " + Estado.getM().get(nove), nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 264 && opt < 267) {
                if (state.puedeQuitarEstacion(f.getSegundoDestino(), f)) {
                    Estado nuevoEstado = new Estado(state);
                    Furgoneta neo = nuevoEstado.getFurgonetas().get(n);
                    nuevoEstado.quitarEstacion(neo.getSegundoDestino(), f);
                    Estacion nove = nuevoEstado.getEstaciones().get(m);
                    retVal.add(new Successor(Estado.QUITAR_ESTACION + " #" + n + "# " + Estado.getM().get(nove), nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 267 && opt < 277) {
                int fgr = f.getBicisEstacionOrigen();
                int j = (fgr == 0)? 0: rand.nextInt(f.getBicisEstacionOrigen());
                if (state.puedeDejarBicis(f, j)) {
                    Estado nuevoEstado = new Estado(state);
                    Furgoneta neo = nuevoEstado.getFurgonetas().get(n);
                    nuevoEstado.dejarBicis(neo, j);
                    FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                    fhc1.getHeuristicValue(nuevoEstado);
                    retVal.add(new Successor(Estado.DEJAR_BICIS + " #"+n+"# [[" + j + "]]", nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 277 && opt < 280) {
                if (state.puedeEliminarFurgoneta(n)) {
                    Estado nuevoEstado = new Estado(state);
                    nuevoEstado.eliminarFurgoneta(n);
                    FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                    fhc1.getHeuristicValue(nuevoEstado);
                    retVal.add(new Successor(Estado.ELIMINAR_FURGONETA+" ~"+n+"~ ", nuevoEstado));
                    valid = true;
                }
            }
            else if (opt >= 280) {
                if (state.puedeAnadirFurgoneta(n)) {
                    Estado nuevoEstado = new Estado(state);
                    nuevoEstado.anadirFurgoneta(n);
                    FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                    fhc1.getHeuristicValue(nuevoEstado);
                    retVal.add(new Successor(Estado.ANADIR_FURGONETA+" ~"+n+"~ ", nuevoEstado));
                    valid = true;
                }
            }
            if (iteraciones == MAX_ITERACIONES) break;
            ++iteraciones;
        }
        return retVal;
    }
}
