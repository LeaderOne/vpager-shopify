
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
    "title",
    "price",
    "code",
    "source",
    "phone",
    "requested_fulfillment_service_id",
    "delivery_category",
    "carrier_identifier",
    "tax_lines"
})
public class ShippingLine implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("price")
    private String price;
    @JsonProperty("code")
    private String code;
    @JsonProperty("source")
    private String source;
    @JsonProperty("phone")
    private Object phone;
    @JsonProperty("requested_fulfillment_service_id")
    private Object requestedFulfillmentServiceId;
    @JsonProperty("delivery_category")
    private Object deliveryCategory;
    @JsonProperty("carrier_identifier")
    private Object carrierIdentifier;
    @JsonProperty("tax_lines")
    private List<Object> taxLines = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6214415432063564098L;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("phone")
    public Object getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(Object phone) {
        this.phone = phone;
    }

    @JsonProperty("requested_fulfillment_service_id")
    public Object getRequestedFulfillmentServiceId() {
        return requestedFulfillmentServiceId;
    }

    @JsonProperty("requested_fulfillment_service_id")
    public void setRequestedFulfillmentServiceId(Object requestedFulfillmentServiceId) {
        this.requestedFulfillmentServiceId = requestedFulfillmentServiceId;
    }

    @JsonProperty("delivery_category")
    public Object getDeliveryCategory() {
        return deliveryCategory;
    }

    @JsonProperty("delivery_category")
    public void setDeliveryCategory(Object deliveryCategory) {
        this.deliveryCategory = deliveryCategory;
    }

    @JsonProperty("carrier_identifier")
    public Object getCarrierIdentifier() {
        return carrierIdentifier;
    }

    @JsonProperty("carrier_identifier")
    public void setCarrierIdentifier(Object carrierIdentifier) {
        this.carrierIdentifier = carrierIdentifier;
    }

    @JsonProperty("tax_lines")
    public List<Object> getTaxLines() {
        return taxLines;
    }

    @JsonProperty("tax_lines")
    public void setTaxLines(List<Object> taxLines) {
        this.taxLines = taxLines;
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
