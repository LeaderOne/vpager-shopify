package com.fenrircyn.vpager.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by markelba on 2/4/17.
 */
public class ShopifyLoginFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ShopifyValidator validator;

    public ShopifyLoginFilter(ShopifyValidator validator) {
        this.validator = validator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("/login was called");
        if(request.getParameterMap().containsKey("code")) {
            boolean valid = false;
            try {
                String code = request.getParameter("code");
                String shop = request.getParameter("shop"); //Shopify included automatically
                String state = request.getParameter("state");
                long epochTime = Long.parseLong(request.getParameter("timestamp")); //Shopify included automatically
                String hmac = request.getParameter("hmac"); //Shopify included automatically

                String combinedStuff =
                        "code=" + code +
                                "&shop=" + shop +
                                "&state=" + state +
                                "&timestamp=" + epochTime;
                logger.debug("Shopify sent signature for validation: " + hmac);

                valid = validator.validateHexEncoded(shop, hmac, combinedStuff);
            } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
                logger.error("An error occurred trying to validate the incoming API call...", e);
            } catch (NumberFormatException e) {
                logger.error("Either code, shop or timestamp was not valid: " +
                        request.getParameter("code") + ", shop: " +
                        request.getParameter("shop") + ", timestamp: " +
                        request.getParameter("timestamp"));
            }

            if(valid) {
                chain.doFilter(request, response);
            } else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                httpServletResponse.sendError(403, "Shopify validation failed.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
