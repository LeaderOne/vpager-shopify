package com.fenrircyn.vpager.entities;

import com.fenrircyn.vpager.dto.Webhook;

import java.io.Serializable;

/**
 * Created by markelba on 12/13/16.
 */
public class ShopifyWebhookContainer implements Serializable {
    private static final long serialVersionUID = 982187213761276L;

    private ShopifyWebhook webhook;

    public ShopifyWebhookContainer() {

    }

    public ShopifyWebhookContainer(ShopifyWebhook webhook) {
        this.webhook = webhook;
    }

    public ShopifyWebhook getWebhook() {
        return webhook;
    }

    public void setWebhook(ShopifyWebhook webhook) {
        this.webhook = webhook;
    }
}
