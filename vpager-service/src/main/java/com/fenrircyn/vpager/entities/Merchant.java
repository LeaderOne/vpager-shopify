package com.fenrircyn.vpager.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by markelba on 12/27/15.
 */
@Entity
@Table(name = "merchant", schema = "ticketing")
public class Merchant implements Serializable {
    private static final long serialVersionUID = 879636873887963L;

    @Id
    @Column(name = "merchant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "now_serving")
    private long nowServing;

    @Column(name = "shopify_shop_url")
    private String shopifyShopUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "shopify_customer_id")
    private long shopifyCustomerId;

    public Merchant() {}

    public Merchant(String contactEmail) {
        this.email = contactEmail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNowServing() {
        return nowServing;
    }

    public void setNowServing(long nowServing) {
        this.nowServing = nowServing;
    }

    public String getShopifyShopUrl() {
        return shopifyShopUrl;
    }

    public void setShopifyShopUrl(String shopifyShopUrl) {
        this.shopifyShopUrl = shopifyShopUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getShopifyCustomerId() {
        return shopifyCustomerId;
    }

    public void setShopifyCustomerId(long shopifyCustomerId) {
        this.shopifyCustomerId = shopifyCustomerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("merchant_id", id).append("now_serving", nowServing)
                .append("shopify_shop_url", shopifyShopUrl).append("email", email).toString();
    }
}
