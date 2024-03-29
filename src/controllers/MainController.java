package controllers;

import exceptions.Error;
import exceptions.PrintErrors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lexer.Lexer;
import main.Main;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import semantic.Semantic;
import syntactic.Symbol;
import syntactic.Syntactic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController
{
    @FXML
    Label lblFilename;
    @FXML
    VBox mainContainer;
    @FXML
    HBox codeContainer;
    @FXML
    VirtualizedScrollPane codeScrollPane;
    @FXML
    TextArea txtErrors;
    @FXML
    CodeArea codeArea;
    @FXML
    ScrollPane symbolTableScrollPane;
    @FXML
    Button btnStart, btnLexical, btnSyntactic, btnSemantic;
    @FXML
    TableView symbolTable;
    @FXML
    TableColumn colId, colLine, colType, colValue;
    @FXML
    MenuItem fileOpen, fileSave, infoLexical, infoSyntactic, infoSemantic, infoGrammar, infoGeneral;

    private String filename;

    @FXML
    public void initialize()
    {
        double maxWidth = Screen.getPrimary().getBounds().getWidth();
        double maxHeight = Screen.getPrimary().getBounds().getHeight();
        this.filename = "";

        configureAnalysisButtons();
        configureCodeArea(maxWidth, maxHeight);
        configureMenu(maxWidth, maxHeight);
        configureMainContainer(maxWidth, maxHeight);
        configureTable();
        lblFilename.setText("Not selected");
        txtErrors.setPrefWidth(maxWidth * 0.90);

        symbolTableScrollPane.setPrefWidth((maxWidth / 2) - (maxWidth * 0.05));
        symbolTableScrollPane.setPrefHeight(maxHeight * 0.65);
    }

    private void configureTable()
    {
        colId.setCellValueFactory(new PropertyValueFactory<Symbol, String>("id"));
        colLine.setCellValueFactory(new PropertyValueFactory<Symbol, List<Integer>>("lines"));
        colType.setCellValueFactory(new PropertyValueFactory<Symbol, String>("type"));
        colValue.setCellValueFactory(new PropertyValueFactory<Symbol, String>("value"));
    }

    private ObservableList<Symbol> getObservableList(List<Symbol> symbols)
    {
        return FXCollections.observableArrayList(new ArrayList<>(symbols));
    }

    private void fillTable(List<Symbol> symbols)
    {
        symbolTable.setItems(getObservableList(symbols));
    }

    private void configureAnalysisButtons()
    {
        btnLexical.setOnMouseClicked(e ->
        {
            try
            {
                Lexer lexer = new Lexer(getCode());
                StringBuilder result = new StringBuilder("Lexical analysis: " + lexer.getResult());

                PrintErrors printErrors = new PrintErrors(lexer.getErrorStack());
                List<String> lexicalErrors = printErrors.showErrors();

                boolean correct = lexicalErrors.size() == 0;

                if (!correct)
                {
                    btnLexical.setStyle("-fx-background-color: #ee5253;");
                    btnSyntactic.setDisable(true);
                }
                else
                {
                    btnSyntactic.setDisable(false);
                    btnLexical.setStyle("-fx-background-color: #10ac84;");
                }
                btnSemantic.setDisable(true);

                result.append("\n\n").append("ERRORS:").append("\n");
                for (String error : lexicalErrors)
                    result.append("  ").append(error).append("\n");
                txtErrors.setText(result.toString());
                Lexer.line = 1;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });

        btnSyntactic.setOnMouseClicked(e ->
        {
            try
            {
                Lexer lexer = new Lexer(getCode());
                lexer.getResult();
                Syntactic syntactic = new Syntactic();
                LinkedList<lexer.Token> tokens = lexer.getSymbols();
                tokens.add(0, new lexer.Token(-1, "lambda", "λ", 0));

                syntactic.validString(tokens);
                Stack<Error> syntacticErrors = syntactic.getErrorStack();
                boolean syntacticResult = syntacticErrors.empty();

                PrintErrors printErrors = new PrintErrors(syntacticErrors);
                List<String> errors = printErrors.showErrors();

                boolean correct = syntacticErrors.size() != 0;
                if (!syntacticResult)
                {
                    btnSyntactic.setStyle("-fx-background-color: #ee5253;");
                    btnSemantic.setDisable(true);
                }
                else
                {
                    btnSemantic.setDisable(false);
                    btnSyntactic.setStyle("-fx-background-color: #10ac84;");
                }

                StringBuilder result = new StringBuilder();
                result.append(syntacticResult);
                result.append("\n\n").append("ERRORS:").append("\n");
                for (String error : errors)
                    result.append("  ").append(error).append("\n");

                txtErrors.setText(result.toString());
                fillTable(syntactic.getSymbolsTable());
                Lexer.line = 1;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });

        btnSemantic.setOnMouseClicked(e ->
        {
            try
            {
                Lexer lexer = new Lexer(getCode());
                lexer.getResult();
                Syntactic syntactic = new Syntactic();
                LinkedList<lexer.Token> tokens = lexer.getSymbols();
                tokens.add(0, new lexer.Token(-1, "lambda", "λ", 0));

                syntactic.validString(tokens);

                Semantic semantic = new Semantic(tokens, syntactic.getSymbolsTable());
                semantic.checkSemantics();
                Stack<Error> errors = semantic.getErrorStack();
                PrintErrors printErrors = new PrintErrors();
                printErrors.setErrors(errors);
                boolean semanticResult = errors.size() == 0;

                if (!semanticResult)
                    btnSemantic.setStyle("-fx-background-color: #ee5253;");
                else
                    btnSemantic.setStyle("-fx-background-color: #10ac84;");

                StringBuilder result = new StringBuilder();
                result.append(semanticResult);
                result.append("\n\n").append("ERRORS:").append("\n");
                for (String error : printErrors.showErrors())
                    result.append("  ").append(error).append("\n");

                txtErrors.setText(result.toString());
                fillTable(syntactic.getSymbolsTable());
                Lexer.line = 1;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });

        btnStart.setOnMouseClicked(e ->
        {
            try
            {
                Lexer lexer = new Lexer(getCode());
                lexer.getResult();
                Syntactic syntactic = new Syntactic();
                LinkedList<lexer.Token> tokens = lexer.getSymbols();
                tokens.add(0, new lexer.Token(-1, "lambda", "λ", 0));

                Stack<Error> errors = lexer.getErrorStack();

                syntactic.validString(tokens);
                errors.addAll(syntactic.getErrorStack());

                Semantic semantic = new Semantic(tokens, syntactic.getSymbolsTable());
                semantic.checkSemantics();
                errors.addAll(semantic.getErrorStack());
                PrintErrors printErrors = new PrintErrors();
                printErrors.setErrors(errors);
                boolean result = errors.size() == 0;

                if (result)
                    btnStart.setStyle("-fx-background-color: #10ac84;");
                else
                    btnStart.setStyle("-fx-background-color: #ee5253;");

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(stringBuilder);
                stringBuilder.append("\n\n").append("ERRORS:").append("\n");
                for (String error : printErrors.showErrors())
                    stringBuilder.append("  ").append(error).append("\n");

                txtErrors.setText(stringBuilder.toString());
                fillTable(syntactic.getSymbolsTable());
                Lexer.line = 1;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });

        btnStart.setStyle("-fx-background-color: #ff9f43;");
        btnLexical.setStyle("-fx-background-color: #ff9f43;");
        btnSemantic.setStyle("-fx-background-color: #ff9f43;");
        btnSyntactic.setStyle("-fx-background-color: #ff9f43;");
    }

    private void configureMainContainer(double maxWidth, double maxHeight)
    {
        mainContainer.setFillWidth(true);
        mainContainer.setPadding(new Insets(maxHeight * 0.05, maxWidth * 0.05, maxWidth * 0.05, maxHeight * 0.05));
        mainContainer.setOnKeyPressed(e ->
        {
            if (e.isControlDown() && e.getCode() == KeyCode.S)
                saveFile();
            else if (e.isControlDown() && e.getCode() == KeyCode.O)
                openFile();
        });
    }

    private void configureMenu(double maxWidth, double maxHeight)
    {
        HBox menuBar = (HBox) this.mainContainer.getChildren().get(0);
        menuBar.setSpacing(maxWidth / 10);
        menuBar.setPrefWidth(maxWidth);
        menuBar.setPrefHeight(maxHeight * 0.10);

        fileOpen.setOnAction(e ->
        {
            btnSyntactic.setDisable(true);
            btnSemantic.setDisable(true);
            openFile();
        });
        fileSave.setOnAction(e ->
        {
            btnSyntactic.setDisable(true);
            btnSemantic.setDisable(true);
            saveFile();
        });
        infoLexical.setOnAction(e -> openAnalysisInfo("lexico"));
        infoSyntactic.setOnAction(e -> openAnalysisInfo("sintactico"));
        infoGrammar.setOnAction(e -> openAnalysisInfo("grammar"));
        infoSemantic.setOnAction(e -> openAnalysisInfo("semantico"));
        infoGeneral.setOnAction(e -> openAnalysisInfo("general"));
    }

    private void configureCodeArea(double maxWidth, double maxHeight)
    {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setOnKeyTyped(e -> modifyButtonsOnCodeAreaKey());

        codeArea.setOnKeyPressed(e -> modifyButtonsOnCodeAreaKey());

        codeScrollPane.setPrefWidth((maxWidth / 2) - (maxWidth * 0.05));
        codeScrollPane.setPrefHeight(maxHeight * 0.65);

        InputMap<KeyEvent> tabInputMap = InputMap.consume(
                EventPattern.keyPressed(KeyCode.TAB),
                e -> codeArea.replaceSelection("    ")
        );

        Nodes.addInputMap(codeArea, tabInputMap);

        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(100))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(getCode())));

        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, e ->
        {
            if (e.getCode() == KeyCode.ENTER)
            {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
            }
        });
    }

    private void modifyButtonsOnCodeAreaKey()
    {
        btnSemantic.setDisable(true);
        btnSyntactic.setDisable(true);
        btnStart.setStyle("-fx-background-color: #ff9f43;");
        btnLexical.setStyle("-fx-background-color: #ff9f43;");
        btnSemantic.setStyle("-fx-background-color: #ff9f43;");
        btnSyntactic.setStyle("-fx-background-color: #ff9f43;");
    }

    private void setCode(String code)
    {
        codeArea.replaceText(code);
    }

    private String getCode()
    {
        return codeArea.getText();
    }

    private void alertMessage(String message, String title, Alert.AlertType type, String header)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    private void saveFile()
    {
        try
        {
            if (this.filename.length() == 0)
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Source Code Files (*.my)", "*.my")
                );
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                File result = fileChooser.showSaveDialog(Main.stage);
                this.filename = result.getAbsolutePath();
                lblFilename.setText(this.filename);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
            writer.write(getCode());
            writer.close();
            alertMessage(String.format("Saved to file: %s", this.filename), "Saved", Alert.AlertType.INFORMATION, "Saved");
        }
        catch (IOException e)
        {
            alertMessage(e.getMessage(), "Error", Alert.AlertType.ERROR, "There was a problem");
        }
    }

    private void openFile()
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Source Code Files (*.my)", "*.my")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File result = fileChooser.showOpenDialog(Main.stage);
            this.filename = result.getAbsolutePath();
            Scanner scanner = new Scanner(result);
            scanner.useDelimiter("\\Z");
            setCode(scanner.next());

            lblFilename.setText(this.filename);
        }
        catch (IOException e)
        {
            alertMessage(e.getMessage(), "Error", Alert.AlertType.ERROR, "There was a problem");
        }
        catch (NullPointerException ignored)
        {
        }
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text)
    {
        String[] KEYWORDS = new String[]{
                "declare", "begin", "print", "read", "if", "else",
                "while", "do", "or", "and", "true", "false", "break",
                "int", "float", "boolean", "char"
        };

        String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        String PAREN_PATTERN = "[()]";
        String BRACE_PATTERN = "[{}]";
        String BRACKET_PATTERN = "[\\[]]";
        String SEMICOLON_PATTERN = ";";
        String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        Pattern PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );

        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find())
        {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private void openAnalysisInfo(String analysis)
    {
        AnalysisInfoController.analysis = analysis;
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/analysisInfo.fxml")));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("controllers/keywords.css")).toExternalForm());

            stage.setTitle(analysis.toUpperCase());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }
}
