package com.fenrircyn.vpager.security.token;

import com.fenrircyn.vpager.security.ShopifyMerchantCustomerUser;
import com.fenrircyn.vpager.security.ValidRoles;
import com.google.common.collect.Sets;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

public class ShopifyAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 5948388888L;

    private String shopifyShopUrl;
    private String hmac;
    private String body;
    protected ShopifyMerchantCustomerUser principal;


    public ShopifyAuthenticationToken(String shopifyShopUrl, String hmac, String body) {
        super(Collections.singletonList(ValidRoles.WEBHOOK_ROLE));

        this.shopifyShopUrl = shopifyShopUrl;
        this.hmac = hmac;
        this.body = body;
        this.principal = new ShopifyMerchantCustomerUser("webhook","unknown", shopifyShopUrl,
                true, true, true, true,
                Sets.newHashSet(ValidRoles.USER_ROLE));
    }

    protected ShopifyAuthenticationToken(String shopifyShopUrl, String hmac, String body, ValidRoles authority) {
        super(Collections.singletonList(authority));

        this.shopifyShopUrl = shopifyShopUrl;
        this.hmac = hmac;
        this.body = body;
        this.principal = new ShopifyMerchantCustomerUser("webhook","unknown", shopifyShopUrl,
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

    public String getHmac() {
        return hmac;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopifyAuthenticationToken)) return false;
        if (!super.equals(o)) return false;

        ShopifyAuthenticationToken that = (ShopifyAuthenticationToken) o;

        if (getShopifyShopUrl() != null ? !getShopifyShopUrl().equals(that.getShopifyShopUrl()) : that.getShopifyShopUrl() != null)
            return false;
        if (getHmac() != null ? !getHmac().equals(that.getHmac()) : that.getHmac() != null) return false;
        if (getBody() != null ? !getBody().equals(that.getBody()) : that.getBody() != null) return false;
        return getPrincipal() != null ? getPrincipal().equals(that.getPrincipal()) : that.getPrincipal() == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (getShopifyShopUrl() != null ? getShopifyShopUrl().hashCode() : 0);
        result = 31 * result + (getHmac() != null ? getHmac().hashCode() : 0);
        result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
        result = 31 * result + (getPrincipal() != null ? getPrincipal().hashCode() : 0);
        return result;
    }
}
