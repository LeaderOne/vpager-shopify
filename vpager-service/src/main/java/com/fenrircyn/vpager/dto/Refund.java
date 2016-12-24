
package com.fenrircyn.vpager.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "order_id",
    "created_at",
    "note",
    "restock",
    "user_id",
    "refund_line_items",
    "transactions",
    "order_adjustments"
})
public class Refund implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("note")
    private String note;
    @JsonProperty("restock")
    private Boolean restock;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("refund_line_items")
    private List<RefundLineItem> refundLineItems = null;
    @JsonProperty("transactions")
    private List<Transaction> transactions = null;
    @JsonProperty("order_adjustments")
    private List<Object> orderAdjustments = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1429725520878003664L;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("order_id")
    public Long getOrderId() {
        return orderId;
    }

    @JsonProperty("order_id")
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("restock")
    public Boolean getRestock() {
        return restock;
    }

    @JsonProperty("restock")
    public void setRestock(Boolean restock) {
        this.restock = restock;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("refund_line_items")
    public List<RefundLineItem> getRefundLineItems() {
        return refundLineItems;
    }

    @JsonProperty("refund_line_items")
    public void setRefundLineItems(List<RefundLineItem> refundLineItems) {
        this.refundLineItems = refundLineItems;
    }

    @JsonProperty("transactions")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @JsonProperty("transactions")
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @JsonProperty("order_adjustments")
    public List<Object> getOrderAdjustments() {
        return orderAdjustments;
    }

    @JsonProperty("order_adjustments")
    public void setOrderAdjustments(List<Object> orderAdjustments) {
        this.orderAdjustments = orderAdjustments;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
