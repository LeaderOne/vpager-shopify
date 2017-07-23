package com.fenrircyn.vpager.security.extractors;

import com.fenrircyn.vpager.security.ShopOwnerUser;
import com.google.common.collect.Sets;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;


/**
 * Created by markelba on 6/24/17.
 */
public class ShopifyShopOwnerPrincipalExtractor extends FixedPrincipalExtractor {
    public static final ShopifyShopOwnerPrincipalExtractor instance = new ShopifyShopOwnerPrincipalExtractor();

    private ShopifyShopOwnerPrincipalExtractor() {
    }

    @Override
    public Object extractPrincipal(Map<String, Object> userMap) {
        Map<String,String> map = (Map<String, String>) userMap.get("shop");
        if (map != null) {
            String username = map.get("email");
            String shopifyShopUrl = map.get("myshopify_domain");

            ShopOwnerUser shopOwnerUser = new ShopOwnerUser(username, "unknown",
                    shopifyShopUrl,
                    true, true, true,
                    true, Sets.newHashSet(new SimpleGrantedAuthority("ROLE_USER")));

            return shopOwnerUser;
        } else {
            return null;
        }
    }
}
