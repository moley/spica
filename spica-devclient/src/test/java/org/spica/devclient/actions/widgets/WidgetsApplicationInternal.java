package org.spica.devclient.actions.widgets;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.spica.devclient.ui.widgets.ActionSearchableItem;
import org.spica.devclient.ui.widgets.SearchableItem;
import org.spica.devclient.ui.widgets.SearchableTextField;
import org.spica.devclient.ui.widgets.UserSearchableItem;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.model.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WidgetsApplicationInternal extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();

        TextField txtUser = new TextField();

        UserSearchableItem user1 = new UserSearchableItem(new UserInfo().name("Meyer").firstname("Marc"));
        UserSearchableItem user2 = new UserSearchableItem(new UserInfo().name("Sparrow").firstname("Jack"));
        UserSearchableItem user3 = new UserSearchableItem(new UserInfo().name("Walkes").firstname("Otto"));

        List<SearchableItem> searchableUsers = Arrays.asList(user1, user2, user3);

        //TODO Content providers
        List<SearchableItem> searchableActions = new ArrayList<SearchableItem>();
        ActionHandler actionHandler = new ActionHandler();
        for (Action nextAction : actionHandler.getRegisteredActions()) {
            searchableActions.add(new ActionSearchableItem(nextAction));
        }

        txtUser.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    System.out.println("Enter on textfield" + event.getSource());


                }
            }
        });
        SearchableTextField searchableTextFieldUser = new SearchableTextField(txtUser);
        searchableTextFieldUser.addSearchStrategy(":", searchableActions, "Commands");
        searchableTextFieldUser.addSearchStrategy("", searchableUsers, "Search");


        gridPane.add(txtUser, 0, 0);

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
