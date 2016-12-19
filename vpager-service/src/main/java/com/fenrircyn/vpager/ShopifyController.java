package com.fenrircyn.vpager;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.entities.ShopifyWebhookContainer;
import com.fenrircyn.vpager.entities.ShopifyWebhookRequest;
import com.fenrircyn.vpager.entities.Ticket;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.apache.commons.codec.binary.Base64;
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
    @RequestMapping(value = "/shopify", method = RequestMethod.GET)
    public ResponseEntity<Merchant> testApp() {
        System.out.println("Incoming get request");

        return ResponseEntity.ok(new Merchant());
    }

    @RequestMapping(value = "/shopify", method = RequestMethod.POST)
    public ResponseEntity<Merchant> testResponse(@RequestBody String postbody,
                                                 @RequestHeader(name = "X-Shopify-Shop-Domain") String shopDomain,
                                                 @RequestHeader(name = "X-Shopify-Hmac-Sha256") String hmacSig) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        byte[] key = "577182d2f32909da7dcb5cf78a02f6b7c3a66cacd86ee75cd3d3be58e719f8e9".getBytes();
        byte[] hmacSigBytes = DatatypeConverter.parseBase64Binary(hmacSig);
        byte[] msgBytes = postbody.getBytes();

        SecretKey macKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);

        byte[] computedDigest = mac.doFinal(msgBytes);

        if(Arrays.equals(computedDigest, hmacSigBytes)) {
            System.out.println("HMAC has passed validation.");
        } else {
            System.err.println("HMAC did NOT pass validation.");
        }

        System.out.println("Incoming request from " + shopDomain + " with signature " + hmacSig + ":\n" + postbody);

        return ResponseEntity.ok(new Merchant());
    }
}
