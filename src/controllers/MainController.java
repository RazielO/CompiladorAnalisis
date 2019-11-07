package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
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
    TableColumn colId, colLine, colType;
    @FXML
    MenuItem fileOpen, fileSave, infoLexical, infoSyntactic, infoSemantic;

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
        lblFilename.setText("Not selected");
        txtErrors.setPrefWidth(maxWidth * 0.90);

        symbolTableScrollPane.setPrefWidth((maxWidth / 2) - (maxWidth * 0.05));
        symbolTableScrollPane.setPrefHeight(maxHeight * 0.65);
    }

    private void configureAnalysisButtons()
    {
        btnLexical.setOnMouseClicked(e -> {
            try
            {
                Lexer lexer = new Lexer(getCode());
                String result = "Lexical analysis: " + lexer.getResult();
                txtErrors.setText(result);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });
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

        fileOpen.setOnAction(e -> openFile());
        fileSave.setOnAction(e -> saveFile());
        infoLexical.setOnAction(e -> openPdf("Lexer.pdf"));
        infoSyntactic.setOnAction(e -> openPdf("Syntactic.pdf"));
    }

    private void configureCodeArea(double maxWidth, double maxHeight)
    {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));


        codeScrollPane.setPrefWidth((maxWidth / 2) - (maxWidth * 0.05));
        codeScrollPane.setPrefHeight(maxHeight * 0.65);

        InputMap<KeyEvent> im = InputMap.consume(
                EventPattern.keyPressed(KeyCode.TAB),
                e -> codeArea.replaceSelection("    ")
        );
        Nodes.addInputMap(codeArea, im);

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

    private void openPdf(String name)
    {
        try
        {
            String path = System.getProperty("user.dir") + "/assets/" + name;
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().open(new File(path));
        }
        catch (IOException e)
        {
            alertMessage(e.getMessage(), "Error", Alert.AlertType.ERROR, "There was a problem");
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
}
