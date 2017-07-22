package com.fenrircyn.vpager;

import com.fenrircyn.vpager.filters.*;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

@Configuration
@EnableOAuth2Client
public class VPagerConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private OAuth2ClientContext oauth2ClientContext;

    @Resource
    private ShopifyValidator validator;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OAuth2RestTemplate shopifyRestTemplate = shopifyRestTemplate(validator);
        Filter shopifySsoFilter = ssoFilter(shopifyRestTemplate, shopifyUserTokenServices(shopifyRestTemplate));

        http.antMatcher("/**")
                .authorizeRequests().antMatchers("/install**")
                    .permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/install"))
                .and().logout().logoutSuccessUrl("/logout").permitAll()
                .and().csrf().disable()
                .addFilterBefore(shopifySsoFilter, BasicAuthenticationFilter.class);
    }


    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-10);
        return registration;
    }

    @Bean
    public OAuth2RestTemplate shopifyRestTemplate(ShopifyValidator validator) {
        OAuth2RestTemplate shopifyRestTemplate = new OAuth2RestTemplate(shopify(), oauth2ClientContext);

        ShopifyAuthCodeAccessTokenProvider provider = new ShopifyAuthCodeAccessTokenProvider(validator);
        shopifyRestTemplate.setAccessTokenProvider(provider);

        return shopifyRestTemplate;
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
        return userMap -> {
            Map<String,String> map = (Map<String, String>) userMap.get("shop");
            String url = map.get("email");

            if(url.contains("zealcon")) {
                return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
            }
            throw new BadCredentialsException("Not in Zealcon organization");
        };

    }

    @Bean
    public UserInfoTokenServices shopifyUserTokenServices(OAuth2RestTemplate shopifyRestTemplate) {
        UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(shopifyResource().getUserInfoUri(), shopify().getClientId());
        userInfoTokenServices.setRestTemplate(shopifyRestTemplate);
        userInfoTokenServices.setAuthoritiesExtractor(authoritiesExtractor(shopifyRestTemplate));
        userInfoTokenServices.setPrincipalExtractor(new ShopifyPrincipalExtractor());

        return userInfoTokenServices;
    }


    private Filter ssoFilter(OAuth2RestTemplate shopifyRestTemplate, UserInfoTokenServices shopifyUserTokenServices) {
        OAuth2ClientAuthenticationProcessingFilter vpagerFilter = new OAuth2ClientAuthenticationProcessingFilter(
                "/install");

        vpagerFilter.setRestTemplate(shopifyRestTemplate);

        vpagerFilter.setTokenServices(shopifyUserTokenServices);
        vpagerFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/shopify/user/console"));

        return vpagerFilter;
    }

    @Bean
    public FilterRegistrationBean requestDumperFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter requestDumperFilter = new RequestDumperFilter();
        registration.setFilter(requestDumperFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean shopifyApiValidationFilter(ShopifyAPIValidationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(filter);
        registration.addUrlPatterns("/shopify/webhooks**");

        return registration;
    }

//    @Bean
    public FilterRegistrationBean shopifyProxyFilter(ShopifyValidator validator) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter proxyFilter = new ShopifyProxyFilter(validator);

        registration.setFilter(proxyFilter);

        return registration;
    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public AuthorizationCodeResourceDetails shopify() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.resource")
    public ResourceServerProperties shopifyResource() {
        return new ResourceServerProperties();
    }
}
