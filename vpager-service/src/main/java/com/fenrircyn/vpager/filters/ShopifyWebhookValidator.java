package com.fenrircyn.vpager.filters;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;

@Component
public class ShopifyWebhookValidator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${shopify.api.shared.secret}")
    private String shopifySharedSecret;

    @Resource
    private MerchantRepository merchantRepository;

    public boolean validate(String shopifyShopUrl, String hmacSig, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);

        if (merchant != null && isValidHmac(body, hmacSig, merchant)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    private boolean isValidHmac(String postbody, String hmacSig, Merchant merchant) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        byte[] key = shopifySharedSecret.getBytes();
        byte[] hmacSigBytes = DatatypeConverter.parseBase64Binary(hmacSig);
        byte[] msgBytes = postbody.getBytes("UTF-8");

        SecretKey macKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);

        byte[] computedDigest = mac.doFinal(msgBytes);

        if (Arrays.equals(computedDigest, hmacSigBytes)) {
            logger.debug("Request has passed HMAC validation for merchant {}", merchant.toString());
            return true;
        } else {
            logger.warn("HMAC filter did NOT pass validation from address merchant {}", merchant);

            return false;
        }
    }

    private Merchant getRegisteredMerchant(String shopifyShopUrl) throws IOException {
        Merchant merchant = merchantRepository.getByShopifyShopUrl(shopifyShopUrl);

        if (merchant == null) {
            logger.warn("Merchant filter did NOT pass validation");
        } else {
            logger.info("Merchant filter passed validation");
        }

        return merchant;
    }
}
