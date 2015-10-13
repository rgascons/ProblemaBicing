import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import java.util.ArrayList;
import java.util.Random;

public class Estado {
    private Furgonetas furgonetas;
    private Estaciones estaciones;
    private ArrayList<Integer> bicisE;
    private static Random r;
    public static String[] op = {"sustituir_estacion(nueva_estacion, estacion_antigua, idF)",
            "dejar_bicis(idF, idE, n)",
            "recoger_bicis(idF, idE, n)",
            "cambiar_estacion_origen(idF, idE)",
            "quitar_estacion(idE, idF)"
    };

    public Estado(int nf, Estaciones est) {
        estaciones = est;
        furgonetas = new Furgonetas(nf, estaciones.size(), System.currentTimeMillis(), estaciones);
        bicisE = new ArrayList<Integer>();
    }

    public Estado(Estado estado) {
        //TODO: estos clones no hacen una deep copy. Hacer el contructor a base de news
        this.furgonetas = (Furgonetas) estado.furgonetas.clone();
        this.estaciones = (Estaciones) estado.estaciones.clone();
        this.bicisE = (ArrayList<Integer>) estado.bicisE.clone();
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

    public void setEstaciones(Estaciones est) {
        estaciones = est;
    }

    public ArrayList<Integer> getBicisE() {return bicisE;}

    public void setBicisE(ArrayList bicis) {bicisE = bicis;}

    public boolean puedeSustituirEstacion(Estacion vieja, Estacion nueva, Furgoneta f) {
        return //dentroLimitesEstaciones(idEVieja) &&
                //dentroLimitesEstaciones(idENueva) &&
                //dentroLimitesFurgonetas(idF) &&
                !nueva.equals(vieja) &&
                coincideEstacionDestino(vieja, f);
    }

    public void sustituirEstacion(Estacion vieja, Estacion nueva, Furgoneta f) {
        if (f.getPrimerDestino().equals(vieja)) f.setPrimerDestino(nueva);
        else f.setSegundoDestino(nueva);
    }

    public boolean puedeDejarBicis(Furgoneta f, int n) {
        return //dentroLimitesFurgonetas(idF) &&
                //dentroLimitesEstaciones(idE) &&
                n > 0 && n <= f.getBicisEstacionOrigen();
    }

    public void dejarBicis(Furgoneta f, int n) {
        f.setBicisPrimeraEstacion(n);
    }

    public boolean puedeRecogerBicis(Furgoneta f, int n) {
        return n >= 0 && n <= 30 &&
                //dentroLimitesFurgonetas(idF)
                f.getOrigen() != null;
    }

    public void recogerBicis(Furgoneta f, int n) {
        f.setBicisEstacionOrigen(n);
    }

    public boolean puedeCambiarEstacionOrigen(Estacion e, Furgoneta f) {
        return true;
    }

    public void cambiarEstacionOrigen(Estacion e, Furgoneta f) {
        f.setOrigen(e);
    }

    public boolean puedeQuitarEstacion(Estacion e, Furgoneta f) {
        return //dentroLimitesEstaciones(idE) &&
                //dentroLimitesFurgonetas(idF) &&
                existeEstacionFurgoneta(e, f);
    }

    public void quitarEstacion(Estacion e, Furgoneta f) {
        if (f.getPrimerDestino().equals(e)) f.setPrimerDestino(null);
        else f.setSegundoDestino(null);
    }

    private boolean existeEstacionFurgoneta(Estacion e, Furgoneta f) {
        return (f.getPrimerDestino() != null && f.getPrimerDestino().equals(e)) ||
        (f.getSegundoDestino() != null && f.getSegundoDestino().equals(e));
    }

    private boolean coincideEstacionDestino(Estacion e, Furgoneta f) {
        return f.getPrimerDestino().equals(e) || f.getSegundoDestino().equals(e);
    }

    /*private boolean dentroLimitesEstaciones(int id) {
        return 0 >= id && id < estaciones.size();
    }

    private boolean dentroLimitesFurgonetas(int id) {return 0 >= id && id < furgonetas.size();}*/
}
