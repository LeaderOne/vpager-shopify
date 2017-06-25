package com.fenrircyn.vpager.filters;

import com.google.common.collect.Sets;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.Map;


/**
 * Created by markelba on 6/24/17.
 */
public class ShopifyPrincipalExtractor extends FixedPrincipalExtractor {
    @Override
    public Object extractPrincipal(Map<String, Object> userMap) {
        Map<String,String> map = (Map<String, String>) userMap.get("shop");
        if (map != null) {
            String username = map.get("email");
            String shop = map.get("myshopify_domain");

            MerchantUser merchantUser = new MerchantUser(username, "unknown",
                    shop,
                    true, true, true,
                    true, Sets.newHashSet(new SimpleGrantedAuthority("ROLE_USER")));

            return merchantUser;
        } else {
            return null;
        }
    }
}
