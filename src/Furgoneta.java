import IA.Bicing.Estacion;

public class Furgoneta {
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
}
