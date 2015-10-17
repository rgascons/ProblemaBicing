import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.Random;

public class Furgonetas extends ArrayList<Furgoneta> implements Cloneable{

    private int nfurg;
    private int nest;
    private long seed;
    private Estaciones estaciones;

    public Furgonetas(int nfurg, int nest, long seed, Estaciones estaciones) {

        this.nfurg = nfurg;
        this.nest = nest;
        this.seed = seed;
        this.estaciones = estaciones;

        Random myRandom = new Random(seed);

        Furgoneta f;
        for (int i = 0; i < nfurg; ++i) {
            int idEstOrigen = myRandom.nextInt(nest);
            int idPrimDestino = myRandom.nextInt(nest);
            int idSegDestino = myRandom.nextInt(nest);
            f = new Furgoneta(
                    estaciones.get(idEstOrigen),
                    estaciones.get(idPrimDestino),
                    estaciones.get(idSegDestino),
                    estaciones.get(idPrimDestino).getNumBicicletasNoUsadas(),
                    estaciones.get(idPrimDestino).getNumBicicletasNoUsadas()
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
