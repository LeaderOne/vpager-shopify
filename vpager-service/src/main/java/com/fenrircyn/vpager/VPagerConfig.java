package com.fenrircyn.vpager;

import com.fenrircyn.vpager.filters.ShopifyInstallVerificationFilter;
import com.fenrircyn.vpager.filters.ShopifyProxyFilter;
import com.fenrircyn.vpager.filters.ShopifyValidator;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Configuration
@EnableOAuth2Client
public class VPagerConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    public RestTemplate shopifyApiRestTemplate() {
        return new RestTemplate();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests().antMatchers("/install**", "/shopify**", "/shopify/**").permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/install")).and().logout()
                .logoutSuccessUrl("/").permitAll()
                .and().csrf().disable()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }


    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-10);
        return registration;
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter vpagerFilter = new OAuth2ClientAuthenticationProcessingFilter(
                "/install");

        OAuth2RestTemplate shopifyTemplate = new OAuth2RestTemplate(shopify(), oauth2ClientContext);
        vpagerFilter.setRestTemplate(shopifyTemplate);

        UserInfoTokenServices tokenServices = new UserInfoTokenServices(shopifyResource().getUserInfoUri(), shopify().getClientId());
        tokenServices.setRestTemplate(shopifyTemplate);
        vpagerFilter.setTokenServices(tokenServices);

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

//    @Bean
    public FilterRegistrationBean shopifyProxyFilter(ShopifyValidator validator) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter proxyFilter = new ShopifyProxyFilter(validator);

        registration.setFilter(proxyFilter);

        return registration;
    }

    @Bean
    public ShopifyInstallVerificationFilter shopifyLoginFilter(ShopifyValidator shopifyValidator) {
        return new ShopifyInstallVerificationFilter(shopifyValidator);
    }

    @Bean
    public FilterRegistrationBean shopifyLoginFilterRegistration(ShopifyInstallVerificationFilter shopifyInstallVerificationFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(shopifyInstallVerificationFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(-100);

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
