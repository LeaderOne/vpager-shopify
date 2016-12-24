
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
    "status",
    "created_at",
    "service",
    "updated_at",
    "tracking_company",
    "shipment_status",
    "tracking_number",
    "tracking_numbers",
    "tracking_url",
    "tracking_urls",
    "receipt",
    "line_items"
})
public class Fulfillment implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("service")
    private String service;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("tracking_company")
    private Object trackingCompany;
    @JsonProperty("shipment_status")
    private Object shipmentStatus;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("tracking_numbers")
    private List<String> trackingNumbers = null;
    @JsonProperty("tracking_url")
    private String trackingUrl;
    @JsonProperty("tracking_urls")
    private List<String> trackingUrls = null;
    @JsonProperty("receipt")
    private Receipt receipt;
    @JsonProperty("line_items")
    private List<LineItem> lineItems = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6197223325936902443L;

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

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("tracking_company")
    public Object getTrackingCompany() {
        return trackingCompany;
    }

    @JsonProperty("tracking_company")
    public void setTrackingCompany(Object trackingCompany) {
        this.trackingCompany = trackingCompany;
    }

    @JsonProperty("shipment_status")
    public Object getShipmentStatus() {
        return shipmentStatus;
    }

    @JsonProperty("shipment_status")
    public void setShipmentStatus(Object shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    @JsonProperty("tracking_number")
    public String getTrackingNumber() {
        return trackingNumber;
    }

    @JsonProperty("tracking_number")
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @JsonProperty("tracking_numbers")
    public List<String> getTrackingNumbers() {
        return trackingNumbers;
    }

    @JsonProperty("tracking_numbers")
    public void setTrackingNumbers(List<String> trackingNumbers) {
        this.trackingNumbers = trackingNumbers;
    }

    @JsonProperty("tracking_url")
    public String getTrackingUrl() {
        return trackingUrl;
    }

    @JsonProperty("tracking_url")
    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    @JsonProperty("tracking_urls")
    public List<String> getTrackingUrls() {
        return trackingUrls;
    }

    @JsonProperty("tracking_urls")
    public void setTrackingUrls(List<String> trackingUrls) {
        this.trackingUrls = trackingUrls;
    }

    @JsonProperty("receipt")
    public Receipt getReceipt() {
        return receipt;
    }

    @JsonProperty("receipt")
    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @JsonProperty("line_items")
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @JsonProperty("line_items")
    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
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
