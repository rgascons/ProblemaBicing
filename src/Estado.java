import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;

import java.util.ArrayList;

public class Estado {
    private Furgonetas furgonetas;
    private Estaciones estaciones;
    private ArrayList<EstacionID> estacionesID;
    public static String[] op = {"sustituir_estacion(nueva_estacion, estacion_antigua, idF)",
            "dejar_bicis(idF, idE, n)",
            "recoger_bicis(idF, idE, n)",
            "cambiar_estacion_origen(idF, idE)",
            "quitar_estacion(idE, idF)"
    };

    public Estado(int nf, Estaciones est) {
        estaciones = est;
        furgonetas = new Furgonetas(nf, estaciones.size(), System.currentTimeMillis(), estaciones);
        estacionesID = new ArrayList<EstacionID>(estaciones.size());
        for (int i = 0; i < estaciones.size(); ++i) {
            EstacionID e = estacionesID.get(i);
            e.setEst(estaciones.get(i));
            e.setId(i);
            e.setBicis(0);
        }
    }

    public Estado(Estado estado) {
        //TODO: falta implementar la copia de bicisE, en principio un loop por el Array es suficiente. Also, nunca añadimos nada a bicisE
        this.furgonetas = estado.furgonetas.clone();
        this.estaciones = estado.estaciones;        //En principio estaciones es inmutable verdad? No haría falta un clone
                                                    //tengo mis dudas, mira NumBicicletasNoUsadas NumBicicletasNext en Estacion
        this.estacionesID = new ArrayList<EstacionID>();
        for (EstacionID e : estado.getEstacionesID()) {
              estacionesID.add(e.clone());
        }
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

    public ArrayList<EstacionID> getEstacionesID() {return estacionesID;}

    public void setBicisE(ArrayList<EstacionID> est) {this.estacionesID = est;}

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
        return n > 0 && n <= f.getBicisEstacionOrigen();
    }

    public void dejarBicis(Furgoneta f, int n) {
        f.setBicisPrimeraEstacion(n);

    }

    public boolean puedeRecogerBicis(Furgoneta f, int n) {
        return n >= 0 && n <= 30 &&
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
        return existeEstacionFurgoneta(e, f);
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
