package com.fenrircyn.vpager.security.token;

import com.fenrircyn.vpager.security.ValidRoles;
import com.fenrircyn.vpager.security.validation.ShopifyValidator;

public class ShopifyWebhookAuthenticationToken extends ShopifyAuthenticationToken {
    private static long serialVersionUID = 999900000999L;

    private long epochTime;
    private String pathPrefix;
    private String hash;

    public ShopifyWebhookAuthenticationToken(String shopifyShopUrl, String hmac, String body, String customerEmail,
                                      long customerId, long epochTime, String pathPrefix, String hash) {
        super(shopifyShopUrl, customerEmail, hmac, body, customerId);

        this.epochTime = epochTime;
        this.pathPrefix = pathPrefix;
        this.hash = hash;
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
}
