package com.fenrircyn.vpager.entities;

import java.io.Serializable;

/**
 * Created by markelba on 12/13/16.
 */
public class ShopifyWebhookRequest implements Serializable {
    private static final long serialVersionUID = 38723872387234981L;
    private String address;
    private String topic;
    private String format;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
