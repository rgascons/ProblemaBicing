import IA.Bicing.Estacion;

public class Furgoneta implements Cloneable {
    private Estacion origen;
    private Estacion primerDestino;
    private Estacion segundoDestino;
    private int bicisEstacionOrigen;
    private int bicisPrimeraEstacion;

    public Furgoneta(Estacion origen, Estacion primerDestino, Estacion segundoDestino,
                     int bicisEstacionOrigen, int bicisPrimeraEstacion) {
        this.origen = origen;
        this.primerDestino = primerDestino;
        this.segundoDestino = segundoDestino;
        this.bicisEstacionOrigen = bicisEstacionOrigen;
        this.bicisPrimeraEstacion = bicisPrimeraEstacion;
    }

    public Estacion getOrigen() {
        return origen;
    }

    public void setOrigen(Estacion origen) {
        this.origen = origen;
    }

    public Estacion getPrimerDestino() {
        return primerDestino;
    }

    public void setPrimerDestino(Estacion primerDestino) {
        this.primerDestino = primerDestino;
    }

    public Estacion getSegundoDestino() {
        return segundoDestino;
    }

    public void setSegundoDestino(Estacion segundoDestino) {
        this.segundoDestino = segundoDestino;
    }

    public int getBicisEstacionOrigen() {
        return bicisEstacionOrigen;
    }

    public void setBicisEstacionOrigen(int bicisEstacionOrigen) {
        this.bicisEstacionOrigen = bicisEstacionOrigen;
    }

    public int getBicisPrimeraEstacion() {
        return bicisPrimeraEstacion;
    }

    public void setBicisPrimeraEstacion(int bicisPrimeraEstacion) {
        this.bicisPrimeraEstacion = bicisPrimeraEstacion;
    }

    public void writeFrurgoneta() {
        if (origen != null) System.out.println("Mi estacion de origen es la " + Estado.getM().get(origen));
        if (primerDestino != null) System.out.println("Mi primer destino es " + Estado.getM().get(primerDestino));
        if (segundoDestino != null) System.out.println("Mi segundo destino es " + Estado.getM().get(segundoDestino));
        if (bicisEstacionOrigen != 0) System.out.println("Recojo " + bicisEstacionOrigen + " bicis");
        if (bicisPrimeraEstacion != 0) System.out.println("En la primera estacion dejo" + bicisPrimeraEstacion);
        System.out.println("En la segunda estacion dejo" + (bicisEstacionOrigen - bicisPrimeraEstacion));
    }

    @Override
    public Furgoneta clone() {
        return new Furgoneta(origen, primerDestino, segundoDestino, bicisEstacionOrigen, bicisPrimeraEstacion);
    }
}
