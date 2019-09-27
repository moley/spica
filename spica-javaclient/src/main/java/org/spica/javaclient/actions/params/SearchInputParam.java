package org.spica.javaclient.actions.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class SearchInputParam<T> extends AbstractInputParam<T> {

    private List<T> items;

    private HashMap<String, T> itemsStringMap = new HashMap<String, T>();

    private T selected;

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

    public void setValue(T value) {

        if (! items.isEmpty()) {
            this.selected = value;
            T selected = itemsStringMap.get(value.toString().strip());
            super.setValue(selected);
        }
    }

}
