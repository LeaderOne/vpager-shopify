package com.fenrircyn.vpager.filters;

import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by markelba on 2/4/17.
 * This appears to be the only way possible to validate the code against Shopify's guidelines on redirect.  Strange.
 */
//TODO: Change this to an AuthorizationCodeAccessTokenProvider to validate params on redirect (see bookmark 1)
public class ShopifyAuthCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ShopifyValidator validator;

    public ShopifyAuthCodeAccessTokenProvider(ShopifyValidator validator) {
        super();
        this.validator = validator;
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, AccessTokenRequest tokenRequest)
            throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException,
            OAuth2AccessDeniedException {
        OAuth2AccessToken token = super.obtainAccessToken(details, tokenRequest);

        logger.debug("Validating auth code and parameters for {}", tokenRequest.getCurrentUri());
        boolean valid = false;

        try {
            String code = Iterables.getFirst(tokenRequest.get("code"), "BAD");
            String shop = Iterables.getFirst(tokenRequest.get("shop"), "BAD"); //Shopify included automatically
            String state = Iterables.getFirst(tokenRequest.get("state"), "BAD");
            long epochTime = Long.parseLong(Iterables.getFirst(tokenRequest.get("timestamp"), "-1")); //Shopify included automatically
            String hmac = Iterables.getFirst(tokenRequest.get("hmac"), "BAD"); //Shopify included automatically

            String combinedStuff =
                    "code=" + code +
                            "&shop=" + shop +
                            "&state=" + state +
                            "&timestamp=" + epochTime;
            logger.debug("Shopify sent signature for validation.  Merchant will NOT be validated since this is the first login. Signature: {}", hmac);

            valid = validator.validateHexEncodedNoMerchant(shop, hmac, combinedStuff);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            logger.error("An error occurred trying to validate the incoming API call...", e);
        } catch (NumberFormatException e) {
            logger.error("Either code, shop or timestamp was not valid: " +
                    tokenRequest.get("code") + ", shop: " +
                    tokenRequest.get("shop") + ", timestamp: " +
                    tokenRequest.get("timestamp"));
        }

        if(valid) {
            return token;
        } else {
            throw new AccessDeniedException("Shopify validation failed for request!");
        }
    }
}
