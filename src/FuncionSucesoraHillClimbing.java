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

        for (Furgoneta f : furgonetas) {
            for (Estacion e : estaciones) {
                if (!e.equals(f.getOrigen())) {
                    if (estado.puedeCambiarEstacionOrigen(e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.cambiarEstacionOrigen(e, f);
                        retVal.add(new Successor(nuevoEstado.op[3], nuevoEstado));
                    }
                    if (estado.puedeSustituirEstacion(f.getPrimerDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.sustituirEstacion(f.getPrimerDestino(), e, f);
                        retVal.add(new Successor(nuevoEstado.op[0], nuevoEstado));
                    }
                    if (estado.puedeSustituirEstacion(f.getSegundoDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        nuevoEstado.sustituirEstacion(f.getSegundoDestino(), e, f);
                        retVal.add(new Successor(nuevoEstado.op[0], nuevoEstado));
                    }
                }
            }
            if (estado.puedeDejarBicis(f, f.getBicisPrimeraEstacion())) {
                Estado nuevoEstado = new Estado(estado);
                nuevoEstado.dejarBicis(f, f.getBicisPrimeraEstacion());
                retVal.add(new Successor(nuevoEstado.op[1], nuevoEstado));
            }
            if (estado.puedeRecogerBicis(f, f.getBicisEstacionOrigen())) {
                Estado nuevoEstado = new Estado(estado);
                nuevoEstado.recogerBicis(f, f.getBicisEstacionOrigen());
                retVal.add(new Successor(nuevoEstado.op[2], nuevoEstado));
            }
        }
        return retVal;
    }
}
