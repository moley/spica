package org.spica.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class AuthoritiesConverter implements AttributeConverter<List<GrantedAuthority>, String> {
  @Override
  public String convertToDatabaseColumn(List<GrantedAuthority> attribute) {

    if (attribute == null)
      return "";

    List<String> roles = new ArrayList<String>();
    for (GrantedAuthority next: attribute)
      roles.add(next.getAuthority());

    return String.join(",", roles);
  }

  @Override
  public List<GrantedAuthority> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty())
      return new ArrayList<GrantedAuthority>();

    String [] dbDatas = dbData.split(",");
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (String next: dbDatas) {
      if (! next.trim().isEmpty())
        authorities.add(new SimpleGrantedAuthority(next));
    }
    return authorities;
  }
}
