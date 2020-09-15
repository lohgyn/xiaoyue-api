package com.line.xiaoyue.config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.line.xiaoyue.config.resolver.CustomAuthorizationRequestResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private AppConfig appConfig;

    private final Map<String, Object> additionalParameters = new LinkedHashMap<>();

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        this.additionalParameters.put("bot_prompt", "normal");

        http.authorizeRequests(a -> {
            a.antMatchers("/**").permitAll().antMatchers("/api/**").authenticated();
        }).exceptionHandling(e -> {
            e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        }).oauth2Login(oauth2 -> {
            final String authorizationRequestBaseUri = appConfig.getOauth2Uri() + "/authorization";
            oauth2.authorizationEndpoint()
                    .authorizationRequestResolver(new CustomAuthorizationRequestResolver(
                            this.clientRegistrationRepository, this.additionalParameters, authorizationRequestBaseUri))
                    .baseUri(authorizationRequestBaseUri).and().loginPage(appConfig.getOauth2Uri())
                    .loginProcessingUrl(appConfig.getOauth2Uri() + "/code/*")
                    .failureUrl(appConfig.getOauth2Uri() + "/fail").permitAll();

        }).logout(logout -> {
            logout.logoutUrl(appConfig.getOauth2Uri() + "/logout")
                    .logoutSuccessUrl(appConfig.getOauth2Uri() + "/logout/success").clearAuthentication(true)
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();
        }).cors().and().csrf().ignoringAntMatchers(appConfig.getOauth2Uri() + "/logout");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(appConfig.getFrontEndUri()));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("GET");
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    WebClient webClient(final ClientRegistrationRepository clientRegistrations,
            final OAuth2AuthorizedClientRepository authorizedClients) {
        final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrations, authorizedClients);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
    }

}
