package controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.util.Objects;

public class AnalysisInfoController
{
    @FXML
    WebView content;

    static String analysis;

    @FXML
    public void initialize()
    {
        content.getEngine().load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/assets/" + analysis + ".html")).toExternalForm());
        content.getEngine().setUserStyleSheetLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("views/assets/markdown.css")).toString());
    }
}
