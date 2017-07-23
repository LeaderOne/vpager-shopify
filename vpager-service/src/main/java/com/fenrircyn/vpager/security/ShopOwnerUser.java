package com.fenrircyn.vpager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by markelba on 6/24/17.
 */
public class ShopOwnerUser extends User {
    private static long serialVersionUID = 3487234120349540L;

    private String shopUrl;

    public ShopOwnerUser(String username, String password, String shopUrl, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.shopUrl = shopUrl;
    }

    public ShopOwnerUser(String username, String password,
                         String shopUrl, boolean enabled, boolean accountNonExpired,
                         boolean credentialsNonExpired, boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        this.shopUrl = shopUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }
}
