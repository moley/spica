package org.spica.devclient.actions.widgets;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class WidgetsApplicationInternal extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();

        List<String> items = new ArrayList<>();
        items.add("Meyer, Marc");
        items.add("Sparrow, Jack");
        items.add("Walkes, Otto");


        ComboBox<String> combobox = new ComboBox<>();
        combobox.setItems(FXCollections.observableArrayList(items));
        ComboBoxAutoComplete<String> comboBoxAutoComplete = new ComboBoxAutoComplete<String>(combobox);
        gridPane.add(combobox, 0, 1);


        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(1000);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
