package com.fenrircyn.vpager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrircyn.vpager.business.MerchantBusiness;
import com.fenrircyn.vpager.dto.Order;
import com.fenrircyn.vpager.dto.Webhook;
import com.fenrircyn.vpager.entities.Merchant;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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
    private MerchantBusiness merchantBusiness;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private OAuth2RestTemplate authenticatedTemplate;

    @RequestMapping(value = "/shopify/install", method = RequestMethod.POST)
    public String installVPager(@RequestParam("shop") String shop //Shopify included automatically
     ) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Merchant merchant = merchantBusiness.createMerchantFromUrl(shop);

        Webhook webhook = new Webhook();

        webhook.setTopic("orders/create");
        webhook.setAddress("https://ecca17cf.ngrok.io/shopify");
        webhook.setFormat("json");

        String url = "https://" + shop + "/admin/webhooks.json";

        ResponseEntity<Webhook> responseEntity = authenticatedTemplate.postForEntity(url, webhook, Webhook.class);

        logger.debug("Received response entity, status " + responseEntity.getStatusCode() + " ID " + webhook.getId());

        return "S'all right!  Merchant ID is " + merchant.getId() + "!";
    }


    @RequestMapping(value = "/shopify/client", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Iterable<Merchant>> createVPagersFromOrder(@RequestBody String postbody)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Iterable<Merchant> merchants;

        ResponseEntity<Iterable<Merchant>> response;

        Order order = objectMapper.readValue(postbody, Order.class);

        logger.debug("Received order {} from email address {} (contact email {}) with {} line items", order.getId(), order.getEmail(), order.getContactEmail(), CollectionUtils.size(order.getLineItems()));

        merchants = merchantBusiness.createMerchantsFromOrder(order);

        response = ResponseEntity.ok(merchants);

        return response;
    }

    //Note: combined parameters must be alphabetical and use hex encoding instead of base 64 signatures!
    @RequestMapping(value = "/shopify/mypagers", method = RequestMethod.GET)
    public ResponseEntity<List<Merchant>> getMyPagers(
            @RequestParam("email") String email
    ) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<Merchant> merchants = merchantBusiness.getMerchantsByEmail(email);

        ResponseEntity<List<Merchant>> response = ResponseEntity.ok(merchants);

        return response;
    }
}
