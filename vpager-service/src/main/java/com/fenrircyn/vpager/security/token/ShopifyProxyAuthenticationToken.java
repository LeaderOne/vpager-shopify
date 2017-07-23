package com.fenrircyn.vpager.security.token;

import com.fenrircyn.vpager.security.ValidRoles;

public class ShopifyProxyAuthenticationToken extends ShopifyAuthenticationToken {
    private static final long serialVersionUID = 999900000999L;

    private long epochTime;
    private String pathPrefix;
    private String hash;
    private long customerId;
    private String shopEmail;

    public ShopifyProxyAuthenticationToken(String shopifyShopUrl, String hmac, String body, String shopEmail,
                                           long customerId, long epochTime, String pathPrefix, String hash) {
        super(shopifyShopUrl, hmac, body, ValidRoles.WEBHOOK_ROLE);

        this.epochTime = epochTime;
        this.pathPrefix = pathPrefix;
        this.hash = hash;
        this.customerId = customerId;
        this.shopEmail = shopEmail;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getHash() {
        return hash;
    }

    public String getEmail() {
        return principal.getUsername();
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public long getCustomerId() {
        return customerId;
    }
}
