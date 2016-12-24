
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
    "email",
    "closed_at",
    "created_at",
    "updated_at",
    "number",
    "note",
    "token",
    "gateway",
    "test",
    "total_price",
    "subtotal_price",
    "total_weight",
    "total_tax",
    "taxes_included",
    "currency",
    "financial_status",
    "confirmed",
    "total_discounts",
    "total_line_items_price",
    "cart_token",
    "buyer_accepts_marketing",
    "name",
    "referring_site",
    "landing_site",
    "cancelled_at",
    "cancel_reason",
    "total_price_usd",
    "checkout_token",
    "reference",
    "user_id",
    "location_id",
    "source_identifier",
    "source_url",
    "processed_at",
    "device_id",
    "browser_ip",
    "landing_site_ref",
    "order_number",
    "discount_codes",
    "note_attributes",
    "payment_gateway_names",
    "processing_method",
    "checkout_id",
    "source_name",
    "fulfillment_status",
    "tax_lines",
    "tags",
    "contact_email",
    "order_status_url",
    "line_items",
    "shipping_lines",
    "billing_address",
    "shipping_address",
    "fulfillments",
    "client_details",
    "refunds",
    "payment_details",
    "customer"
})
public class Order implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("closed_at")
    private Object closedAt;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("number")
    private Long number;
    @JsonProperty("note")
    private Object note;
    @JsonProperty("token")
    private String token;
    @JsonProperty("gateway")
    private String gateway;
    @JsonProperty("test")
    private Boolean test;
    @JsonProperty("total_price")
    private String totalPrice;
    @JsonProperty("subtotal_price")
    private String subtotalPrice;
    @JsonProperty("total_weight")
    private Integer totalWeight;
    @JsonProperty("total_tax")
    private String totalTax;
    @JsonProperty("taxes_included")
    private Boolean taxesIncluded;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("financial_status")
    private String financialStatus;
    @JsonProperty("confirmed")
    private Boolean confirmed;
    @JsonProperty("total_discounts")
    private String totalDiscounts;
    @JsonProperty("total_line_items_price")
    private String totalLineItemsPrice;
    @JsonProperty("cart_token")
    private String cartToken;
    @JsonProperty("buyer_accepts_marketing")
    private Boolean buyerAcceptsMarketing;
    @JsonProperty("name")
    private String name;
    @JsonProperty("referring_site")
    private String referringSite;
    @JsonProperty("landing_site")
    private String landingSite;
    @JsonProperty("cancelled_at")
    private Object cancelledAt;
    @JsonProperty("cancel_reason")
    private Object cancelReason;
    @JsonProperty("total_price_usd")
    private String totalPriceUsd;
    @JsonProperty("checkout_token")
    private Object checkoutToken;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("user_id")
    private Object userId;
    @JsonProperty("location_id")
    private Object locationId;
    @JsonProperty("source_identifier")
    private String sourceIdentifier;
    @JsonProperty("source_url")
    private Object sourceUrl;
    @JsonProperty("processed_at")
    private String processedAt;
    @JsonProperty("device_id")
    private Object deviceId;
    @JsonProperty("browser_ip")
    private Object browserIp;
    @JsonProperty("landing_site_ref")
    private String landingSiteRef;
    @JsonProperty("order_number")
    private Long orderNumber;
    @JsonProperty("discount_codes")
    private List<DiscountCode> discountCodes = null;
    @JsonProperty("note_attributes")
    private List<NoteAttribute> noteAttributes = null;
    @JsonProperty("payment_gateway_names")
    private List<String> paymentGatewayNames = null;
    @JsonProperty("processing_method")
    private String processingMethod;
    @JsonProperty("checkout_id")
    private Long checkoutId;
    @JsonProperty("source_name")
    private String sourceName;
    @JsonProperty("fulfillment_status")
    private Object fulfillmentStatus;
    @JsonProperty("tax_lines")
    private List<TaxLine> taxLines = null;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("contact_email")
    private String contactEmail;
    @JsonProperty("order_status_url")
    private Object orderStatusUrl;
    @JsonProperty("line_items")
    private List<LineItem> lineItems = null;
    @JsonProperty("shipping_lines")
    private List<ShippingLine> shippingLines = null;
    @JsonProperty("billing_address")
    private BillingAddress billingAddress;
    @JsonProperty("shipping_address")
    private ShippingAddress shippingAddress;
    @JsonProperty("fulfillments")
    private List<Fulfillment> fulfillments = null;
    @JsonProperty("client_details")
    private ClientDetails clientDetails;
    @JsonProperty("refunds")
    private List<Refund> refunds = null;
    @JsonProperty("payment_details")
    private PaymentDetails paymentDetails;
    @JsonProperty("customer")
    private Customer customer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2247724829910588510L;

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

    @JsonProperty("closed_at")
    public Object getClosedAt() {
        return closedAt;
    }

    @JsonProperty("closed_at")
    public void setClosedAt(Object closedAt) {
        this.closedAt = closedAt;
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

    @JsonProperty("number")
    public Long getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(Long number) {
        this.number = number;
    }

    @JsonProperty("note")
    public Object getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(Object note) {
        this.note = note;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("gateway")
    public String getGateway() {
        return gateway;
    }

    @JsonProperty("gateway")
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    @JsonProperty("test")
    public Boolean getTest() {
        return test;
    }

    @JsonProperty("test")
    public void setTest(Boolean test) {
        this.test = test;
    }

    @JsonProperty("total_price")
    public String getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("total_price")
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("subtotal_price")
    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    @JsonProperty("subtotal_price")
    public void setSubtotalPrice(String subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    @JsonProperty("total_weight")
    public Integer getTotalWeight() {
        return totalWeight;
    }

    @JsonProperty("total_weight")
    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    @JsonProperty("total_tax")
    public String getTotalTax() {
        return totalTax;
    }

    @JsonProperty("total_tax")
    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    @JsonProperty("taxes_included")
    public Boolean getTaxesIncluded() {
        return taxesIncluded;
    }

    @JsonProperty("taxes_included")
    public void setTaxesIncluded(Boolean taxesIncluded) {
        this.taxesIncluded = taxesIncluded;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("financial_status")
    public String getFinancialStatus() {
        return financialStatus;
    }

    @JsonProperty("financial_status")
    public void setFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }

    @JsonProperty("confirmed")
    public Boolean getConfirmed() {
        return confirmed;
    }

    @JsonProperty("confirmed")
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @JsonProperty("total_discounts")
    public String getTotalDiscounts() {
        return totalDiscounts;
    }

    @JsonProperty("total_discounts")
    public void setTotalDiscounts(String totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    @JsonProperty("total_line_items_price")
    public String getTotalLineItemsPrice() {
        return totalLineItemsPrice;
    }

    @JsonProperty("total_line_items_price")
    public void setTotalLineItemsPrice(String totalLineItemsPrice) {
        this.totalLineItemsPrice = totalLineItemsPrice;
    }

    @JsonProperty("cart_token")
    public String getCartToken() {
        return cartToken;
    }

    @JsonProperty("cart_token")
    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }

    @JsonProperty("buyer_accepts_marketing")
    public Boolean getBuyerAcceptsMarketing() {
        return buyerAcceptsMarketing;
    }

    @JsonProperty("buyer_accepts_marketing")
    public void setBuyerAcceptsMarketing(Boolean buyerAcceptsMarketing) {
        this.buyerAcceptsMarketing = buyerAcceptsMarketing;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("referring_site")
    public String getReferringSite() {
        return referringSite;
    }

    @JsonProperty("referring_site")
    public void setReferringSite(String referringSite) {
        this.referringSite = referringSite;
    }

    @JsonProperty("landing_site")
    public String getLandingSite() {
        return landingSite;
    }

    @JsonProperty("landing_site")
    public void setLandingSite(String landingSite) {
        this.landingSite = landingSite;
    }

    @JsonProperty("cancelled_at")
    public Object getCancelledAt() {
        return cancelledAt;
    }

    @JsonProperty("cancelled_at")
    public void setCancelledAt(Object cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    @JsonProperty("cancel_reason")
    public Object getCancelReason() {
        return cancelReason;
    }

    @JsonProperty("cancel_reason")
    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    @JsonProperty("total_price_usd")
    public String getTotalPriceUsd() {
        return totalPriceUsd;
    }

    @JsonProperty("total_price_usd")
    public void setTotalPriceUsd(String totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    @JsonProperty("checkout_token")
    public Object getCheckoutToken() {
        return checkoutToken;
    }

    @JsonProperty("checkout_token")
    public void setCheckoutToken(Object checkoutToken) {
        this.checkoutToken = checkoutToken;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("user_id")
    public Object getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Object userId) {
        this.userId = userId;
    }

    @JsonProperty("location_id")
    public Object getLocationId() {
        return locationId;
    }

    @JsonProperty("location_id")
    public void setLocationId(Object locationId) {
        this.locationId = locationId;
    }

    @JsonProperty("source_identifier")
    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    @JsonProperty("source_identifier")
    public void setSourceIdentifier(String sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    @JsonProperty("source_url")
    public Object getSourceUrl() {
        return sourceUrl;
    }

    @JsonProperty("source_url")
    public void setSourceUrl(Object sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @JsonProperty("processed_at")
    public String getProcessedAt() {
        return processedAt;
    }

    @JsonProperty("processed_at")
    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    @JsonProperty("device_id")
    public Object getDeviceId() {
        return deviceId;
    }

    @JsonProperty("device_id")
    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("browser_ip")
    public Object getBrowserIp() {
        return browserIp;
    }

    @JsonProperty("browser_ip")
    public void setBrowserIp(Object browserIp) {
        this.browserIp = browserIp;
    }

    @JsonProperty("landing_site_ref")
    public String getLandingSiteRef() {
        return landingSiteRef;
    }

    @JsonProperty("landing_site_ref")
    public void setLandingSiteRef(String landingSiteRef) {
        this.landingSiteRef = landingSiteRef;
    }

    @JsonProperty("order_number")
    public Long getOrderNumber() {
        return orderNumber;
    }

    @JsonProperty("order_number")
    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    @JsonProperty("discount_codes")
    public List<DiscountCode> getDiscountCodes() {
        return discountCodes;
    }

    @JsonProperty("discount_codes")
    public void setDiscountCodes(List<DiscountCode> discountCodes) {
        this.discountCodes = discountCodes;
    }

    @JsonProperty("note_attributes")
    public List<NoteAttribute> getNoteAttributes() {
        return noteAttributes;
    }

    @JsonProperty("note_attributes")
    public void setNoteAttributes(List<NoteAttribute> noteAttributes) {
        this.noteAttributes = noteAttributes;
    }

    @JsonProperty("payment_gateway_names")
    public List<String> getPaymentGatewayNames() {
        return paymentGatewayNames;
    }

    @JsonProperty("payment_gateway_names")
    public void setPaymentGatewayNames(List<String> paymentGatewayNames) {
        this.paymentGatewayNames = paymentGatewayNames;
    }

    @JsonProperty("processing_method")
    public String getProcessingMethod() {
        return processingMethod;
    }

    @JsonProperty("processing_method")
    public void setProcessingMethod(String processingMethod) {
        this.processingMethod = processingMethod;
    }

    @JsonProperty("checkout_id")
    public Long getCheckoutId() {
        return checkoutId;
    }

    @JsonProperty("checkout_id")
    public void setCheckoutId(Long checkoutId) {
        this.checkoutId = checkoutId;
    }

    @JsonProperty("source_name")
    public String getSourceName() {
        return sourceName;
    }

    @JsonProperty("source_name")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    @JsonProperty("contact_email")
    public String getContactEmail() {
        return contactEmail;
    }

    @JsonProperty("contact_email")
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @JsonProperty("order_status_url")
    public Object getOrderStatusUrl() {
        return orderStatusUrl;
    }

    @JsonProperty("order_status_url")
    public void setOrderStatusUrl(Object orderStatusUrl) {
        this.orderStatusUrl = orderStatusUrl;
    }

    @JsonProperty("line_items")
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @JsonProperty("line_items")
    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @JsonProperty("shipping_lines")
    public List<ShippingLine> getShippingLines() {
        return shippingLines;
    }

    @JsonProperty("shipping_lines")
    public void setShippingLines(List<ShippingLine> shippingLines) {
        this.shippingLines = shippingLines;
    }

    @JsonProperty("billing_address")
    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    @JsonProperty("billing_address")
    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    @JsonProperty("shipping_address")
    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    @JsonProperty("shipping_address")
    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @JsonProperty("fulfillments")
    public List<Fulfillment> getFulfillments() {
        return fulfillments;
    }

    @JsonProperty("fulfillments")
    public void setFulfillments(List<Fulfillment> fulfillments) {
        this.fulfillments = fulfillments;
    }

    @JsonProperty("client_details")
    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    @JsonProperty("client_details")
    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    @JsonProperty("refunds")
    public List<Refund> getRefunds() {
        return refunds;
    }

    @JsonProperty("refunds")
    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    @JsonProperty("payment_details")
    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    @JsonProperty("payment_details")
    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @JsonProperty("customer")
    public Customer getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer customer) {
        this.customer = customer;
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
