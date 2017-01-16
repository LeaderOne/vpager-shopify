package com.fenrircyn.vpager.filters;

import com.fenrircyn.vpager.entities.Merchant;
import com.fenrircyn.vpager.repos.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

@Component
public class ShopifyValidator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${shopify.api.shared.secret}")
    private String shopifySharedSecret;

    @Resource
    private MerchantRepository merchantRepository;

    public boolean validateBase64Encoded(String shopifyShopUrl, String hmacSig, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);

        if (merchant != null && isValidBase64Hmac(body, hmacSig, merchant)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    public boolean validateHexEncoded(String shopifyShopUrl, String hmacSigHex, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);
        logger.debug("In validateHexEncoded, hmac is {}", hmacSigHex);

        if (merchant != null && isValidHexHmac(body, hmacSigHex, merchant)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    private boolean isValidHexHmac(String postbody, String hmacSigHex, Merchant merchant) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        byte[] key = shopifySharedSecret.getBytes();
        byte[] msgBytes = postbody.getBytes("UTF-8");

        SecretKey macKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);

        byte[] computedDigest = mac.doFinal(msgBytes);

        byte[] hmacSigBytes = DatatypeConverter.parseHexBinary(hmacSigHex);

        if (Arrays.equals(computedDigest, hmacSigBytes)) {
            logger.debug("Request has passed HMAC hex validation for merchant {}", merchant.toString());
            return true;
        } else {
            String computedDigestString = DatatypeConverter.printHexBinary(computedDigest);

            logger.warn("HMAC hex filter did NOT pass validation from address merchant {}, hmacSig {}, computed digest {}. postbody:\n{}",
                    merchant, hmacSigHex, computedDigestString, postbody);

            return false;
        }
    }

    private boolean isValidBase64Hmac(String postbody, String hmacSig, Merchant merchant) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        byte[] key = shopifySharedSecret.getBytes();
        byte[] hmacSigBytes = DatatypeConverter.parseBase64Binary(hmacSig);
        byte[] msgBytes = postbody.getBytes("UTF-8");

        SecretKey macKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);

        byte[] computedDigest = mac.doFinal(msgBytes);

        if (Arrays.equals(computedDigest, hmacSigBytes)) {
            logger.debug("Request has passed HMAC Base 64 validation for merchant {}", merchant.toString());
            return true;
        } else {
            String computedDigestString = DatatypeConverter.printBase64Binary(computedDigest);

            logger.warn("HMAC Base 64 filter did NOT pass validation from address merchant {}, hmacSig {}, computed digest {}. postbody:\n{}",
                    merchant, hmacSig, computedDigestString, postbody);

            return false;
        }
    }

    private Merchant getRegisteredMerchant(String shopifyShopUrl) throws IOException {
        Merchant merchant = merchantRepository.getByShopifyShopUrl(shopifyShopUrl);

        if (merchant == null) {
            logger.warn("Merchant filter did NOT pass validation, URL was: " + shopifyShopUrl);
        } else {
            logger.info("Merchant filter passed validation for URL " + shopifyShopUrl);
        }

        return merchant;
    }
}
