
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
    "variant_id",
    "title",
    "quantity",
    "price",
    "grams",
    "sku",
    "variant_title",
    "vendor",
    "fulfillment_service",
    "product_id",
    "requires_shipping",
    "taxable",
    "gift_card",
    "name",
    "variant_inventory_management",
    "properties",
    "product_exists",
    "fulfillable_quantity",
    "total_discount",
    "fulfillment_status",
    "tax_lines"
})
public class LineItem implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("variant_id")
    private Long variantId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("price")
    private String price;
    @JsonProperty("grams")
    private Integer grams;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("variant_title")
    private String variantTitle;
    @JsonProperty("vendor")
    private Object vendor;
    @JsonProperty("fulfillment_service")
    private String fulfillmentService;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("requires_shipping")
    private Boolean requiresShipping;
    @JsonProperty("taxable")
    private Boolean taxable;
    @JsonProperty("gift_card")
    private Boolean giftCard;
    @JsonProperty("name")
    private String name;
    @JsonProperty("variant_inventory_management")
    private String variantInventoryManagement;
    @JsonProperty("properties")
    private List<Property> properties = null;
    @JsonProperty("product_exists")
    private Boolean productExists;
    @JsonProperty("fulfillable_quantity")
    private Integer fulfillableQuantity;
    @JsonProperty("total_discount")
    private String totalDiscount;
    @JsonProperty("fulfillment_status")
    private Object fulfillmentStatus;
    @JsonProperty("tax_lines")
    private List<TaxLine> taxLines = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1528517013338416857L;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("variant_id")
    public Long getVariantId() {
        return variantId;
    }

    @JsonProperty("variant_id")
    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("grams")
    public Integer getGrams() {
        return grams;
    }

    @JsonProperty("grams")
    public void setGrams(Integer grams) {
        this.grams = grams;
    }

    @JsonProperty("sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("variant_title")
    public String getVariantTitle() {
        return variantTitle;
    }

    @JsonProperty("variant_title")
    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    @JsonProperty("vendor")
    public Object getVendor() {
        return vendor;
    }

    @JsonProperty("vendor")
    public void setVendor(Object vendor) {
        this.vendor = vendor;
    }

    @JsonProperty("fulfillment_service")
    public String getFulfillmentService() {
        return fulfillmentService;
    }

    @JsonProperty("fulfillment_service")
    public void setFulfillmentService(String fulfillmentService) {
        this.fulfillmentService = fulfillmentService;
    }

    @JsonProperty("product_id")
    public Long getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @JsonProperty("requires_shipping")
    public Boolean getRequiresShipping() {
        return requiresShipping;
    }

    @JsonProperty("requires_shipping")
    public void setRequiresShipping(Boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }

    @JsonProperty("taxable")
    public Boolean getTaxable() {
        return taxable;
    }

    @JsonProperty("taxable")
    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    @JsonProperty("gift_card")
    public Boolean getGiftCard() {
        return giftCard;
    }

    @JsonProperty("gift_card")
    public void setGiftCard(Boolean giftCard) {
        this.giftCard = giftCard;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("variant_inventory_management")
    public String getVariantInventoryManagement() {
        return variantInventoryManagement;
    }

    @JsonProperty("variant_inventory_management")
    public void setVariantInventoryManagement(String variantInventoryManagement) {
        this.variantInventoryManagement = variantInventoryManagement;
    }

    @JsonProperty("properties")
    public List<Property> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @JsonProperty("product_exists")
    public Boolean getProductExists() {
        return productExists;
    }

    @JsonProperty("product_exists")
    public void setProductExists(Boolean productExists) {
        this.productExists = productExists;
    }

    @JsonProperty("fulfillable_quantity")
    public Integer getFulfillableQuantity() {
        return fulfillableQuantity;
    }

    @JsonProperty("fulfillable_quantity")
    public void setFulfillableQuantity(Integer fulfillableQuantity) {
        this.fulfillableQuantity = fulfillableQuantity;
    }

    @JsonProperty("total_discount")
    public String getTotalDiscount() {
        return totalDiscount;
    }

    @JsonProperty("total_discount")
    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @JsonProperty("fulfillment_status")
    public Object getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    @JsonProperty("fulfillment_status")
    public void setFulfillmentStatus(Object fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    @JsonProperty("tax_lines")
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

    @JsonProperty("tax_lines")
    public void setTaxLines(List<TaxLine> taxLines) {
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
