package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application
{
    public static Stage stage;

    /**
     * Muestra el FXML deseado en el primaryStage
     *
     * @param primaryStage donde se mostrará el FXML
     * @throws Exception no se encontró el FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Main.stage = primaryStage;


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/main.fxml")));

        Scene scene = new Scene(root, 1920, 1080);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("controllers/keywords.css")).toExternalForm());

        primaryStage.setTitle("Fase de análisis de un compilador");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Método principal del programa. Llama al analizador léxico. Imprime la tabla de símbolos generada y
     * los errores encontrados
     *
     * @param args argumentos pasados al programa
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
