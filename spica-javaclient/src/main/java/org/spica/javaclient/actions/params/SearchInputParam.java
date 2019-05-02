package org.spica.javaclient.actions.params;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Data
public class SearchInputParam<T> extends AbstractInputParam {

    private List<T> items;

    private HashMap<String, T> itemsStringMap = new HashMap<String, T>();

    private String selected;

    public SearchInputParam(final String key, final String displayname, final List<T> items, Renderer<T> renderer) {
        this.setKey(key);
        this.setDisplayname(displayname);
        this.setItems(items);
        this.setRenderer(renderer);

        for (T next: items) {
            String nextAsString = renderer.toString(next);
            itemsStringMap.put(nextAsString, next);
        }
    }

    public Collection<String> getItems () {
        return itemsStringMap.keySet();
    }

    public void setSelected (final String selected) {
        super.setValue(itemsStringMap.get(selected));
        this.selected = selected;
    }

    public T getSelected () {
        return itemsStringMap.get(selected);
    }

    public String getSelectedAsString () {
        return selected;
    }
}
