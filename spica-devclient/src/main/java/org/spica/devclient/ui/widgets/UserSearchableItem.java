package org.spica.devclient.ui.widgets;

import org.spica.javaclient.model.UserInfo;

public class UserSearchableItem implements SearchableItem {

    private UserInfo userInfo;

    public UserSearchableItem (final UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    @Override
    public String getSearchKey() {
        return userInfo.getName() + ", " + userInfo.getFirstname();
    }

    @Override
    public String getDisplayname() {
        return userInfo.getName() + ", " + userInfo.getFirstname();
    }

    @Override
    public String getIcon() {
        return "fa-user";
    }
}
