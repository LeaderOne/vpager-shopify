package com.fenrircyn.vpager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ShopifyMerchantCustomerUser extends User {
    private static long serialVersionUID = 78348773458728L;

    private ShopOwnerUser customerOf;
    private long customerId;

    public ShopifyMerchantCustomerUser(long customerId, String username, String password,
                                       String shopUrl, boolean enabled, boolean accountNonExpired,
                                       boolean credentialsNonExpired, boolean accountNonLocked,
                                       Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        customerOf = new ShopOwnerUser(username, password, shopUrl, authorities);
        this.customerId = customerId;
    }

    public ShopOwnerUser getCustomerOf() {
        return customerOf;
    }

    public void setCustomerOf(ShopOwnerUser customerOf) {
        this.customerOf = customerOf;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
