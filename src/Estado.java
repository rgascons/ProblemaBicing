import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.Random;

public class Estado {
    private Furgonetas furgonetas;
    private Estaciones estaciones;
    private ArrayList bicisE;
    private static Random r;
    public static String[] op = {"sustituir_estacion(nueva_estacion, estacion_antigua, idF)",
            "dejar_bicis(idF, idE, n)",
            "recoger_bicis(idF, idE, n)",
            "cambiar_estacion_origen(idF, idE)",
            "quitar_estacion(estacion, idF)"
    };
    
    public Estado(Furgonetas furgonetas, Estaciones estaciones, ArrayList bicisE) {
    }

    public Estado(int nf, Estaciones estaciones) {
        furgonetas = new Furgonetas(nf, estaciones.size(), System.currentTimeMillis(), estaciones);
        this.estaciones = estaciones;
    }
    public Estado(int nf, int ne) {

    }

    public Estado(Estado estado) {
        //TODO: constructor por copia
    }

    public Furgonetas getFurgonetas() {
        return furgonetas;
    }

    public void setFurgonetas(Furgonetas furgonetas) {
        this.furgonetas = furgonetas;
    }

    public Estaciones getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(Estaciones estaciones) {
        this.estaciones = estaciones;
    }

    public boolean puedeSustituirEstacion(int idEVieja, int idENueva, int idF) {
        return idENueva != idEVieja && (furgonetas.get(idF).getPrimerDestino().equals(estaciones.get(idEVieja)));
    }

    private boolean dentroLimites(int id) {
        return 0 >= id && id < estaciones.size();
    }
}
