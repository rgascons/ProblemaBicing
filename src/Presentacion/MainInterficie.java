package Presentacion;

import Dominio.*;
import IA.Bicing.Estaciones;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Properties;


public class MainInterficie extends Application{

    private NumberTextField nEstText;
    private NumberTextField nFurgText;
    private NumberTextField nBicisText;
    private RadioButton randomSeed;
    private RadioButton customSeed;
    private NumberTextField customSeedField;
    private GridCiudad canvas;
    private ProgressBar progressBar;
    private RadioButton hillClimb;
    private RadioButton annealing;
    private NumberTextField numIt;
    private NumberTextField k;
    private NumberTextField lambda;
    private RadioButton H1;
    private RadioButton H2;
    private RadioButton generadorRand;
    private RadioButton generadorPseudoRand;
    private RadioButton operadoresv1;
    private RadioButton operadoresv2;
    private Button ejecutar;
    private Button reiniciar;
    private Button nuevaSol;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Bicing");
        stage.setMinHeight(600);
        stage.setMinWidth(800);

        Label nEst = new Label("  N. Est: ");
        nEstText = new NumberTextField();
        nEstText.setMaxWidth(75);
        Label nFurg = new Label("  N. Furg: ");
        nFurgText = new NumberTextField();
        nFurgText.setMaxWidth(75);
        Label nBicis = new Label("  N. Bicis: ");
        nBicisText = new NumberTextField();
        nBicisText.setMaxWidth(75);
        Label seed = new Label("  Seed: ");
        ToggleGroup seedGroup = new ToggleGroup();
        randomSeed = new RadioButton("Random");
        randomSeed.setToggleGroup(seedGroup);
        randomSeed.setSelected(true);
        customSeed = new RadioButton("Custom seed");
        customSeed.setToggleGroup(seedGroup);
        HBox parametresBicis = new HBox();
        customSeedField = new NumberTextField();
        customSeedField.setDisable(true);
        customSeedField.setMaxWidth(75);
        customSeed.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue)
                customSeedField.setDisable(false);
            else customSeedField.setDisable(true);
        });
        parametresBicis.getChildren().addAll(
                nEst, nEstText, nFurg, nFurgText, nBicis, nBicisText, seed, randomSeed, customSeed, customSeedField);
        parametresBicis.setAlignment(Pos.TOP_CENTER);

        canvas = new GridCiudad(400, 400, 150);
        HBox canvasLog = new HBox();
        canvasLog.getChildren().addAll(canvas);
        canvasLog.setAlignment(Pos.CENTER);

        progressBar = new ProgressBar(-1.0D);
        progressBar.setMinWidth(600);
        HBox progress = new HBox();
        progress.getChildren().add(progressBar);
        progress.setAlignment(Pos.CENTER);

        Label algoritmo = new Label("Algoritmo: ");
        ToggleGroup algoGroup = new ToggleGroup();
        hillClimb = new RadioButton("Hill Climbing");
        hillClimb.setToggleGroup(algoGroup);
        hillClimb.setSelected(true);
        annealing = new RadioButton("Annealing");
        annealing.setToggleGroup(algoGroup);
        numIt = new NumberTextField();
        numIt.setPromptText("Num it");
        numIt.setDisable(true);
        numIt.setMaxWidth(100);
        k = new NumberTextField();
        k.setPromptText("K");
        k.setDisable(true);
        k.setMaxWidth(100);
        lambda = new NumberTextField();
        lambda.setPromptText("lambda");
        lambda.setDisable(true);
        lambda.setMaxWidth(100);
        annealing.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                numIt.setDisable(false);
                k.setDisable(false);
                lambda.setDisable(false);
            } else {
                numIt.setDisable(true);
                k.setDisable(true);
                lambda.setDisable(true);
            }
        });
        HBox boxAlgo = new HBox();
        boxAlgo.setSpacing(5);
        boxAlgo.setAlignment(Pos.CENTER);
        boxAlgo.getChildren().addAll(algoritmo, hillClimb, annealing, numIt, k, lambda);

        Label heuristicos = new Label("Heuristicos: ");
        ToggleGroup groupHeur = new ToggleGroup();
        H1 = new RadioButton("H1");
        H1.setSelected(true);
        H1.setToggleGroup(groupHeur);
        H2 = new RadioButton("H2");
        H2.setToggleGroup(groupHeur);
        Label generador = new Label("Generador: ");
        ToggleGroup generadorGroup = new ToggleGroup();
        generadorRand = new RadioButton("Random");
        generadorRand.setToggleGroup(generadorGroup);
        generadorRand.setSelected(true);
        generadorPseudoRand = new RadioButton("Pseudo-rand");
        generadorPseudoRand.setToggleGroup(generadorGroup);
        Label operadores = new Label("Operadores");
        ToggleGroup opGroup = new ToggleGroup();
        operadoresv1 = new RadioButton("Version 1");
        operadoresv1.setToggleGroup(opGroup);
        operadoresv1.setSelected(true);
        operadoresv2 = new RadioButton("Version 2");
        operadoresv2.setToggleGroup(opGroup);
        HBox boxHeur = new HBox();
        boxHeur.getChildren().addAll(
                heuristicos, H1, H2, generador, generadorRand, generadorPseudoRand, operadores, operadoresv1, operadoresv2);
        boxHeur.setSpacing(5);
        boxHeur.setAlignment(Pos.CENTER);

        ejecutar = new Button("Ejecutar");
        ejecutar.setOnAction(event -> {
            ejecutarAlgorismo();
        });
        reiniciar = new Button("Reiniciar");
        nuevaSol = new Button("Nueva solucion");
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(ejecutar, reiniciar, nuevaSol);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.getChildren().addAll(parametresBicis, canvasLog, progress, boxAlgo, boxHeur, buttonBox);
        root.setSpacing(20);
        root.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(root,800,800);

        stage.setScene(scene);

        stage.show();
    }

    private void ejecutarAlgorismo() {
        int nEst = Integer.parseInt(nEstText.getText());
        int nFurg = Integer.parseInt(nFurgText.getText());
        int nBicis = Integer.parseInt(nBicisText.getText());
        long seed;
        if (randomSeed.isSelected())
            seed = System.currentTimeMillis();
        else seed = (long) Integer.parseInt(customSeedField.getText());
        Estaciones estaciones = new Estaciones(nEst, nBicis, 0, (int)seed);
        Estado estadoInicial;
        if (generadorRand.isSelected()) {
            estadoInicial = new Estado(1, nFurg, estaciones, seed);
        } else {
            estadoInicial = new Estado(2, nFurg, estaciones, seed);
        }
        Problem problem;
        Search search;
        if (hillClimb.isSelected()) {
            search = new HillClimbingSearch();
            if (H1.isSelected()) {
                problem = new Problem(
                        estadoInicial, new FuncionSucesoraHillClimbing2(), new GoalTest(), new FuncionHeuristica());
            } else {    //H2
                problem = new Problem(
                        estadoInicial, new FuncionSucesoraHillClimbing2(), new GoalTest(), new FuncionHeuristicaC1());
            }
        } else {    //annealing
            search = new SimulatedAnnealingSearch();
            if (H1.isSelected()) {
                problem = new Problem(
                        estadoInicial, new FuncionSucesoraSA(), new GoalTest(), new FuncionHeuristica());
            } else {    //H2
                problem = new Problem(
                        estadoInicial, new FuncionSucesoraSA(), new GoalTest(), new FuncionHeuristicaC1());
            }
        }
        try {
            SearchAgent agent = new SearchAgent(problem, search);
            System.out.print("Acciones\n");
            printActions(agent.getActions());
            System.out.print("Instrumentación\n");
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void printActions(List actions) {
        for (Object action1 : actions) {
            String action = (String) action1;
            System.out.println(action);
        }

    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    public static void main(String[] args) {
        /*Dominio.ControladorExperimentos c = new Dominio.ControladorExperimentos();
        c.lanzadorExperimentos();*/
        launch(args);

    }
}
