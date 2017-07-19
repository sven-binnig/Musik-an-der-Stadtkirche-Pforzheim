/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author svenina
 */
public class AboutBox extends BorderPane {

    private final String title;
    private final String version;
    private final Stage parent;

    public AboutBox(Stage parent, String title, String version) {
        this.title = title;
        this.version = version;
        this.parent = parent;
        this.initUI();
    }

    private void initUI() {
        Scene scene = new Scene(this);
        parent.setScene(scene);
        
        this.setPadding(new Insets(5, 5, 5, 5));

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("musik_stadtkirche.jpg")));
        this.setTop(imageView);
        
        //this.prefWidthProperty().bind(imageView.fitWidthProperty());
        parent.setWidth(449);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(5, 5, 5, 5));
        vbox.getChildren().addAll(new Label(title), new Label("Version " + version));
        this.setCenter(vbox);

        Button button = new Button("Weiter");
        VBox vbUnten = new VBox();
        vbUnten.setPadding(new Insets(5, 5, 5, 5));
        vbUnten.getChildren().addAll(new Separator(), button);

        button.setOnAction((actionEvent) -> {
            parent.close();
        });
        this.setBottom(vbUnten);
    }

}
