package com.fenrircyn.vpager.security.validation;

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
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

@Component
public class ShopifyValidator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${security.oauth2.client.clientSecret}")
    private String shopifySharedSecret;

    @Resource
    private MerchantRepository merchantRepository;

    private static Pattern shopifyFilter = Pattern.compile("[0-9a-zA-Z\\-]+\\.myshopify\\.com");

    /**
     * Validates that the given request is authentic by converting the body to base 64 encoding then validating the HMAC signature.
     * This is used by webhook requests.
     *
     * @see <a href="https://help.shopify.com/api/getting-started/webhooks">Shopify Webhook documentation</a>
     * @param shopifyShopUrl the shop requesting webhook access
     * @param hmacSig64 the HMAC signature, in base 64 hex encoding
     * @param body the body of the request
     * @return true if the request can be confirmed to come from Shopify, false otherwise.
     * @throws IOException for encryption errors
     * @throws InvalidKeyException for encryption errors
     * @throws NoSuchAlgorithmException for encryption errors.
     */
    public boolean validateHmac64(String shopifyShopUrl, String hmacSig64, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);

        if (merchant != null && isValidHmac(body, hmacSig64, shopifyShopUrl,
                DatatypeConverter::parseBase64Binary, DatatypeConverter::printBase64Binary)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates that the given request is authentic by converting the body to hex and then validating the HMAC signature.
     * This is used by proxy requests.
     *
     * @see <a href="https://help.shopify.com/api/tutorials/application-proxies">Shopify Proxy documentation</a>
     * @param shopifyShopUrl the shop requesting proxy access
     * @param hmacSigHex the HMAC signature, in hex encoding
     * @param body the body of the request
     * @return true if the request can be confirmed to come from Shopify, false otherwise.
     * @throws IOException for encryption errors
     * @throws InvalidKeyException for encryption errors
     * @throws NoSuchAlgorithmException for encryption errors.
     */
    public boolean validateHexEncoded(String shopifyShopUrl, String hmacSigHex, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Merchant merchant = getRegisteredMerchant(shopifyShopUrl);
        logger.debug("In validateHexEncoded, hmac is {}", hmacSigHex);

        return validateHexEncodedNoMerchant(merchant.getShopifyShopUrl(), hmacSigHex, body);
    }

    private boolean isValidHmac(String postbody, String hmacSig, String merchantUrl,
                                Function<String, byte[]> conversionFunction,
                                Function<byte[], String> reverseConversionFunction)
            throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        byte[] key = shopifySharedSecret.getBytes();
        byte[] msgBytes = postbody.getBytes("UTF-8");

        SecretKey macKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);

        byte[] computedDigest = mac.doFinal(msgBytes);
        byte[] hmacSigBytes = conversionFunction.apply(hmacSig);

        if (Arrays.equals(computedDigest, hmacSigBytes)) {
            logger.debug("Request has passed HMAC validation for merchant {}", merchantUrl);
            return true;
        } else {
            String computedDigestString = reverseConversionFunction.apply(computedDigest);

            logger.warn("HMAC filter did NOT pass validation from address merchant {}, hmacSig {}, computed digest {}. postbody:\n{}",
                    merchantUrl, hmacSig, computedDigestString, postbody);

            return false;
        }
    }

    public boolean validateHexEncodedNoMerchant(String shopifyShopUrl, String hmacSigHex, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        logger.debug("In validateHexEncoded, hmac is {}", hmacSigHex);

        if (isValidHostname(shopifyShopUrl) && isValidHmac(body, hmacSigHex, shopifyShopUrl,
                DatatypeConverter::parseHexBinary, DatatypeConverter::printHexBinary)) {
            logger.debug("Request has passed validation.");

            return true;
        } else {
            return false;
        }
    }

    private static boolean isValidHostname(String shopifyShopUrl) {
        final Logger logger = LoggerFactory.getLogger(ShopifyValidator.class);

        logger.debug("Checking for valid hostname.");

        if(shopifyFilter.matcher(shopifyShopUrl).matches()) {
            logger.debug("Hostname filter has passed validation.");
            return true;
        } else {
            logger.warn("Hostname filter did NOT pass validation.");
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
