
package com.fenrircyn.vpager.dto;

import java.io.Serializable;
import java.util.HashMap;
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
    "quantity",
    "line_item_id",
    "subtotal",
    "total_tax",
    "line_item"
})
public class RefundLineItem implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("line_item_id")
    private Long lineItemId;
    @JsonProperty("subtotal")
    private Double subtotal;
    @JsonProperty("total_tax")
    private Double totalTax;
    @JsonProperty("line_item")
    private LineItem lineItem;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2360502390206123025L;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("line_item_id")
    public Long getLineItemId() {
        return lineItemId;
    }

    @JsonProperty("line_item_id")
    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    @JsonProperty("subtotal")
    public Double getSubtotal() {
        return subtotal;
    }

    @JsonProperty("subtotal")
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @JsonProperty("total_tax")
    public Double getTotalTax() {
        return totalTax;
    }

    @JsonProperty("total_tax")
    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    @JsonProperty("line_item")
    public LineItem getLineItem() {
        return lineItem;
    }

    @JsonProperty("line_item")
    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
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
