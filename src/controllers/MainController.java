package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import main.Main;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

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


        configureCodeArea(maxWidth, maxHeight);
        configureMenu(maxWidth, maxHeight);
        lblFilename.setText("Not selected");

        mainContainer.setFillWidth(true);
        mainContainer.setPadding(new Insets(maxHeight * 0.05, maxWidth * 0.05, maxWidth * 0.05, maxHeight * 0.05));
        mainContainer.setOnKeyPressed(e ->
        {
            if (e.isControlDown() && e.getCode() == KeyCode.S)
                saveFile();
            else if (e.isControlDown() && e.getCode() == KeyCode.O)
                openFile();
        });

        txtErrors.setPrefWidth(maxWidth * 0.90);


        symbolTableScrollPane.setPrefWidth((maxWidth / 2) - (maxWidth * 0.05));
        symbolTableScrollPane.setPrefHeight(maxHeight * 0.65);
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
}
