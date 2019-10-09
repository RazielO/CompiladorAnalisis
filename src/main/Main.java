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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        Tag tags = new Tag();

        FileReader fileReader = new FileReader();
        fileReader.setFilename(System.getProperty("user.dir") + "/src/main/code.txt");

        PrintErrors printErrors = new PrintErrors();

        try
        {
            Lexer lexer = new Lexer(fileReader.read().trim());

            List<String> tokenList = new ArrayList<>();
            tokenList.add("TOKEN");
            List<String> tagList = new ArrayList<>();
            tagList.add("TAG");
            List<String> lexemeList = new ArrayList<>();
            lexemeList.add("LEXEME");

            while (!lexer.isEndOfCode())
            {
                int token = lexer.scanNextToken();

                if (token != -1) // No es comentario
                {
                    tokenList.add(String.valueOf(token));
                    tagList.add(String.valueOf(tags.get(token)));
                    lexemeList.add(lexer.getLexeme());
                }
            }

            printErrors.setErrors(lexer.getErrorStack());
            printErrors.showErrors();

            System.out.println("\n");

            System.out.println("TABLA DE LEXEMAS");
            List<List<String>> lists = new ArrayList<>();
            lists.add(tokenList);
            lists.add(tagList);
            lists.add(lexemeList);
            PrintTable tokens = new PrintTable(lists);
            tokens.print();

            System.out.println("\n");
            System.out.println("TABLA DE SIMBOLOS");
            PrintTable printTable = new PrintTable(lexer.getSymbols());
            printTable.print();

            System.exit(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
