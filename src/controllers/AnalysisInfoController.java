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
//        FileReader reader = new FileReader();
//        reader.setFilename(String.format(System.getProperty("user.dir") + "/assets/%s.html", analysis));
        content.getEngine().load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/assets/" + analysis + ".html")).toExternalForm());
        content.getEngine().setUserStyleSheetLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("views/assets/markdown.css")).toString());
    }
}
