package com.fenrircyn.vpager.business.impl;

import com.fenrircyn.vpager.business.FulfillShopifyBusiness;
import com.fenrircyn.vpager.dto.Fulfillment;
import com.fenrircyn.vpager.dto.LineItem;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * Created by markelba on 12/28/16.
 */
@Component
public class FulfillShopifyBusinessImpl implements FulfillShopifyBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RestTemplate zshopRestTemplate;

    @Value("${shopify.api.fulfill.url}")
    private String zshopFulfillmentUrl;

    @Value("${vpager.url}")
    private String vpagerUrl;

    @Override
    public void createFulfillmentFromMerchant(Merchant merchant, Order order, LineItem lineItem) {
        Fulfillment fulfillment = new Fulfillment();

        fulfillment.setOrderId(order.getId());
        fulfillment.setLineItems(Collections.singletonList(lineItem));
        fulfillment.setTrackingNumber(String.valueOf(merchant.getId()));
        fulfillment.setTrackingUrl(vpagerUrl + "/merchant/" + merchant.getId() + "/hangoutashingle");

        String fulfillmentUrl = zshopFulfillmentUrl + order.getId() + "/fulfillments.json";

        logger.debug("Sending fulfillment information to " + fulfillmentUrl);

        try {
            ResponseEntity<Fulfillment> response = zshopRestTemplate.postForEntity(fulfillmentUrl, fulfillment, Fulfillment.class);

            logger.debug("Server response was {} to create fullfillment {} for merchant {} order number {}",
                    response.getStatusCode(), response.getBody().getId(), merchant.getId(), order.getId());
        } catch (RestClientException e) {
            logger.error("Unable to create fulfillment for order id " + order.getId() +
                    ", line ID " + lineItem.getId() + ", merchant " + merchant.getId(), e);
        }
    }
}
