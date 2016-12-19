package com.fenrircyn.vpager.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by markelba on 12/13/16.
 */
public class ShopifyWebhook extends ShopifyWebhookRequest {
    private static final long serialVersionUID = 483457834829L;

    private long id;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    private String[] fields;
    @JsonProperty("metafield_namespaces")
    private String[] metafieldNamespaces;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String[] getMetafieldNamespaces() {
        return metafieldNamespaces;
    }

    public void setMetafieldNamespaces(String[] metafieldNamespaces) {
        this.metafieldNamespaces = metafieldNamespaces;
    }
}
