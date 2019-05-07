package org.spica.devclient.ui.widgets;

import javafx.collections.ObservableList;

import java.util.List;

public class SearchStrategy<T> {

    private final String searchKey;

    private final List<T> allItems;

    private final String name;

    public SearchStrategy(String searchKey, ObservableList<T> allItems, String name) {
        this.searchKey = searchKey;
        this.allItems = allItems;
        this.name = name;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public String getSearchKeyUpper () {
        return searchKey.toUpperCase();
    }

    public List<T> getAllItems() {
        return allItems;
    }

    public String getName() {
        return name;
    }
}
