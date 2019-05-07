package org.spica.devclient.ui.widgets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
import org.spica.devclient.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchableTextField {

    private ObservableList<SearchableItem> filteredItems;

    private SearchStrategy currentSearchStrategy;

    private TextField textField;

    private ListView<SearchableItem> listView;

    private List<SearchStrategy<SearchableItem>> searchStrategies = new ArrayList<>();

    private Popup popupWindow = new Popup();

    public SearchableTextField(final TextField textField) {
        listView = new ListView<SearchableItem>();
        listView.setVisible(false);

        this.textField = textField;
        popupWindow.getContent().add(listView);
        popupWindow.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                listView.getSelectionModel().selectFirst();

            }
        });

        adaptSearchList("");
        this.textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(oldValue + "->" + newValue);
                adaptSearchList(newValue);


            }
        });
        this.textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println("textField key " + event.getCode().getName());
                if (event.getCode() == KeyCode.DOWN && !popupWindow.isShowing()) {
                    popupWindow.show(textField.getScene().getWindow());
                    listView.requestFocus();
                    event.consume();


                }

            }
        });

        this.listView.setCellFactory(listView -> new ListCell<SearchableItem>() {

            @Override
            protected void updateItem(SearchableItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getDisplayname());
                    setGraphic(UiUtils.getIcon(item.getIcon()));
                }
            }
        });

        this.listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    event.consume();
                    textField.requestFocus();
                    popupWindow.hide();
                }

                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("Enter on ListView " + event.getSource());
                    event.consume();
                    textField.setText(listView.getSelectionModel().getSelectedItem().getSearchKey());
                    textField.positionCaret(textField.getText().length() + 1);
                    popupWindow.hide();
                }
            }
        });
    }

    private void adaptSearchList(final String newValue) {
        if (newValue.trim().isEmpty())
            listView.setVisible(false);
        else {

            for (SearchStrategy<SearchableItem> searchStrategy : searchStrategies) {
                if (newValue.toUpperCase().startsWith(searchStrategy.getSearchKeyUpper()) || searchStrategy.getSearchKeyUpper().trim().isEmpty()) {
                    List<SearchableItem> filtered = new ArrayList<SearchableItem>();
                    for (SearchableItem next : searchStrategy.getAllItems()) {
                        if (next.getSearchKey().toUpperCase().contains(newValue.toUpperCase().substring(searchStrategy.getSearchKeyUpper().length()))) {
                            filtered.add(next);
                        }
                    }
                    filteredItems = FXCollections.observableList(filtered);

                    listView.setItems(filteredItems);
                    listView.setVisible(true);
                    listView.setPrefWidth(textField.getWidth());
                    listView.setPrefHeight(300);


                    Bounds textFieldBoundsOnScreen = UiUtils.getBounds(textField);
                    popupWindow.setX(textFieldBoundsOnScreen.getMinX());
                    popupWindow.setY(textFieldBoundsOnScreen.getMinY() + textFieldBoundsOnScreen.getHeight());

                    currentSearchStrategy = searchStrategy;
                    popupWindow.show(textField.getScene().getWindow());

                    break;
                }

            }
        }

    }

    public void addSearchStrategy(String searchKey, final List<SearchableItem> observableArrayList, final String name) {
        searchStrategies.add(new SearchStrategy(searchKey, FXCollections.observableList(observableArrayList), name));
    }

    public SearchableItem getSelectedSearchableItem () {
        return listView.getSelectionModel().getSelectedItem();
    }

    public Popup getPopup () {
        return popupWindow;
    }

}
