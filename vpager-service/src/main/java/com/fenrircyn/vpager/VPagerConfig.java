package com.fenrircyn.vpager;

import com.fenrircyn.vpager.security.extractors.ShopifyAuthoritiesExtractor;
import com.fenrircyn.vpager.security.extractors.ShopifyShopOwnerPrincipalExtractor;
import com.fenrircyn.vpager.security.filters.ShopifyAPIValidationFilter;
import com.fenrircyn.vpager.security.providers.ShopifyAuthCodeAccessTokenProvider;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Configuration
@EnableOAuth2Client
public class VPagerConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private OAuth2ClientContext oauth2ClientContext;

    @Resource
    private ShopifyAPIValidationFilter validationFilter;

    @Resource
    private ShopifyAuthCodeAccessTokenProvider accessTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OAuth2RestTemplate shopifyRestTemplate = shopifyRestTemplate();
        Filter shopifySsoFilter = ssoFilter(shopifyRestTemplate, shopifyUserTokenServices(shopifyRestTemplate));

        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/install**")
                    .permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/install"))
                .and().logout().logoutSuccessUrl("/logout").permitAll()
                .and()
                .csrf().disable()
                .addFilterBefore(validationFilter, BasicAuthenticationFilter.class)
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
    public OAuth2RestTemplate shopifyRestTemplate() {
        OAuth2RestTemplate shopifyRestTemplate = new OAuth2RestTemplate(shopify(), oauth2ClientContext);

        shopifyRestTemplate.setAccessTokenProvider(accessTokenProvider);

        return shopifyRestTemplate;
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor() {
        return new ShopifyAuthoritiesExtractor();
    }

    @Bean
    public UserInfoTokenServices shopifyUserTokenServices(OAuth2RestTemplate shopifyRestTemplate) {
        UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(shopifyResource().getUserInfoUri(), shopify().getClientId());
        userInfoTokenServices.setRestTemplate(shopifyRestTemplate);
        userInfoTokenServices.setAuthoritiesExtractor(authoritiesExtractor());
        userInfoTokenServices.setPrincipalExtractor(ShopifyShopOwnerPrincipalExtractor.instance);

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
