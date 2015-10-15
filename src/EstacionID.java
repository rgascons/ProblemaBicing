import IA.Bicing.Estacion;

public class EstacionID implements Cloneable {
    private Estacion est;
    private int id;
    private int bicis;

    public EstacionID(Estacion est, int id, int bicis) {
        this.setEst(est);
        this.setId(id);
        this.setBicis(bicis);
    }

    public Estacion getEst() {
        return est;
    }

    public void setEst(Estacion est) {
        this.est = est;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBicis() {
        return bicis;
    }

    public void setBicis(int bicis) {
        this.bicis = bicis;
    }

    public EstacionID clone() {
        return new EstacionID(this.est, this.id, this.bicis);
    }
}
