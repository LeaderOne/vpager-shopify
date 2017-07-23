package com.fenrircyn.vpager.security.filters;

import com.fenrircyn.vpager.security.providers.ShopifyApiAuthenticationProvider;
import com.fenrircyn.vpager.security.token.ShopifyAuthenticationToken;
import com.fenrircyn.vpager.security.token.ShopifyWebhookAuthenticationToken;
import com.fenrircyn.vpager.security.validation.ShopifyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by markelba on 7/15/17.
 */
@Component
public class ShopifyAPIValidationFilter extends AbstractAuthenticationProcessingFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ShopifyApiAuthenticationProvider authenticationProvider;

    @Resource
    private AuthenticationManager authenticationManager;

    public ShopifyAPIValidationFilter() {
        super("/shopify");
    }

    @PostConstruct
    public void init() {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication isValid = null;
        Map<String, String[]> parameterMap = request.getParameterMap();

        logger.debug("Filtering inbound request for Shopify API.  Params: {}", Arrays.toString(parameterMap.entrySet().toArray()));

        if(parameterMap.keySet().addAll(Arrays.asList("customer_id", "hash", "email", "path_prefix",
                "shop", "timestamp", "signature"))) {
            isValid = attemptAuthenticationOfWebhookRequest(request, response);
        } else {
            isValid = attemptAuthenticationOfProxyRequest(request, response);
        }

        return isValid;
    }

    public Authentication attemptAuthenticationOfProxyRequest(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        String shopDomain = requestWrapper.getHeader("X-Shopify-Shop-Domain");
        String hmacSig = requestWrapper.getHeader("X-Shopify-Hmac-Sha256");
        String requestBody = null;
        long shopifyCustomerId = Long.parseLong(request.getParameter("customer_id"));
        String customerEmail = request.getParameter("email");

        try(Scanner scanner = new Scanner(requestWrapper.getInputStream(), StandardCharsets.UTF_8.name())) {
            requestBody = scanner.useDelimiter("\\A").next();
        }

        return authenticationProvider.authenticate(new ShopifyAuthenticationToken(shopDomain, customerEmail, hmacSig, requestBody, shopifyCustomerId));
    }

    private Authentication attemptAuthenticationOfWebhookRequest(HttpServletRequest request, HttpServletResponse response) {
        ShopifyWebhookAuthenticationToken shopifyWebhookAuthenticationToken = null;

        try {
            long customerId = Long.parseLong(request.getParameter("customer_id"));
            String hash = request.getParameter("hash");
            String customerEmail = request.getParameter("email");
            String pathPrefix = request.getParameter("path_prefix"); //Shopify included automatically
            String shop = request.getParameter("shop"); //Shopify included automatically
            long epochTime = Long.parseLong(request.getParameter("timestamp")); //Shopify included automatically
            String hmac = request.getParameter("signature"); //Shopify included automatically

            String combinedStuff =
                    "customer_id=" + customerId +
                            "email=" + customerEmail +
                            "hash=" + hash +
                            "path_prefix=" + pathPrefix +
                            "shop=" + shop +
                            "timestamp=" + epochTime;
            logger.debug("Shopify sent signature for validation: " + hmac);

            shopifyWebhookAuthenticationToken = new ShopifyWebhookAuthenticationToken(shop, customerEmail, hmac, combinedStuff, customerId, epochTime, pathPrefix, hash);

            authenticationProvider.authenticate(shopifyWebhookAuthenticationToken);
        } catch (NumberFormatException e) {
            logger.error("Either customer or timestamp was not a valid number, customer: " +
                    request.getParameter("customer_id") + ", timestamp: " +
                    request.getParameter("timestamp"));
        }

        return shopifyWebhookAuthenticationToken;
    }

    @Override
    public void destroy() {

    }
}
