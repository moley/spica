package org.spica.server.user.api;

import org.spica.server.user.domain.User;
import org.spica.server.user.model.UserInfo;

public class UserMapper {

    UserInfo toApi (User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setFirstname(user.getFirstname());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setId(user.getId());

        return userInfo;
    }
}
