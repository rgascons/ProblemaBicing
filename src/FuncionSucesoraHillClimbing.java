import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class FuncionSucesoraHillClimbing implements SuccessorFunction {

    @Override
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        Estado estado = (Estado) aState;
        Furgonetas furgonetas = estado.getFurgonetas();
        Estaciones estaciones = estado.getEstaciones();
        int i = 0;
        for (Furgoneta f : furgonetas) {
            for (Estacion e : estaciones) {
                if (!e.equals(f.getOrigen())) {
                    if (estado.puedeCambiarEstacionOrigen(e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.cambiarEstacionOrigen(e, f);
                        retVal.add(new Successor(Estado.CAMBIAR_ESTACION_ORIGEN+" #"+i+"# -> "+Estado.getM().get(e), nuevoEstado));
                    }
                    if (estado.puedeSustituirEstacion(f.getPrimerDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.sustituirEstacion(f.getPrimerDestino(), e, f);
                        retVal.add(new Successor(Estado.SUSTITUIR_ESTACION+" #"+i+"# "+Estado.getM().get(f.getPrimerDestino())+" -> "+Estado.getM().get(e), nuevoEstado));
                    }
                    if (estado.puedeSustituirEstacion(f.getSegundoDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.sustituirEstacion(f.getSegundoDestino(), e, f);
                        retVal.add(new Successor(Estado.SUSTITUIR_ESTACION+" #"+i+"# "+Estado.getM().get(f.getSegundoDestino())+" -> "+Estado.getM().get(e), nuevoEstado));
                    }
                    if (estado.puedeQuitarEstacion(e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.quitarEstacion(e, f);
                        retVal.add(new Successor(Estado.QUITAR_ESTACION+" #"+i+"# "+Estado.getM().get(e), nuevoEstado));
                    }
                }
            }
            if (estado.puedeDejarBicis(f, f.getBicisPrimeraEstacion())) {
                for (int j = 1; j <= 29; ++j) {
                    Estado nuevoEstado = new Estado(estado);
                    nuevoEstado.dejarBicis(f, f.getBicisPrimeraEstacion());
                    retVal.add(new Successor(Estado.DEJAR_BICIS + " #" + j + "#", nuevoEstado));
                }
            }
            if (estado.puedeRecogerBicis(f, f.getBicisEstacionOrigen())) {
                for (int j = 1; j <= 29; ++j) {
                    Estado nuevoEstado = new Estado(estado);
                    nuevoEstado.recogerBicis(f, f.getBicisEstacionOrigen());
                    retVal.add(new Successor(Estado.RECOGER_BICIS, nuevoEstado));
                }
            }
            ++i;
        }
        return retVal;
    }
}
