package com.fenrircyn.vpager.security.filters;

import com.fenrircyn.vpager.security.providers.ShopifyApiAuthenticationProvider;
import com.fenrircyn.vpager.security.token.ShopifyAuthenticationToken;
import com.fenrircyn.vpager.security.token.ShopifyProxyAuthenticationToken;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UriTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        super("/services/shopify**");
    }

    @PostConstruct
    public void init() {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) req);
        super.doFilter(requestWrapper, res, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication isValid = null;
        Map<String, String[]> parameterMap = request.getParameterMap();

        logger.debug("Filtering inbound request to {} for Shopify API.  Params: {}", request.getRequestURI(), prettyPrintParams(parameterMap));

        if(parameterMap.keySet().stream().anyMatch(Sets.newHashSet("customer_id", "hash", "email", "path_prefix",
                "shop", "timestamp", "signature")::contains)) {
            isValid = attemptAuthenticationOfProxyRequest(request, response);
        } else {
            isValid = attemptAuthenticationOfWebhook(request, response);
        }

        return isValid;
    }

    private String prettyPrintParams(Map<String, String[]> parameterMap) {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,String[]> mapEntry : parameterMap.entrySet()) {
            sb.append(mapEntry.getKey()).append("=");
            String delim = "";
            for(String s: mapEntry.getValue()) {
                sb.append(s).append(delim);
                delim = ",";
            }
        }

        return sb.toString();
    }

    public Authentication attemptAuthenticationOfWebhook(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String shopDomain = request.getHeader("X-Shopify-Shop-Domain");
        String hmacSig = request.getHeader("X-Shopify-Hmac-Sha256");
        String requestBody = null;

        try(Scanner scanner = new Scanner(request.getInputStream(), StandardCharsets.UTF_8.name())) {
            requestBody = scanner.useDelimiter("\\A").next();
        }

        return authenticationProvider.authenticate(new ShopifyAuthenticationToken(shopDomain, hmacSig, requestBody));
    }

    private Authentication attemptAuthenticationOfProxyRequest(HttpServletRequest request, HttpServletResponse response) {
        ShopifyProxyAuthenticationToken shopifyProxyAuthenticationToken = null;

        long customerId = getCustomerId(request.getRequestURI());

        try {
            String hash = request.getParameter("hash");
            String shopEmail = request.getParameter("email");
            String pathPrefix = request.getParameter("path_prefix"); //Shopify included automatically
            String shop = request.getParameter("shop"); //Shopify included automatically
            long epochTime = Long.parseLong(request.getParameter("timestamp")); //Shopify included automatically
            String hmac = request.getParameter("signature"); //Shopify included automatically

            String combinedStuff =
                    "customer_id=" + customerId +
                            "email=" + shopEmail +
                            "hash=" + hash +
                            "path_prefix=" + pathPrefix +
                            "shop=" + shop +
                            "timestamp=" + epochTime;
            logger.debug("Shopify sent signature for validation: " + hmac);

            shopifyProxyAuthenticationToken = new ShopifyProxyAuthenticationToken(shop, hmac, combinedStuff,
                    shopEmail, customerId, epochTime, pathPrefix, hash);

            authenticationProvider.authenticate(shopifyProxyAuthenticationToken);
        } catch (NumberFormatException e) {
            logger.error("Either customer or timestamp was not a valid number in URL {}, customer: {}, timestamp {}",
                    request.getRequestURI(),
                    customerId,
                    request.getParameter("timestamp"));
        }

        return shopifyProxyAuthenticationToken;
    }

    private long getCustomerId(String requestURI) {
        Pattern merchantIdPattern = Pattern.compile("/merchant/([0-9]+)/");
        Matcher matcher = merchantIdPattern.matcher(requestURI);

        String merchantString = matcher.group();

        return Long.getLong(merchantString);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURI().contains("/services/shopify");
    }

    @Override
    public void destroy() {

    }
}
