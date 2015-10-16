import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Estado {
    private Furgonetas furgonetas;
    private static Estaciones estaciones;
    private ArrayList<Integer> bicisE;
    private static Map<Estacion, Integer> m;


    public static String SUSTITUIR_ESTACION = "sustituir_estacion";
    public static String DEJAR_BICIS = "dejar_bicis";
    public static String RECOGER_BICIS = "recoger_bicis";
    public static String CAMBIAR_ESTACION_ORIGIEN = "cambiar_estacion_origen";
    public static String QUITAR_ESTACION = "quitar_estacion";

    public Estado(int nf, Estaciones est) {
        estaciones = est;
        furgonetas = new Furgonetas(nf, estaciones.size(), System.currentTimeMillis(), estaciones);
        bicisE = new ArrayList<Integer>();
        m = new HashMap<Estacion, Integer>();
        for (int i = 0; i < estaciones.size(); ++i) {
            m.put(estaciones.get(i), i);
        }
        bicisE.addAll(m.values());
    }

    public Estado(Estado estado) {
        //TODO: falta implementar la copia de bicisE, en principio un loop por el Array es suficiente. Also, nunca añadimos nada a bicisE
        this.furgonetas = estado.furgonetas.clone();
        estaciones = estado.getEstaciones();
        this.bicisE = new ArrayList<Integer>();
        for (Integer i : estado.getBicisE()) {
            this.bicisE.add(i);
        }
    }

    public static Map<Estacion, Integer> getM() {
        return m;
    }

    public static void setM(Map<Estacion, Integer> map) {
        m = map;
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

    public ArrayList<Integer> getbicisE() {return bicisE;}

    public void setbicisE(ArrayList<Integer> bicisE) {this.bicisE = bicisE;}

    public boolean puedeSustituirEstacion(Estacion vieja, Estacion nueva, Furgoneta f) {
        return //dentroLimitesEstaciones(idEVieja) &&
                !nueva.equals(vieja) &&
                coincideEstacionDestino(vieja, f);
    }

    public void sustituirEstacion(Estacion vieja, Estacion nueva, Furgoneta f) {
        if (f.getPrimerDestino().equals(vieja)) f.setPrimerDestino(nueva);
        else f.setSegundoDestino(nueva);
    }

    public boolean puedeDejarBicis(Furgoneta f, int n) {
        return n > 0 && n <= f.getBicisEstacionOrigen();
    }

    public void dejarBicis(Furgoneta f, int n) {
        f.setBicisPrimeraEstacion(n);
        int ip = m.get(f.getPrimerDestino());
        int is = m.get(f.getSegundoDestino());
        int bp = bicisE.get(ip) + n;
        int bs = f.getBicisEstacionOrigen() - bp;
        bicisE.set(ip, bp);
        bicisE.set(is, bs);
    }

    public boolean puedeRecogerBicis(Furgoneta f, int n) {
        return n >= 0 && n <= 30 &&
                f.getOrigen() != null;
    }

    public void recogerBicis(Furgoneta f, int n) {
        f.setBicisEstacionOrigen(n);
        int i = m.get(f.getOrigen());
        bicisE.set(i, bicisE.get(i) - n);
    }

    public boolean puedeCambiarEstacionOrigen(Estacion e, Furgoneta f) {
        return true;
    }

    public void cambiarEstacionOrigen(Estacion e, Furgoneta f) {
        f.setOrigen(e);
    }

    public boolean puedeQuitarEstacion(Estacion e, Furgoneta f) {
        return existeEstacionFurgoneta(e, f);
    }

    public void quitarEstacion(Estacion e, Furgoneta f) {
        if (f.getPrimerDestino().equals(e)) {
            bicisE.set(m.get(f.getPrimerDestino()), 0);
            f.setPrimerDestino(null);

        }
        else {
            bicisE.set(m.get(f.getSegundoDestino()), 0);
            f.setSegundoDestino(null);
        }
    }

    private boolean existeEstacionFurgoneta(Estacion e, Furgoneta f) {
        return (f.getPrimerDestino() != null && f.getPrimerDestino().equals(e)) ||
        (f.getSegundoDestino() != null && f.getSegundoDestino().equals(e));
    }

    private boolean coincideEstacionDestino(Estacion e, Furgoneta f) {
        return f.getPrimerDestino().equals(e) || f.getSegundoDestino().equals(e);
    }

    public ArrayList<Integer> getBicisE() {
        return bicisE;
    }

    public void setBicisE(ArrayList<Integer> bicisE) {
        this.bicisE = bicisE;
    }
}
