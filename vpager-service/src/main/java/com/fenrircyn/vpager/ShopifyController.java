package com.fenrircyn.vpager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.filters.ShopifyWebhookValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import javax.xml.ws.Response;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

/**
 * Created by markelba on 12/11/16.
 */
@RestController
public class ShopifyController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ShopifyWebhookValidator validator;

    @Resource
    private MerchantBusiness merchantBusiness;

    @Resource
    private ObjectMapper objectMapper;

    @Value("shopify.nonce")
    private String nonce;

    @RequestMapping(value = "/shopify", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Iterable<Merchant>> createVPagersFromOrder(@RequestBody String postbody,
                                                                     @RequestHeader(name = "X-Shopify-Shop-Domain") String shopDomain,
                                                                     @RequestHeader(name = "X-Shopify-Hmac-Sha256") String hmacSig)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Iterable<Merchant> merchants = null;

        ResponseEntity<Iterable<Merchant>> response;

        if (validator.validate(shopDomain, hmacSig, postbody)) {
            Order order = objectMapper.readValue(postbody, Order.class);

            logger.debug("Received order {} from email address {} (contact email {}) with {} line items", order.getId(), order.getEmail(), order.getContactEmail(), CollectionUtils.size(order.getLineItems()));

            merchants = merchantBusiness.createMerchantsFromOrder(order);

            response = ResponseEntity.ok(merchants);
        } else {
            logger.warn("Failing order due to invalid Shopify message");
            response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return response;
    }

    @RequestMapping("/shopify/install")
    public ResponseEntity<Merchant> installApp(@RequestParam("code") String authorizationCode,
                                               @RequestParam("hmac") String hmac,
                                               @RequestParam("timestamp") long timestamp,
                                               @RequestParam("shop") String hostname)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String verificationString = "code=" + authorizationCode + "&shop=" + hostname + "&timestamp=" + timestamp;

        if(validator.validate(hmac, verificationString)) {
            return ResponseEntity.ok(merchantBusiness.createMerchantFromUrl(hostname));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping("/shopify")
    public String getHelloWorld() {
        return "Hello!";
    }

    @RequestMapping(value = "/shopify/mypagers", method = RequestMethod.POST)
    public ResponseEntity<List<Merchant>> getMyPagers(
            @RequestParam("customer_id") long customerId,
            @RequestParam("hash") String hash,
            @RequestParam("email") String email,
            @RequestParam("path_prefix") String pathPrefix, //Shopify included automatically
            @RequestParam("shop") String shop, //Shopify included automatically
            @RequestParam("timestamp") long epochTime, //Shopify included automatically
            @RequestParam("signature") String hmac //Shopify included automatically
            )  throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String combinedStuff = "customer_id=" + customerId +
                               "hash=" + hash +
                               "email=" + email +
                                "path_prefix=" + pathPrefix +
                                "shop=" + shop +
                                "timestamp=" + epochTime;

        ResponseEntity<List<Merchant>> response;

        if(validator.validate(shop, hmac, combinedStuff)) {
            List<Merchant> merchants = merchantBusiness.getMerchantsByEmail(email);

            response = ResponseEntity.ok(merchants);
        } else {
            logger.warn("Failing merchant list request due to invalid Shopify message");
            response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return response;
    }
}
