package com.fenrircyn.vpager.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by markelba on 1/16/17.
 */
public class ShopifyProxyFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ShopifyValidator validator;

    public ShopifyProxyFilter(ShopifyValidator validator) {
        this.validator = validator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        logger.debug("Filtering inbound request for Shopify API.  Params: {}", Arrays.toString(parameterMap.entrySet().toArray()));

        if(parameterMap.keySet().addAll(Arrays.asList("customer_id", "hash", "email", "path_prefix",
                "shop", "timestamp", "signature"))) {
            if(validateSignedRequest(request)) {
                logger.debug("Validation succeeded for customer {}", parameterMap.get("customer_id")[0]);
                chain.doFilter(request, response);
            } else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request could not be verified!");
            }
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request did not contain the required parameters!");

            logger.warn("Request to shopify filter was missing required parameters: customerId {}, hash {}, email {}, pathPrefix {}, shop {}, epochTime {}, hmac {}",
                    request.getParameter("customer_id"), request.getParameter("hash"), request.getParameter("email"),
                    request.getParameter("pathPrefix"), request.getParameter("shop"), request.getParameter("epochTime"),
                    request.getParameter("hmac"));
        }
    }

    private boolean validateSignedRequest(ServletRequest request) {
        boolean valid = false;
        try {
            long customerId = Long.parseLong(request.getParameter("customer_id"));
            String hash = request.getParameter("hash");
            String email = request.getParameter("email");
            String pathPrefix = request.getParameter("path_prefix"); //Shopify included automatically
            String shop = request.getParameter("shop"); //Shopify included automatically
            long epochTime = Long.parseLong(request.getParameter("timestamp")); //Shopify included automatically
            String hmac = request.getParameter("signature"); //Shopify included automatically

            String combinedStuff =
                    "customer_id=" + customerId +
                            "email=" + email +
                            "hash=" + hash +
                            "path_prefix=" + pathPrefix +
                            "shop=" + shop +
                            "timestamp=" + epochTime;
            logger.debug("Shopify sent signature for validation: " + hmac);

            valid = validator.validateHexEncoded(shop, hmac, combinedStuff);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            logger.error("An error occurred trying to validate the incoming API call...", e);
        } catch (NumberFormatException e) {
            logger.error("Either customer or timestamp was not a valid number, customer: " +
                    request.getParameter("customer_id") + ", timestamp: " +
                    request.getParameter("timestamp"));
        }

        return valid;
    }

    @Override
    public void destroy() {

    }
}
