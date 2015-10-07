import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.Random;

public class Furgonetas extends ArrayList<Furgoneta> {
    private Random myRandom;

    private Furgonetas() {}

    public Furgonetas(int nfurg, int nest, int seed, Estaciones estaciones) {
        this.myRandom = new Random((long)seed);

        Furgoneta f;
        for (int i = 0; i < nfurg; ++i) {
            int idEstOrigen = this.myRandom.nextInt(nest);
            int idPrimDestino = this.myRandom.nextInt(nest);
            int idSegDestino = this.myRandom.nextInt(nest);
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
}
