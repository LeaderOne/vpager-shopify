package com.fenrircyn.vpager.security.token;

import com.fenrircyn.vpager.security.ShopifyMerchantCustomerUser;
import com.fenrircyn.vpager.security.ValidRoles;
import com.google.common.collect.Sets;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class ShopifyAuthenticationToken extends AbstractAuthenticationToken {
    private static long serialVersionUID = 5948388888L;

    private String shopifyShopUrl;
    private long customerId;
    private String hmac;
    private String body;
    protected ShopifyMerchantCustomerUser principal;


    public ShopifyAuthenticationToken(String shopifyShopUrl, String customerEmail, String hmac, String body,
                                      long customerId) {
        super(Collections.singletonList(ValidRoles.PROXY_ROLE));

        this.shopifyShopUrl = shopifyShopUrl;
        this.customerId = customerId;
        this.hmac = hmac;
        this.body = body;
        this.principal = new ShopifyMerchantCustomerUser(customerId, customerEmail, "unknown", shopifyShopUrl,
                true, true, true, true,
                Sets.newHashSet(ValidRoles.USER_ROLE));
    }

    @Override
    public Object getCredentials() {
        return this;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getShopifyShopUrl() {
        return shopifyShopUrl;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getHmac() {
        return hmac;
    }

    public String getBody() {
        return body;
    }
}
