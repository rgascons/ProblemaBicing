import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.Random;

public class Furgonetas extends ArrayList<Furgoneta> implements Cloneable{

    public Furgonetas(int nfurg, int nest, long seed, Estaciones estaciones) {
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
}
