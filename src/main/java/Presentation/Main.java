package Presentation;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;

public class Main extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("HGraph v1.0b");
        scene = new Scene(new Browser(stage),900,600, Color.web("#666970"));
        stage.setScene(scene);
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
class Browser extends Region {

    final WebView browser = new WebView();

    final WebEngine webEngine = browser.getEngine();
    public Browser(Stage stage) {
        //apply the styles
        getStyleClass().add("browser");


        // load the web page
        URL url = getClass().getResource("www/index.html");
        webEngine.load(url.toExternalForm());
        Font.loadFont(getClass().getResource("www/fonts/ionicons.ttf").toExternalForm(),10);

        browser.setContextMenuEnabled(false);

        webEngine.getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {

                        JSObject jsobj = (JSObject) webEngine.executeScript("app");
                        jsobj.setMember("HGraph", new PresentationController(webEngine, stage));
                    }
                });

        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {

            @Override
            public void handle(WebEvent<String> arg0) {
                System.out.println("alert");
            }
        });

        //add the web view to the scene
        getChildren().add(browser);

    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 900;
    }

    @Override protected double computePrefHeight(double width) {
        return 600;
    }
}