package com.fenrircyn.vpager.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by markelba on 2/4/17.
 */
//TODO: Wrap the OAuth filter or compose it so that it validates the parameters (without merchant validation) on first login.
//TODO: Find out why one gets dumped at / after login
public class ShopifyInstallVerificationFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ShopifyValidator validator;

    public ShopifyInstallVerificationFilter(ShopifyValidator validator) {
        this.validator = validator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestPath = ((HttpServletRequest) request).getRequestURL().toString();
        logger.debug("ShopifyInstallVerificationFilter was called for {}", requestPath);

        if(request.getParameterMap().containsKey("code")) {
            logger.debug("Validating auth code and parameters for {}", requestPath);
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
                logger.debug("Shopify sent signature for validation.  Merchant will NOT be validated since this is the first login. Signature: {}", hmac);

                valid = validator.validateHexEncodedNoMerchant(shop, hmac, combinedStuff);
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
            logger.debug("ShopifyInstallVerificationFilter could not find auth code to validate for {}", requestPath);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
