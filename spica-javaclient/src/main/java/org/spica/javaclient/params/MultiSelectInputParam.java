package org.spica.javaclient.params;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MultiSelectInputParam<T> extends AbstractInputParam<T> {

    private List<T> items;

    private HashMap<String, T> itemsStringMap = new HashMap<String, T>();

    private T selected;

    public MultiSelectInputParam(final String key, final String displayname, final List<T> items, Renderer<T> renderer) {
        this.setKey(key);
        this.setDisplayname(displayname);
        this.setItems(items);
        this.setRenderer(renderer);

        for (T next : items) {
            String nextAsString = renderer.toString(next);
            itemsStringMap.put(nextAsString, next);
        }
    }

    public Collection<String> getItems() {
        return itemsStringMap.keySet();
    }

    public HashMap<String, T> getItemsStringMap () {
        return itemsStringMap;
    }

    public String getValueOf (final String key) {
        T value = itemsStringMap.get(key);
        return value != null ? value.toString(): null;
    }

    public void setValue(T value) {
        this.selected = value;
        T selected = itemsStringMap.get(value.toString().strip());
        super.setValue(selected);
    }
}
