package com.line.xiaoyue.config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import com.line.xiaoyue.config.resolver.CustomAuthorizationRequestResolver;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebSecurityConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    protected SecurityFilterChain filterChain(final HttpSecurity http,
            final ClientRegistrationRepository clientRegistrationRepository,
            final CorsConfigurationSource corsConfigurationSource) throws Exception {

        final Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put("bot_prompt", "normal");

        return http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/v1/public/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth2 -> {
                    final String authorizationRequestBaseUri = appConfig.getOauth2Uri() + "/authorization";
                    oauth2.authorizationEndpoint(config -> {
                        config.authorizationRequestResolver(new CustomAuthorizationRequestResolver(
                                clientRegistrationRepository, additionalParameters,
                                authorizationRequestBaseUri))
                                .baseUri(authorizationRequestBaseUri);
                    })
                            .loginPage(appConfig.getOauth2Uri())
                            .loginProcessingUrl(appConfig.getOauth2Uri() + "/code/*")
                            .failureUrl(appConfig.getOauth2Uri() + "/fail").permitAll();

                }).logout(logout -> logout
                        .logoutUrl(appConfig.getOauth2Uri() + "/logout")
                        .logoutSuccessUrl(appConfig.getOauth2Uri() + "/logout/success")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID").permitAll())
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/v1/public/line/bot/php/webhook"))
                .build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(Environment environment) {

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(appConfig.getFrontEndUri());

        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("dev"))) {
            log.info("add allowed origin: {}", appConfig.getFrontEndTestUri());
            configuration.addAllowedOrigin(appConfig.getFrontEndTestUri());
        }

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
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
