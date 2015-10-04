import IA.Bicing.Estaciones;

public class Estado {
    private Furgonetas furgonetas;
    private Estaciones estaciones;
    
    public Estado(Furgonetas furgonetas, Estaciones estaciones) {
        this.furgonetas = furgonetas;
        this.estaciones = estaciones;
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
