package com.fenrircyn.vpager.security.providers;

import com.fenrircyn.vpager.security.token.ShopifyAuthenticationToken;
import com.fenrircyn.vpager.security.token.ShopifyProxyAuthenticationToken;
import com.fenrircyn.vpager.security.validation.ShopifyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class ShopifyApiAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ShopifyValidator shopifyValidator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication decision = null;

        if (authentication instanceof ShopifyProxyAuthenticationToken) {
            logger.debug("Authentication instance is a proxy request");
            decision = authenticateProxyRequest(authentication);
        } else {
            logger.debug("Authentication instance is a webhook request");
            decision = authenticateWebhookRequest(authentication);
        }


        return decision;
    }


    private Authentication authenticateWebhookRequest(Authentication authentication) {
        Authentication decision;
        ShopifyAuthenticationToken shopifyAuthenticationToken = (ShopifyAuthenticationToken) authentication;

        try {
            if(shopifyValidator.validateHmac64(shopifyAuthenticationToken.getShopifyShopUrl(), shopifyAuthenticationToken.getHmac(), shopifyAuthenticationToken.getBody())) {
                authentication.setAuthenticated(true);
                decision = authentication;
            } else {
                throw new BadCredentialsException("Could not verify that this is a shopify customer!");
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new InsufficientAuthenticationException("Could not initialize Shopify authentication provider", e);
        }
        return decision;
    }


    private Authentication authenticateProxyRequest(Authentication authentication) {
        Authentication decision;
        ShopifyProxyAuthenticationToken shopifyAuthenticationToken = (ShopifyProxyAuthenticationToken) authentication;

        try {
            if(shopifyValidator.validateHexEncoded(shopifyAuthenticationToken.getShopifyShopUrl(),
                    shopifyAuthenticationToken.getHmac(), shopifyAuthenticationToken.getBody())) {
                authentication.setAuthenticated(true);
                decision = authentication;
            } else {
                throw new BadCredentialsException("Could not verify that this is a shopify customer!");
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new InsufficientAuthenticationException("Could not initialize Shopify authentication provider", e);
        }
        return decision;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isInstance(ShopifyAuthenticationToken.class);
    }
}
