import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Estado {
    private Furgonetas furgonetas;
    private static Estaciones estaciones;
    private ArrayList<Integer> bicisE;
    private ArrayList<Boolean> ini;
    private static Map<Estacion, Integer> m;

    public static String SUSTITUIR_ESTACION = "sustituir_estacion";
    public static String DEJAR_BICIS = "dejar_bicis";
    public static String RECOGER_BICIS = "recoger_bicis";
    public static String CAMBIAR_ESTACION_ORIGEN = "cambiar_estacion_origen";
    public static String QUITAR_ESTACION = "quitar_estacion";

private class par {
    private Estacion origen;
    private int bicisOrigen;
    public par(Estacion estOrigen, int bicis) {
        origen = estOrigen;
        bicisOrigen = bicis;
    }
    public Estacion getFirst() {return origen;}
    public void setFirst(Estacion estOrigen) {origen = estOrigen;}
    public int getSecond() {return bicisOrigen;}
    public void setSecond(int bicis) {bicisOrigen = bicis;}
}

    public Estado(int indice, int nf, Estaciones est, long seed) {
        estaciones = est;
        this.bicisE = new ArrayList<>();
        this.ini = new ArrayList<>();
        m = new HashMap<>();
        for (int i = 0; i < estaciones.size(); ++i) {
            m.put(estaciones.get(i), i);
            bicisE.add(0);
            ini.add(false);
        }
        switch (indice) {
            case 1:
                generadorEstadoInicial1(nf, seed);
                break;
            case 2:
                generadorEstadoInicial2(nf, seed);
                break;
            default:
                System.out.println("El indice no es valido");
        }
    }

    public Estado(Estado estado) {
        //TODO: falta implementar la copia de bicisE, en principio un loop por el Array es suficiente. Also, nunca añadimos nada a bicisE
        this.furgonetas = estado.furgonetas.clone();
        estaciones = estado.getEstaciones();
        this.bicisE = new ArrayList<>(estado.getBicisE());
        this.ini = new ArrayList<>(estado.ini);
    }

    private void generadorEstadoInicial1(int nf, long seed) {
        furgonetas = new Furgonetas(nf, estaciones.size(), seed, estaciones);
        this.bicisE = new ArrayList<>();
        this.ini = new ArrayList<>();
        m = new HashMap<>();
        for (int i = 0; i < estaciones.size(); ++i) {
            m.put(estaciones.get(i), i);
            bicisE.add(0);
            ini.add(false);
        }
        for (Furgoneta f : this.furgonetas)
        {
            Estacion o = f.getOrigen();
            if(o != null)
            {
                int io = m.get(o);
                int bic = bicisE.get(io);
                ini.set(io,true);
                int rec = f.getBicisEstacionOrigen();
                if ((o.getNumBicicletasNoUsadas()+bic)-rec < 0)
                {
                    rec = 0;
                    f.setBicisEstacionOrigen(0);
                    f.setBicisPrimeraEstacion(0);
                }
                bicisE.set(io, bic-rec);
            }

            Estacion e1 = f.getPrimerDestino();
            if(e1 != null)
            {
                int index = m.get(e1);
                int bic = this.bicisE.get(index);
                int llev = f.getBicisPrimeraEstacion();
                bic += llev;
                this.bicisE.set(index, bic);
            }

            Estacion e2  = f.getSegundoDestino();
            if(e2 != null)
            {
                int i2 = m.get(e2);
                Integer bic2 = this.bicisE.get(i2);
                Integer llev2 = f.getBicisEstacionOrigen()-f.getBicisPrimeraEstacion();
                bic2 += llev2;
                this.bicisE.set(i2,bic2);
            }
        }
    }

    private void generadorEstadoInicial2(int nf, long seed) {
        Queue<par> estacionesOrigen = new ConcurrentLinkedQueue<>();
        Queue<Estacion> estacionesDestino = new ConcurrentLinkedQueue<>();
        furgonetas = new Furgonetas(nf, estaciones.size(), estaciones);
        int i = 0;
        while (i < estaciones.size()) {
            Estacion e = estaciones.get(i);
            if (e.getDemanda() <= e.getNumBicicletasNext()) {
                int bicisOrigen = (e.getNumBicicletasNoUsadas() > 30) ? 30 : e.getNumBicicletasNoUsadas();
                par p = new par(e, bicisOrigen);
                System.out.println("He metido la estacion " + i + "en la cola de origen con " + bicisOrigen + "bicis");
                estacionesOrigen.add(p);
            }
            else if (e.getDemanda() < e.getNumBicicletasNext() + e.getNumBicicletasNoUsadas()) {
                int bicisCoger = e.getNumBicicletasNoUsadas() + e.getNumBicicletasNext() - e.getDemanda();
                int bicisOrigen = (bicisCoger > 30) ? 30 : bicisCoger;
                par p = new par(e, bicisOrigen);
                System.out.println("He metido la estacion " + i + "en la cola de origen con " + bicisOrigen + "bicis");
                estacionesOrigen.add(p);
            }
            else {
                System.out.println("He metido la estacion " + i + "en la cola");
                estacionesDestino.add(e);
            }
            ++i;
        }
        if (!estacionesDestino.isEmpty()) {
            Estacion primerDestino = estacionesDestino.peek();
            estacionesDestino.poll();
            Estacion segundoDestino = estacionesDestino.peek();
            estacionesDestino.poll();
            Estacion origen = estaciones.get(j);
            int bicisPrimeraEst;
            if (segundoDestino != null) {//Si hay qu repartir entre la primera y la segunda
                //bicisRestantes siempre sera mayor que 0 porque sino no estaria en la cola
                int bicisRestantes = primerDestino.getDemanda() - primerDestino.getNumBicicletasNext() - primerDestino.getNumBicicletasNoUsadas();
                bicisPrimeraEst = (bicisRestantes > 29) ? 29 : bicisRestantes;
            }
            else bicisPrimeraEst = bicisOrigen;//Sino todas para la primera
            int io = m.get(origen);//origen sera no nulo porque sino no estaria en esta rama
            bicisE.set(io, bicisE.get(io) - bicisOrigen);
            int ip = m.get(primerDestino);//tampoco es nulo
            bicisE.set(ip, bicisE.get(ip) + bicisPrimeraEst);
            if (noNulo(segundoDestino)) {
                int is = m.get(primerDestino);
                bicisE.set(is, bicisE.get(is) + (bicisOrigen - bicisPrimeraEst));
            }
            Furgoneta f = new Furgoneta(origen, primerDestino, segundoDestino, bicisOrigen, bicisPrimeraEst);
            ++contador;
            furgonetas.add(f);
        }
        Furgoneta f = new Furgoneta(null, null, null, 0, 0);
    }

            i = j + 1;
        }
        for (int j = contador; j < nf; ++j) furgonetas.add(new Furgoneta(null, null, null, 0, 0));
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
        return noNulo(vieja) && noNulo(nueva) && !nueva.equals(vieja) && coincideEstacionDestino(vieja, f);
    }

    public void sustituirEstacion(Estacion vieja, Estacion nueva, Furgoneta f) {
        if (f.getPrimerDestino().equals(vieja))
        {
            f.setPrimerDestino(nueva);
            int idv = m.get(vieja);
            int idn = m.get(nueva);
            int biv = bicisE.get(idv);
            int bin = bicisE.get(idn);
            int llev = f.getBicisPrimeraEstacion();
            bicisE.set(idv, biv-llev);
            bicisE.set(idn, bin+llev);
        }
        else
        {
            f.setSegundoDestino(nueva);
            int idv = m.get(vieja);
            int idn = m.get(nueva);
            int biv = bicisE.get(idv);
            int bin = bicisE.get(idn);
            int llev = f.getBicisEstacionOrigen()-f.getBicisPrimeraEstacion();
            bicisE.set(idv, biv-llev);
            bicisE.set(idn, bin+llev);
        }

    }

    public boolean puedeDejarBicis(Furgoneta f, int n) {
        return n >= 0 && n <= f.getBicisEstacionOrigen() && noNulo(f.getPrimerDestino());
    }

    public void dejarBicis(Furgoneta f, int n) {
        int oldn = f.getBicisPrimeraEstacion();
        int old2 = f.getBicisSegundaEstacion();
        f.setBicisPrimeraEstacion(n);
        int ip = m.get(f.getPrimerDestino());
        int bp = bicisE.get(ip)-(oldn-n);
        bicisE.set(ip, bp);
        if (noNulo(f.getSegundoDestino())) {
            int is = m.get(f.getSegundoDestino());
            int bs = bicisE.get(is) -(old2-f.getBicisSegundaEstacion());
            bicisE.set(is, bs);
        }
    }

    public boolean puedeRecogerBicis(Furgoneta f, int n) {
        return n <= f.getOrigen().getNumBicicletasNoUsadas() && n >= 0 && n <= 30;
    }

    public void recogerBicis(Furgoneta f, int n) {
        int oldn = f.getBicisEstacionOrigen();
        int old1 = f.getBicisPrimeraEstacion();
        int old2 = f.getBicisSegundaEstacion();
        f.setBicisEstacionOrigen(n);
        int i = m.get(f.getOrigen());
        int bic = bicisE.get(i);
        bic = (bic+oldn)-n;
        bicisE.set(i, bic);

        if (f.getBicisPrimeraEstacion() > n)
        {
            int k = m.get(f.getPrimerDestino());
            f.setBicisPrimeraEstacion(n);
            int ob = bicisE.get(k);
            bicisE.set(m.get(f.getPrimerDestino()), (ob-old1)+n);
        }

        if (f.getSegundoDestino() != null)
        {
            int j = m.get(f.getSegundoDestino());
            int oldb = bicisE.get(j);
            int nowbicfurg = f.getBicisSegundaEstacion();
            bicisE.set(j, (oldb-old2)+nowbicfurg);
        }
    }

    public boolean puedeCambiarEstacionOrigen(Estacion e, Furgoneta f) {
        return !ini.get(m.get(e)) &&(!e.equals(f.getPrimerDestino()) && !e.equals(f.getSegundoDestino()));
    }

    public void cambiarEstacionOrigen(Estacion e, Furgoneta f) {
        int olde = getM().get(f.getOrigen());
        int oldbic = bicisE.get(olde);
        int oldrec = f.getBicisEstacionOrigen();
        int old1 = f.getBicisPrimeraEstacion();
        int old2 = f.getBicisSegundaEstacion();
        ini.set(olde, false);
        bicisE.set(olde,oldbic+oldrec);
        f.setOrigen(e);
        int ide= getM().get(e);
        ini.set(ide, true);
        //System.out.print("Cambiar origen por "+ide+"\n");
        int bic= bicisE.get(ide);
        int disp = e.getNumBicicletasNoUsadas()+bic;
        if (e.getNumBicicletasNext()-disp < e.getDemanda()) disp = e.getNumBicicletasNext()-e.getDemanda();
        int rec = 0;
        if (disp > 0 )
        {
          rec = (disp > 30)? 30:disp;
        }
        bicisE.set(ide,bic-rec);
        f.setBicisEstacionOrigen(rec);

        if (old1 > rec)
        {
            int i1d = m.get(f.getPrimerDestino());
            int ob = bicisE.get(i1d);
            bicisE.set(i1d, ob -(old1-rec));
            f.setBicisPrimeraEstacion(rec);
        }

        if (f.getSegundoDestino() != null)
        {
            int ob2 = bicisE.get(m.get(f.getSegundoDestino()));
            bicisE.set(m.get(f.getSegundoDestino()), ob2-old2+f.getBicisSegundaEstacion());
        }

        //TO DO:
    }

    public boolean puedeQuitarEstacion(Estacion e, Furgoneta f) {
        return coincideEstacionDestino(e, f);
    }

    public void quitarEstacion(Estacion e, Furgoneta f) {
        if (f.getPrimerDestino().equals(e)) {
            int i = m.get(f.getPrimerDestino());
            bicisE.set(i, bicisE.get(i) - f.getBicisPrimeraEstacion());
            if (f.getSegundoDestino() != null)
            {
                int old2 = f.getBicisSegundaEstacion();
                f.setPrimerDestino(f.getSegundoDestino());
                f.setSegundoDestino(null);
                f.setBicisPrimeraEstacion(f.getBicisEstacionOrigen());
                Estacion n1 = f.getPrimerDestino(); //antes conocido como SegundoDestino
                int ob = bicisE.get(m.get(n1));
                bicisE.set(m.get(n1), (ob-old2)+f.getBicisPrimeraEstacion());
            }
            else {
                f.setPrimerDestino(null);
                f.setBicisPrimeraEstacion(0);
                f.setOrigen(null);
                f.setBicisEstacionOrigen(0);
            }
        }
        else {
            int j = m.get(f.getSegundoDestino());
            bicisE.set(j, bicisE.get(j) - f.getBicisSegundaEstacion());
            f.setSegundoDestino(null);
            int ob = bicisE.get(m.get(f.getPrimerDestino()));
            int old1 = f.getBicisPrimeraEstacion();
            f.setBicisPrimeraEstacion(f.getBicisEstacionOrigen());
            bicisE.set(m.get(f.getPrimerDestino()), ob -(old1-f.getBicisEstacionOrigen()));
        }
    }


    private boolean coincideEstacionDestino(Estacion e, Furgoneta f) {
        return (noNulo(f.getPrimerDestino()) && f.getPrimerDestino().equals(e)) || (noNulo(f.getSegundoDestino()) && f.getSegundoDestino().equals(e));
    }

    private boolean noNulo(Estacion e) {return e != null;}

    public ArrayList<Integer> getBicisE() {
        return bicisE;
    }

    public void setBicisE(ArrayList<Integer> bicisE) {
        this.bicisE = bicisE;
    }

    public void writeEstado() {
        for (int i = 0; i < furgonetas.size(); ++i) {
            System.out.println("Furgoneta num " + i);
            Furgoneta f = furgonetas.get(i);
            if (!f.estaVacia()) f.writeFurgoneta();
            else System.out.println("esta vacia ");
        }
        for (int i = 0; i < estaciones.size(); ++i) {
            System.out.println("Estacion num " + i + " tiene " + bicisE.get(i) + " bicis");
        }
    }
}
