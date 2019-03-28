package org.spica.server.user.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.server.user.UserRoleName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

public class AuthoritiesConverterTest {

    private String roles = String.join(",", Arrays.asList(UserRoleName.ROLE_ADMIN.name(), UserRoleName.ROLE_USER.name()));

    @Test
    public void fromDatabase () {
        AuthoritiesConverter authoritiesConverter = new AuthoritiesConverter();
        List<GrantedAuthority> authorities = authoritiesConverter.convertToEntityAttribute(roles);
        Assert.assertEquals("Role1 invalid", UserRoleName.ROLE_ADMIN.name(), authorities.get(0).getAuthority());
        Assert.assertEquals("Role2 invalid", UserRoleName.ROLE_USER.name(), authorities.get(1).getAuthority());
        List<GrantedAuthority> authoritiesEmpty = authoritiesConverter.convertToEntityAttribute("");
        Assert.assertTrue ("Number of authorities in empty string invalid",authoritiesEmpty.isEmpty());

    }

    @Test
    public void toDatabase () {
        AuthoritiesConverter authoritiesConverter = new AuthoritiesConverter();
        List<GrantedAuthority> authorities = Arrays.asList(   new SimpleGrantedAuthority(UserRoleName.ROLE_ADMIN.name()),
                                                                    new SimpleGrantedAuthority(UserRoleName.ROLE_USER.name()));
        String asString = authoritiesConverter.convertToDatabaseColumn(authorities);

        Assert.assertEquals ("Invalid string ", roles, asString);

    }
}
