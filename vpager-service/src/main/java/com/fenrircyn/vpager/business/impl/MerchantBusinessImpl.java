package com.fenrircyn.vpager.business.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.Fulfillment;
import com.fenrircyn.vpager.dto.LineItem;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;


@Component
public class MerchantBusinessImpl implements MerchantBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RestTemplate zshopRestTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Value("${shopify.api.fulfill.url}")
    private String zshopFulfillmentUrl;

    @Value("${shopify.api.auth.creds}")
    private String authorizationCreds;

    @Resource
    private MerchantRepository merchantRepository;

    @Value("${vpager.url}")
    private String vpagerUrl;

    private static class FulfillmentObject {
        @JsonProperty("fulfillment")
        private Fulfillment fulfillment;

        Fulfillment getFulfillment() {
            return fulfillment;
        }

        void setFulfillment(Fulfillment fulfillment) {
            this.fulfillment = fulfillment;
        }
    }

    @Override
    public Iterable<Merchant> createMerchantsFromOrder(Order order) {
        String contactEmail = order.getContactEmail();

        List<Merchant> merchantList = new ArrayList<>(order.getLineItems().size());
        List<LineItem> lineItems = new ArrayList<>(order.getLineItems());

        Fulfillment finalFulfillment = new Fulfillment();
        finalFulfillment.setLineItems(lineItems);
        finalFulfillment.setTrackingNumber("1234");
        finalFulfillment.setTrackingCompany("Foo");

        for(LineItem lineItem : finalFulfillment.getLineItems()) {
            for (int i = 0; i < lineItem.getQuantity(); i++) {
                logger.debug("Creating merchant from email {} for line item {} with {} tickets", contactEmail, lineItem.getId(), lineItem.getQuantity());
                Merchant merchant = new Merchant(contactEmail);
                merchantList.add(merchant);
            }
        }

        Iterable<Merchant> saved = merchantRepository.save(merchantList);

        processAndSendFulfillment(order, finalFulfillment, saved);


        return saved;
    }



    private void processAndSendFulfillment(Order order, Fulfillment finalFulfillment, Iterable<Merchant> saved) {
        List<String> trackingUrls = new ArrayList<>(order.getLineItems().size());
        finalFulfillment.setTrackingUrls(trackingUrls);
        for(Merchant merchant : saved) {
            trackingUrls.add(vpagerUrl + "/merchant/" + merchant.getId());
        }

        FulfillmentObject fulfillmentObject = new FulfillmentObject();

        fulfillmentObject.setFulfillment(finalFulfillment);

        String fulfillmentUrl = zshopFulfillmentUrl + order.getId() + "/fulfillments.json";
        sendFulfillmentMessage(order, fulfillmentObject, fulfillmentUrl);
    }

    private void sendFulfillmentMessage(Order order, FulfillmentObject fulfillmentObject, String fulfillmentUrl) {
        try {
            StringWriter sw = new StringWriter();
            objectMapper.writeValue(sw, fulfillmentObject);
            logger.debug("Sending fulfillment information to " + fulfillmentUrl + "\n" + sw.toString());
        } catch (IOException e) {
            logger.error("Error logging", e);
        }

        ResponseEntity<String> response;

        try {
            StringWriter sw = new StringWriter();
            objectMapper.writeValue(sw, fulfillmentObject);
            logger.debug("Sending fulfillment information to " + fulfillmentUrl + "\n" + sw.toString());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setCacheControl("no-cache");
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            String encodedAuth = "Basic " + new String(Base64.encodeBase64(authorizationCreds.getBytes()));

            httpHeaders.set("Authorization", encodedAuth);

            HttpEntity<FulfillmentObject> request = new HttpEntity<>(fulfillmentObject, httpHeaders);

            response = zshopRestTemplate.postForEntity(fulfillmentUrl, request, String.class);

            logger.debug("Server response was {} to create fullfillment order number {}",
                    response.getStatusCode(), order.getId());
        } catch (HttpClientErrorException e) {
            logger.error("Unable to create fulfillment, status code " + e.getStatusCode() + " for order id " + order.getId() +
                    ", body:\n" + e.getResponseBodyAsString(), e);

            throw e;
        } catch (IOException e) {
            logger.error("Error logging body of JSON message", e);
        }
    }
}
