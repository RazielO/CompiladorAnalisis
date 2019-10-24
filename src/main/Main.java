package main;

import exceptions.PrintErrors;
import files.FileReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lexer.Lexer;
import lexer.Tag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.*;
import pda.Control;
import pda.PushDownAutomaton;
import pda.Rules;

public class Main extends Application
{
    /**
     * Muestra el FXML deseado en el primaryStage
     *
     * @param primaryStage donde se mostrará el FXML
     * @throws Exception no se encontró el FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
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
        //launch(args);

        FileReader reader = new FileReader();
        try
        {
            reader.setFilename(System.getProperty("user.dir") + "/src/pda/files/rules.txt");
            String rulesReading = reader.read();
            reader.setFilename(System.getProperty("user.dir") + "/src/pda/files/info.txt");
            String infoReading = reader.read();

            PushDownAutomaton pda = new PushDownAutomaton(infoReading, rulesReading);
            String string= "abbbba";
            System.out.printf("The string '%s' is valid: %s", string, pda.validString(string));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
