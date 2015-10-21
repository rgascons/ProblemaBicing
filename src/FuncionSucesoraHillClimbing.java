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
        System.out.print("-----\n");
        for (Furgoneta furgoneta : furgonetas)
            System.out.print(Estado.getM().get(furgoneta.getOrigen()) + "->" + Estado.getM().get(furgoneta.getPrimerDestino()) + "->" + Estado.getM().get(furgoneta.getSegundoDestino()) + "\n");
        System.out.print("-----\n");
        for (int i = 0 ; i < furgonetas.size();++i) {
            Furgoneta f = furgonetas.get(i);
            for (int j = 0; j < estaciones.size(); ++j) {
                Estacion e = estaciones.get(j);
                if (!e.equals(f.getOrigen())) {
                    if (estado.puedeCambiarEstacionOrigen(e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        Furgoneta neo = nuevoEstado.getFurgonetas().get(i);
                        Estacion nove = nuevoEstado.getEstaciones().get(j);
                        nuevoEstado.cambiarEstacionOrigen(nove, neo);
                        /*FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                        fhc1.getHeuristicValue(nuevoEstado);*/
                        retVal.add(new Successor(Estado.CAMBIAR_ESTACION_ORIGEN+" #"+i+"# -> "+ Estado.getM().get(nove), nuevoEstado));
                    }
                    if (estado.puedeSustituirEstacion(f.getPrimerDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        Furgoneta neo = nuevoEstado.getFurgonetas().get(i);
                        Estacion nove = nuevoEstado.getEstaciones().get(j);
                        nuevoEstado.sustituirEstacion(neo.getPrimerDestino(), nove, neo);
                        /*FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                        fhc1.getHeuristicValue(nuevoEstado);*/
                        retVal.add(new Successor(Estado.SUSTITUIR_ESTACION+" #"+i+"# "+Estado.getM().get(f.getPrimerDestino())+" -> "+Estado.getM().get(nove), nuevoEstado));
                    }
                    if (estado.puedeSustituirEstacion(f.getSegundoDestino(), e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        Furgoneta neo = nuevoEstado.getFurgonetas().get(i);
                        Estacion nove = nuevoEstado.getEstaciones().get(j);
                        nuevoEstado.sustituirEstacion(neo.getSegundoDestino(), nove, neo);
                        /*FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                        fhc1.getHeuristicValue(nuevoEstado);*/
                        retVal.add(new Successor(Estado.SUSTITUIR_ESTACION+" #"+i+"# "+Estado.getM().get(f.getSegundoDestino())+" -> "+Estado.getM().get(nove), nuevoEstado));
                    }
                    if (estado.puedeQuitarEstacion(e, f)) {
                        Estado nuevoEstado = new Estado(estado);
                        Furgoneta neo = nuevoEstado.getFurgonetas().get(i);
                        Estacion nove = nuevoEstado.getEstaciones().get(j);
                        nuevoEstado.quitarEstacion(nove, neo);
                        FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                        /*fhc1.getHeuristicValue(nuevoEstado);*/
                        retVal.add(new Successor(Estado.QUITAR_ESTACION+" #"+i+"# "+Estado.getM().get(nove), nuevoEstado));
                    }
                }
            }

            for (int j = 0; j <= 30; ++j) {
                if (estado.puedeDejarBicis(f, j)) {
                    Estado nuevoEstado = new Estado(estado);
                    Furgoneta neo = nuevoEstado.getFurgonetas().get(i);
                    nuevoEstado.dejarBicis(neo, j);
                    /*FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                    fhc1.getHeuristicValue(nuevoEstado);*/
                    retVal.add(new Successor(Estado.DEJAR_BICIS + " #"+i+"# [[" + j + "]]", nuevoEstado));
                }

            }

            for (int j = 0; j <= 30; ++j) {
                if (estado.puedeRecogerBicis(f, j)) {
                    Estado nuevoEstado = new Estado(estado);
                    Furgoneta neo = nuevoEstado.getFurgonetas().get(i);
                    nuevoEstado.recogerBicis(neo, j);
                    /*FuncionHeuristicaC1 fhc1 = new FuncionHeuristicaC1();
                    fhc1.getHeuristicValue(nuevoEstado);*/
                    retVal.add(new Successor(Estado.RECOGER_BICIS+" #"+i+"# {{" + j + "}}", nuevoEstado));
                }
            }
        }
        /*System.out.println("Genero " + retVal.size() + " estados sucesores");
        for (int j = 0; j < furgonetas.size(); ++j) {
            System.out.println("Soy furgoneta " + j);
            furgonetas.get(j).writeFrurgoneta();
        }*/
        return retVal;
    }
}
