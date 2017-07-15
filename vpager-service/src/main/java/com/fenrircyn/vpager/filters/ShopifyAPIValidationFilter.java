package com.fenrircyn.vpager.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by markelba on 7/15/17.
 */
@Component
public class ShopifyAPIValidationFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ShopifyValidator shopifyValidator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);

        String shopDomain = requestWrapper.getHeader("X-Shopify-Shop-Domain");
        String hmacSig = requestWrapper.getHeader("X-Shopify-Hmac-Sha256");
        String requestBody = null;

        try(Scanner scanner = new Scanner(requestWrapper.getInputStream(), StandardCharsets.UTF_8.name())) {
            requestBody = scanner.useDelimiter("\\A").next();
        }

        try {
            if (shopifyValidator.validateBase64Encoded(shopDomain, hmacSig, requestBody)) {
                filterChain.doFilter(requestWrapper, httpServletResponse);
            } else {
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Shopify signature failed to validate!");
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
