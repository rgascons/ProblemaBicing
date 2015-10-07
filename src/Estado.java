import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.Random;

public class Estado {
    private Furgonetas furgonetas;
    private Estaciones estaciones;
    private ArrayList bicisE;
    private static Random r;
    
    public Estado(Furgonetas furgonetas, Estaciones estaciones, ArrayList bicisE) {
    }

    public Estado(int nf,) {
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
}
