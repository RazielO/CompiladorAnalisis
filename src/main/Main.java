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
            reader.setFilename(System.getProperty("user.dir") + "/src/main/code.txt");
            StringBuilder code = new StringBuilder(reader.read());
            Lexer lexer = new Lexer(code.toString());

            code = new StringBuilder();
            code.append("λ ");
            while (!lexer.isEndOfCode())
            {
                int reading = lexer.scanNextToken();
                if (reading != -1)
                    code.append(reading).append(" ");
            }

            String lexerResult = code.toString().trim();
            System.out.printf("Lexer result: '%s'\n", lexerResult);

            // Parser
            reader.setFilename(System.getProperty("user.dir") + "/src/pda/files/info.txt");
            String info = reader.read();
            reader.setFilename(System.getProperty("user.dir") + "/src/pda/files/rules.txt");
            String rules = reader.read();
            PushDownAutomaton pda = new PushDownAutomaton(info, rules);

            boolean parserResult = pda.validString(lexerResult);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
