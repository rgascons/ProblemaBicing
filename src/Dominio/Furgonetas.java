package Dominio;

import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.Random;

public class Furgonetas extends ArrayList<Furgoneta> implements Cloneable{

    private int nfurg;
    private int nest;
    //private long seed;
    private Estaciones estaciones;

    public Furgonetas(int nfurg, int nest, long seed, Estaciones estaciones) {

        this.nfurg = nfurg;
        this.nest = nest;
        //this.seed = seed;
        this.estaciones = estaciones;
        boolean orig[] = new boolean[estaciones.size()];
        Random myRandom = new Random(seed);

        Furgoneta f;
        for (int i = 0; i < nfurg; ++i) {
            int idEstOrigen;
            boolean excedente;
            do {
                idEstOrigen = myRandom.nextInt(nest);
                Estacion eo = estaciones.get(idEstOrigen);
                excedente = eo.getNumBicicletasNext() > eo.getDemanda();
            }
            while (orig[idEstOrigen] || !excedente); //Si ya es un origen o no tiene excedente de bicis...
            orig[idEstOrigen] = true;
            int idPrimDestino;
            boolean deficit;
            int cn1 = 0;
            do {
                idPrimDestino = myRandom.nextInt(nest);
                Estacion ed1 = estaciones.get(idPrimDestino);
                deficit = ed1.getNumBicicletasNext() <= ed1.getDemanda();
                if (cn1 >= estaciones.size()) deficit = true;
                ++cn1;
            }
            while (idEstOrigen == idPrimDestino && !deficit);
            int idSegDestino;
            boolean def2;
            int cn2 = 0;
            do {
                idSegDestino = myRandom.nextInt(nest);
                Estacion ed2 = estaciones.get(idSegDestino);
                def2 = ed2.getNumBicicletasNext() <= ed2.getDemanda();
                if (cn2 >= estaciones.size()) def2 = true;
                ++cn2;
            }
            while (idSegDestino == idEstOrigen || idSegDestino == idPrimDestino || !def2);

            Estacion origen = estaciones.get(idEstOrigen);
            int bicisOrigen = origen.getNumBicicletasNoUsadas();
            //if (origen.getNumBicicletasNext()-bicisOrigen < origen.getDemanda()) bicisOrigen = origen.getNumBicicletasNext()-origen.getDemanda();
            int bicisfurg = (bicisOrigen >= 30)? 30: bicisOrigen;
            int bicis1est;
            int delta = estaciones.get(idPrimDestino).getDemanda() - estaciones.get(idPrimDestino).getNumBicicletasNext();
            if (delta > 0)
                bicis1est = (bicisfurg >= delta)? delta: bicisfurg;
            else {
                bicis1est = 0;
            }
            f = new Furgoneta(
                    estaciones.get(idEstOrigen),
                    estaciones.get(idPrimDestino),
                    estaciones.get(idSegDestino),
                    bicisfurg,
                    bicis1est
            );
            this.add(f);
        }
    }

    public Furgonetas(int nfurg, int nest, Estaciones estaciones)
    {
        this.nfurg = nfurg;
        this.nest = nest;
        this.estaciones = estaciones;
    }

    @Override
    public Furgonetas clone() {
        super.clone();
        Furgonetas nF = new Furgonetas(nfurg, nest, estaciones);
        for (Furgoneta f : this) {
            nF.add(f.clone());
        }
        return nF;
    }
}
