package org.spica.devclient.ui.widgets;


import org.spica.javaclient.actions.Action;

public class ActionSearchableItem implements SearchableItem {

    private Action action;

    public ActionSearchableItem(final Action action) {
        this.action = action;
    }

    @Override
    public String getSearchKey() {
        return ":" + action.getGroup().getShortkey() + " " + action.getCommand().getShortkey();
    }

    @Override
    public String getDisplayname() {
        return action.getGroup().getShortkey() + " " + action.getCommand().getShortkey() + " - " + action.getDisplayname();}

    @Override
    public String getIcon() {
        return "fa-cogs";
    }

    public Action getAction () {
        return action;
    }
}
