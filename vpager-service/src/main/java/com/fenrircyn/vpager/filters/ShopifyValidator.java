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
import java.util.function.Function;

@Component
public class ShopifyValidator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${security.oauth2.client.clientSecret}")
    private String shopifySharedSecret;

    @Resource
    private MerchantRepository merchantRepository;

    public boolean validateBase64Encoded(String shopifyShopUrl, String hmacSig64, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);

        if (merchant != null && isValidHmac(body, hmacSig64, merchant,
                DatatypeConverter::parseBase64Binary, DatatypeConverter::printBase64Binary)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    public boolean validateHexEncoded(String shopifyShopUrl, String hmacSigHex, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);
        logger.debug("In validateHexEncoded, hmac is {}", hmacSigHex);

        if (merchant != null && isValidHmac(body, hmacSigHex, merchant,
                DatatypeConverter::parseHexBinary, DatatypeConverter::printHexBinary)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    private boolean isValidHmac(String postbody, String hmacSig, Merchant merchant,
                                Function<String,byte[]> conversionFunction,
                                Function<byte[],String> reverseConversionFunction)
            throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        byte[] key = shopifySharedSecret.getBytes();
        byte[] msgBytes = postbody.getBytes("UTF-8");

        SecretKey macKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);

        byte[] computedDigest = mac.doFinal(msgBytes);
        byte[] hmacSigBytes = conversionFunction.apply(hmacSig);

        if (Arrays.equals(computedDigest, hmacSigBytes)) {
            logger.debug("Request has passed HMAC validation for merchant {}", merchant.toString());
            return true;
        } else {
            String computedDigestString = reverseConversionFunction.apply(computedDigest);

            logger.warn("HMAC filter did NOT pass validation from address merchant {}, hmacSig {}, computed digest {}. postbody:\n{}",
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
