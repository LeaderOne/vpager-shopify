package com.fenrircyn.vpager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.filters.ShopifyWebhookValidator;
import com.fenrircyn.vpager.repos.MerchantRepository;
import com.fenrircyn.vpager.repos.TicketRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    private ShopifyWebhookValidator validator;

    @Resource
    private MerchantBusiness merchantBusiness;

    @Resource
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/shopify", method = RequestMethod.POST)
    public ResponseEntity<List<Merchant>> createVPagersFromOrder(@RequestBody String postbody,
                                                           @RequestHeader(name = "X-Shopify-Shop-Domain") String shopDomain,
                                                           @RequestHeader(name = "X-Shopify-Hmac-Sha256") String hmacSig)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<Merchant> merchants = null;

        if (validator.validate(shopDomain, hmacSig, postbody)) {
            Order order = objectMapper.readValue(postbody, Order.class);

            logger.debug("Received order {} from email address {} (contact email {}) with {} line items", order.getId(), order.getEmail(), order.getContactEmail(), CollectionUtils.size(order.getLineItems()));

            merchants = merchantBusiness.createMerchantsFromOrder(order);

        } else {
            logger.warn("Failing order due to invalid Shopify message");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(merchants);
    }
}
