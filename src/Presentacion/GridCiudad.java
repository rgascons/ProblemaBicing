package Presentacion;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridCiudad extends Canvas {

    int width;
    int height;
    int nEst;
    int nEstPorFilaYColumna;
    int separacionW;
    int separacionH;

    public GridCiudad(int width, int height) {
        this.width = width;
        this.height = height;
        init(width, height);
    }

    public GridCiudad(int width, int height, int nEst) {
        this.width = width;
        this.height = height;
        this.nEst = nEst;
        this.nEstPorFilaYColumna = ((int) Math.ceil(Math.sqrt(nEst)))+2;
        this.separacionW = width / nEstPorFilaYColumna;
        this.separacionH = height / nEstPorFilaYColumna;
        init(width, height);
        draw();
    }

    public void reDraw(int nEst) {
        this.nEst = nEst;
        this.nEstPorFilaYColumna = ((int) Math.ceil(Math.sqrt(nEst)))+2;
        this.separacionW = width / nEstPorFilaYColumna;
        this.separacionH = height / nEstPorFilaYColumna;
        draw();
    }

    private void init(int width, int height) {
        setHeight(height);
        setWidth(width);
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        int maxi = ((int)(getWidth()/separacionW))*separacionW;
        int maxj = ((int)(getHeight()/separacionH))*separacionH;
        gc.setLineWidth(2);
        for (int i = separacionW; i < getWidth(); i+=separacionW) {
            for (int j = separacionH; j < getHeight(); j+=separacionH) {
                if (j == separacionH) {
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(0.3);
                    gc.strokeLine(i, j, i, maxj);
                    gc.setLineWidth(2);
                    gc.setStroke(Color.RED);
                }
                if (i == separacionW) {
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(0.3);
                    gc.strokeLine(i, j , maxi, j);
                    gc.setLineWidth(2);
                    gc.setStroke(Color.RED);
                }
                if (i == separacionW || i == maxi || i == getWidth()-separacionW || j == separacionH || j == maxj || j == getHeight()-separacionH) {
                    gc.setStroke(Color.WHITE);
                }
                else gc.setStroke(Color.RED);
                gc.strokeLine(i, j, i, j);
                /*gc.setFont(Font.font("Abadi MT Condensed Light", 10));
                gc.strokeText("H", i, j);*/
            }
        }
    }
}
