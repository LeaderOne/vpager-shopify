
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
    "email",
    "accepts_marketing",
    "created_at",
    "updated_at",
    "first_name",
    "last_name",
    "orders_count",
    "state",
    "total_spent",
    "last_order_id",
    "note",
    "verified_email",
    "multipass_identifier",
    "tax_exempt",
    "tags",
    "last_order_name",
    "default_address"
})
public class Customer implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("accepts_marketing")
    private Boolean acceptsMarketing;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("orders_count")
    private Long ordersCount;
    @JsonProperty("state")
    private String state;
    @JsonProperty("total_spent")
    private String totalSpent;
    @JsonProperty("last_order_id")
    private Long lastOrderId;
    @JsonProperty("note")
    private Object note;
    @JsonProperty("verified_email")
    private Boolean verifiedEmail;
    @JsonProperty("multipass_identifier")
    private Object multipassIdentifier;
    @JsonProperty("tax_exempt")
    private Boolean taxExempt;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("last_order_name")
    private String lastOrderName;
    @JsonProperty("default_address")
    private DefaultAddress defaultAddress;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3720430720581743924L;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("accepts_marketing")
    public Boolean getAcceptsMarketing() {
        return acceptsMarketing;
    }

    @JsonProperty("accepts_marketing")
    public void setAcceptsMarketing(Boolean acceptsMarketing) {
        this.acceptsMarketing = acceptsMarketing;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("orders_count")
    public Long getOrdersCount() {
        return ordersCount;
    }

    @JsonProperty("orders_count")
    public void setOrdersCount(Long ordersCount) {
        this.ordersCount = ordersCount;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("total_spent")
    public String getTotalSpent() {
        return totalSpent;
    }

    @JsonProperty("total_spent")
    public void setTotalSpent(String totalSpent) {
        this.totalSpent = totalSpent;
    }

    @JsonProperty("last_order_id")
    public Long getLastOrderId() {
        return lastOrderId;
    }

    @JsonProperty("last_order_id")
    public void setLastOrderId(Long lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    @JsonProperty("note")
    public Object getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(Object note) {
        this.note = note;
    }

    @JsonProperty("verified_email")
    public Boolean getVerifiedEmail() {
        return verifiedEmail;
    }

    @JsonProperty("verified_email")
    public void setVerifiedEmail(Boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    @JsonProperty("multipass_identifier")
    public Object getMultipassIdentifier() {
        return multipassIdentifier;
    }

    @JsonProperty("multipass_identifier")
    public void setMultipassIdentifier(Object multipassIdentifier) {
        this.multipassIdentifier = multipassIdentifier;
    }

    @JsonProperty("tax_exempt")
    public Boolean getTaxExempt() {
        return taxExempt;
    }

    @JsonProperty("tax_exempt")
    public void setTaxExempt(Boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    @JsonProperty("last_order_name")
    public String getLastOrderName() {
        return lastOrderName;
    }

    @JsonProperty("last_order_name")
    public void setLastOrderName(String lastOrderName) {
        this.lastOrderName = lastOrderName;
    }

    @JsonProperty("default_address")
    public DefaultAddress getDefaultAddress() {
        return defaultAddress;
    }

    @JsonProperty("default_address")
    public void setDefaultAddress(DefaultAddress defaultAddress) {
        this.defaultAddress = defaultAddress;
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
