package Dominio;

import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Estado {
    private Furgonetas furgonetas;
    private static Estaciones estaciones;
    private ArrayList<Integer> bicisE;
    private ArrayList<Boolean> ini;
    private static Map<Estacion, Integer> m;

    public static String SUSTITUIR_ESTACION = "sustituir_estacion";
    public static String DEJAR_BICIS = "dejar_bicis";
    public static String ELIMINAR_FURGONETA = "eliminar_furgoneta";
    public static String ANADIR_FURGONETA = "añadir_furgoneta";
    public static String CAMBIAR_ESTACION_ORIGEN = "cambiar_estacion_origen";
    public static String QUITAR_ESTACION = "quitar_estacion";
    public static String RECOGER_BICIS = "recoger_bicis";

    private class comparador implements Comparator<par> {
        @Override
        public int compare(par o1, par o2) {
            return -(o1.getBicisOrigen() - o2.getBicisOrigen());
        }
    }

    private class par {
        private Estacion origen;
        private int bicisOrigen;
        public par(Estacion estOrigen, int bicis) {
            origen = estOrigen;
            bicisOrigen = bicis;
        }
        public Estacion getOrigen() {return origen;}
        public void setOrigen(Estacion org) {origen = org;}
        public int getBicisOrigen() {return bicisOrigen;}
        public void setBicisOrigen(int bo) {bicisOrigen = bo;}
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
                generadorEstadoInicial2(nf);
                break;
            default:
                System.out.println("El indice no es valido");
        }
        //this.writeEstado();
    }

    public Estado(Estado estado) {
        this.furgonetas = estado.furgonetas.clone();
        estaciones = estado.getEstaciones();
        this.bicisE = new ArrayList<>(estado.getBicisE());
        this.ini = new ArrayList<>(estado.ini);
    }

    private void generadorEstadoInicial1(int nf, long seed) {
        furgonetas = new Furgonetas(nf, estaciones.size(), estaciones);
        boolean orig[] = new boolean[estaciones.size()];
        Random myRandom = new Random(seed);

        this.bicisE = new ArrayList<>();
        this.ini = new ArrayList<>();
        m = new HashMap<>();
        for (int i = 0; i < estaciones.size(); ++i) {
            m.put(estaciones.get(i), i);
            bicisE.add(0);
            ini.add(false);
        }

        for (int i = 0; i < nf ; ++i)
        {
            int idEstOrigen;
            int cn0 = 0;
            boolean excedente;
            do {
                idEstOrigen = myRandom.nextInt(estaciones.size());
                Estacion eo = estaciones.get(idEstOrigen);
                excedente = eo.getNumBicicletasNext() > eo.getDemanda();
                if (cn0 >= estaciones.size()) excedente = true;
                ++cn0;
            }
            while (orig[idEstOrigen] || !excedente); //Si ya es un origen o no tiene excedente de bicis...
            orig[idEstOrigen] = true;
            int idPrimDestino;
            boolean deficit;
            int cn1 = 0;
            do {
                idPrimDestino = myRandom.nextInt(estaciones.size());
                Estacion ed1 = estaciones.get(idPrimDestino);
                deficit = ed1.getNumBicicletasNext() <= ed1.getDemanda();
                if (cn1 >= estaciones.size()) deficit = true;
                ++cn1;
            }
            while (idEstOrigen == idPrimDestino && !deficit);
            int idSegDestino;
            boolean def2;
            int cn2 = 0;
            do {
                idSegDestino = myRandom.nextInt(estaciones.size());
                Estacion ed2 = estaciones.get(idSegDestino);
                def2 = ed2.getNumBicicletasNext() <= ed2.getDemanda();
                if (cn2 >= estaciones.size()) def2 = true;
                ++cn2;
            }
            while (idSegDestino == idEstOrigen || idSegDestino == idPrimDestino || !def2);

            //TODO Solo coger las bicis necesarias
            Estacion origen = estaciones.get(idEstOrigen);
            Estacion dest1 = estaciones.get(idPrimDestino);
            Estacion dest2 = estaciones.get(idSegDestino);
            int bicisOrigen = origen.getNumBicicletasNoUsadas()+bicisE.get(idEstOrigen);
            int bicisNec1 =  dest1.getDemanda()-(dest1.getNumBicicletasNext()+bicisE.get(idPrimDestino));
            int bicisNec2 = dest2.getDemanda()-(dest2.getNumBicicletasNext()+bicisE.get(idSegDestino));
            // Si al coger todas las bicis en origen no cumplimos la demanda, cogemos la diferencia:
            bicisOrigen = ((origen.getNumBicicletasNext()+bicisE.get(idPrimDestino))-bicisOrigen < origen.getDemanda())? (origen.getNumBicicletasNext()+bicisE.get(idPrimDestino))-origen.getDemanda():bicisOrigen;
            // Si en origen hay más bicicletas de las necesarias, cogemos solo las necesarias:
            //bicisOrigen = (bicisOrigen > bicisNec1+bicisNec2)? bicisNec1+bicisNec2: bicisOrigen;
            // Si hay más de 30 bicis para llevar, coge 30:
            int bicisfurg = (bicisOrigen >= 30)? 30: bicisOrigen;
            int bicis1est;
            if (bicisNec1 > 0)
                bicis1est = (bicisfurg >= bicisNec1)? bicisNec1 : bicisfurg;
            else {
                bicis1est = 0;
            }
            Furgoneta f = new Furgoneta(
                    estaciones.get(idEstOrigen),
                    estaciones.get(idPrimDestino),
                    estaciones.get(idSegDestino),
                    bicisfurg,
                    bicis1est
            );
            furgonetas.add(f);

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



    private void generadorEstadoInicial2(int nf) {
        Queue<par> estacionesOrigen = new PriorityQueue<>(new comparador());
        Queue<Estacion> estacionesDestino = new ConcurrentLinkedQueue<>();
        furgonetas = new Furgonetas(nf, estaciones.size(), estaciones);
        for (int i = 0; i < estaciones.size(); ++i) {
            Estacion e = estaciones.get(i);
            if (e.getDemanda() <= e.getNumBicicletasNext()) {
                int bicisOrigen = (e.getNumBicicletasNoUsadas() > 30) ? 30 : e.getNumBicicletasNoUsadas();
                par p = new par(e, bicisOrigen);
                //System.out.println("He metido la estacion " + i + " en la cola de origen con " + bicisOrigen + " bicis");
                estacionesOrigen.add(p);
            }
            else if (e.getDemanda() < e.getNumBicicletasNext() + e.getNumBicicletasNoUsadas()) {
                int bicisCoger = e.getNumBicicletasNoUsadas() + e.getNumBicicletasNext() - e.getDemanda();//Para saber cuantas puedo coger
                int bicisOrigen = (bicisCoger > 30) ? 30 : bicisCoger;
                par p = new par(e, bicisOrigen);
                //System.out.println("He metido la estacion " + i + " en la cola de origen con " + bicisOrigen + " bicis");
                estacionesOrigen.add(p);
            } else {
                //System.out.println("He metido la estacion " + i + " en la cola de destinos");
                estacionesDestino.add(e);
            }
        }
        int i = 0;
        while (i < nf && !estacionesOrigen.isEmpty() && !estacionesDestino.isEmpty()) {
            par p = estacionesOrigen.peek();
            estacionesOrigen.poll();
            Estacion origen = p.getOrigen();
            int bicisOrigen = p.getBicisOrigen();
            Estacion primerDestino = estacionesDestino.peek();
            estacionesDestino.poll();
            Estacion segundoDestino = estacionesDestino.peek();
            estacionesDestino.poll();
            int bicisPrimeraEst;
            if (segundoDestino != null) {//Si hay que repartir entre la primera y la segunda
                int bicisRestantes = primerDestino.getDemanda() - primerDestino.getNumBicicletasNext()- primerDestino.getNumBicicletasNoUsadas();//bicisE.get(m.get(primerDestino)))
                //Si se necesitan mas bicis de las que se han recogido se cogen las que se han recogido menos 1 para que lleve 1 a la segunda estacion
                bicisPrimeraEst = (bicisRestantes >= bicisOrigen) ? bicisOrigen - 1 : bicisRestantes;
            }
            else bicisPrimeraEst = bicisOrigen;//Sino todas para la primera
            int io = m.get(origen);
            bicisE.set(io, bicisE.get(io) - bicisOrigen);
            int ip = m.get(primerDestino);//tampoco es nulo
            bicisE.set(ip, bicisE.get(ip) + bicisPrimeraEst);
            if (noNulo(segundoDestino)) {
                int is = m.get(primerDestino);
                bicisE.set(is, bicisE.get(is) + (bicisOrigen - bicisPrimeraEst));
            }
            Furgoneta f = new Furgoneta(origen, primerDestino, segundoDestino, bicisOrigen, bicisPrimeraEst);
            furgonetas.add(f);
            ++i;
        }
        for (int j = i; j < nf; ++j) furgonetas.add(new Furgoneta(null, null, null, 0, 0));
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
        return f != null && f.getOrigen() != null && n <= f.getOrigen().getNumBicicletasNoUsadas() && n >= 0 && n <= 30;
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

        int old1 = f.getBicisPrimeraEstacion();
        int old2 = f.getBicisSegundaEstacion();
        if (!f.estaVacia()) {
            int olde = getM().get(f.getOrigen());
            int oldbic = bicisE.get(olde);
            int oldrec = f.getBicisEstacionOrigen();
            ini.set(olde, false);
            bicisE.set(olde, oldbic + oldrec);
        }
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
                int oldo = bicisE.get(m.get(f.getOrigen()));
                bicisE.set(m.get(f.getOrigen()), oldo-f.getBicisEstacionOrigen());
                int old1 = bicisE.get(m.get(f.getPrimerDestino()));
                bicisE.set(m.get(f.getPrimerDestino()), old1-f.getBicisPrimeraEstacion());
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

    public boolean puedeEliminarFurgoneta(int i)
    {
        return !furgonetas.get(i).estaVacia();
    }

    public void eliminarFurgoneta(int i)
    {
        Furgoneta f = furgonetas.get(i);
        int ido = m.get(f.getOrigen());

        if (f.getPrimerDestino() != null)
        {
            int id1 = m.get(f.getPrimerDestino());
            int old1 = bicisE.get(id1);
            bicisE.set(id1, old1-f.getBicisPrimeraEstacion());
        }

        if (noNulo(f.getSegundoDestino())) {
            int id2 = m.get(f.getSegundoDestino());
            int old2 = bicisE.get(id2);
            bicisE.set(id2, old2-f.getBicisSegundaEstacion());
        }
        int oldo = bicisE.get(ido);

        bicisE.set(ido, oldo+f.getBicisEstacionOrigen());

        f.setOrigen(null);
        f.setPrimerDestino(null);
        f.setSegundoDestino(null);
        f.setBicisEstacionOrigen(0);
        f.setBicisPrimeraEstacion(0);
    }

    public boolean puedeAnadirFurgoneta(int i)
    {
        return furgonetas.get(i).estaVacia();
    }

    public void anadirFurgoneta(int i)
    {
        Furgoneta f = furgonetas.get(i);
        f.setOrigen(estaciones.get(0));
        f.setBicisEstacionOrigen(f.getOrigen().getNumBicicletasNext()-bicisE.get(0));
        f.setPrimerDestino(estaciones.get(1));
        f.setBicisPrimeraEstacion(f.getBicisEstacionOrigen());
        f.setSegundoDestino(estaciones.get(2));
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
            System.out.println("Dominio.Furgoneta num " + i);
            Furgoneta f = furgonetas.get(i);
            if (!f.estaVacia()) f.writeFurgoneta();
            else System.out.println("esta vacia ");
        }
        for (int i = 0; i < estaciones.size(); ++i) {
            System.out.println("Estacion num " + i + " tiene " + bicisE.get(i) + " bicis");
        }
    }
}
