package com.fenrircyn.vpager.security.extractors;

import com.fenrircyn.vpager.security.ValidRoles;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

public class ShopifyAuthoritiesExtractor implements AuthoritiesExtractor {
    @SuppressWarnings("unchecked")
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> userMap) {
        Map<String,String> map = (Map<String, String>) userMap.get("shop");
        String url = map.get("email");

        if(url.contains("zealcon")) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(ValidRoles.USER_ROLE.name());
        }
        throw new BadCredentialsException("Not in Zealcon organization");
    }
}
