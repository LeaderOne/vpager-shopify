package com.fenrircyn.vpager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.entities.ShopifyWebhookContainer;
import com.fenrircyn.vpager.entities.ShopifyWebhookRequest;
import com.fenrircyn.vpager.entities.Ticket;
import com.fenrircyn.vpager.filters.ShopifyWebhookValidator;
import com.fenrircyn.vpager.repos.MerchantRepository;
import com.fenrircyn.vpager.repos.TicketRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
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
    private MerchantRepository merchantRepository;

    @Resource
    private TicketRepository ticketRepository;

    @Resource
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/shopify", method = RequestMethod.GET)
    public ResponseEntity<Merchant> testApp() {
        System.out.println("Incoming get request");

        return ResponseEntity.ok(new Merchant());
    }

    @RequestMapping(value = "/shopify", method = RequestMethod.POST)
    public ResponseEntity<Ticket> createOrder(@RequestBody String postbody,
                                              @RequestHeader(name = "X-Shopify-Shop-Domain") String shopDomain,
                                              @RequestHeader(name = "X-Shopify-Hmac-Sha256") String hmacSig)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (validator.validate(shopDomain, hmacSig, postbody)) {
            Order order = objectMapper.readValue(postbody, Order.class);

            logger.debug("Inserting new order for %d", order.getId());
            Merchant merchant = merchantRepository.getByShopifyShopUrl(shopDomain);

            if (merchant == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                logger.debug("Inserting new order %d for merchant %s", order.getId(), merchant.getShopifyShopUrl());
                Ticket ticket = new Ticket();

                ticket.setMerchant(merchant);
                ticket.setShopifyOrderId(order.getId());

                ticket = ticketRepository.save(ticket);

                return ResponseEntity.ok(ticket);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
