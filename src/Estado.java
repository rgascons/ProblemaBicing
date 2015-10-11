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
            "quitar_estacion(idE, idF)"
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
        return dentroLimitesEstaciones(idEVieja) &&
                dentroLimitesEstaciones(idENueva) &&
                dentroLimitesFurgonetas(idF) &&
                idENueva != idEVieja &&
                coincideEstacionDestino(idF, idEVieja);
    }

    public void sustituirEstacion(int idEVieja, int idENueva, int idF) {
        Furgoneta f = furgonetas.get(idF);
        if (f.getPrimerDestino().equals(estaciones.get(idEVieja))) f.setPrimerDestino(estaciones.get(idENueva));
        else f.setSegundoDestino(estaciones.get(idENueva));
    }

    //TODO: Decidir si se hace implicitamente a la primera estacion o cambiar
    public boolean puedeDejarBicis(int idF, /*int idE, */int n) {
        return dentroLimitesFurgonetas(idF) &&
                //dentroLimitesEstaciones(idE) &&
                n > 0 && furgonetas.get(idF).getBicisEstacionOrigen() >= n;
    }

    //TODO: Lo de arriba implica lo de abajo
    public void dejarBicis(int idF, int idE, int n) {
        Furgoneta f = furgonetas.get(idF);
        if (f.getPrimerDestino().equals(estaciones.get(idE))) f.setBicisPrimeraEstacion(n);
    }

    public boolean puedeRecogerBicis(int idF, int n) {
        return n >= 0 && n <= 30 && dentroLimitesFurgonetas(idF) && furgonetas.get(idF).getOrigen() != null;
    }

    public void recogerBicis(int idF, int n) {
        furgonetas.get(idF).setBicisEstacionOrigen(n);
    }

    private boolean puedeCambiarEstacionOrigen(int idE, int idF) {
        return dentroLimitesEstaciones(idE) && dentroLimitesFurgonetas(idF);
    }

    private void cambiarEstacionOrigen(int idE, int idF) {
        furgonetas.get(idF).setOrigen(estaciones.get(idE));
    }

    private boolean puedeQuitarEstacion(int idE, int idF) {
        return dentroLimitesEstaciones(idE) && dentroLimitesFurgonetas(idF) && existeEstacionFurgoneta(idF, idE);
    }

    private void quitarEstacion(int idE, int idF) {
        Furgoneta f = furgonetas.get(idF);
        if (f.getPrimerDestino().equals(estaciones.get(idE))) f.setPrimerDestino(null);
        else f.setSegundoDestino(null);
    }

    private boolean existeEstacionFurgoneta(int idE, int idF) {
        return (furgonetas.get(idF).getPrimerDestino() != null && furgonetas.get(idF).getPrimerDestino().equals(estaciones.get(idE))) ||
        (furgonetas.get(idF).getSegundoDestino() != null && furgonetas.get(idF).getSegundoDestino().equals(estaciones.get(idE)));
    }

    private boolean coincideEstacionDestino(int idF, int idE) {
        return furgonetas.get(idF).getPrimerDestino().equals(estaciones.get(idE)) || furgonetas.get(idF).getSegundoDestino().equals(estaciones.get(idE));
    }


    private boolean dentroLimitesEstaciones(int id) {
        return 0 >= id && id < estaciones.size();
    }

    private boolean dentroLimitesFurgonetas(int id) {return 0 >= id && id < furgonetas.size();}
}
