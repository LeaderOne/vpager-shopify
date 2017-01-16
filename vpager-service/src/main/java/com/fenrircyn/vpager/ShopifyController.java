package com.fenrircyn.vpager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.filters.ShopifyValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by markelba on 12/11/16.
 */
@RestController
public class ShopifyController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ShopifyValidator validator;

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

        if (validator.validateBase64Encoded(shopDomain, hmacSig, postbody)) {
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

    @RequestMapping("/shopify")
    public String getHelloWorld() {
        return "Hello!";
    }

    //Note: combined parameters must be alphabetical and use hex encoding instead of base 64 signatures!
    @RequestMapping(value = "/shopify/mypagers", method = RequestMethod.GET)
    public ResponseEntity<List<Merchant>> getMyPagers(
            @RequestParam("customer_id") long customerId,
            @RequestParam("hash") String hash,
            @RequestParam("email") String email,
            @RequestParam("path_prefix") String pathPrefix, //Shopify included automatically
            @RequestParam("shop") String shop, //Shopify included automatically
            @RequestParam("timestamp") long epochTime, //Shopify included automatically
            @RequestParam("signature") String hmac //Shopify included automatically
    ) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String combinedStuff =
                "customer_id=" + customerId +
                        "email=" + email +
                        "hash=" + hash +
                        "path_prefix=" + pathPrefix +
                        "shop=" + shop +
                        "timestamp=" + epochTime;

        logger.debug("Shopify sent signature: " + hmac);

        ResponseEntity<List<Merchant>> response;

        if (validator.validateHexEncoded(shop, hmac, combinedStuff)) {
            List<Merchant> merchants = merchantBusiness.getMerchantsByEmail(email);

            response = ResponseEntity.ok(merchants);
        } else {
            logger.warn("Failing merchant list request due to invalid Shopify message");
            response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return response;
    }
}
