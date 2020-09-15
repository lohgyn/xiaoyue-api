package com.line.xiaoyue.config.resolver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final String DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

    private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;
    private final Map<String, Object> additionalParameters;

    public CustomAuthorizationRequestResolver(final ClientRegistrationRepository clientRegistrationRepository) {

        this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, this.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        this.additionalParameters = null;
    }

    public CustomAuthorizationRequestResolver(final ClientRegistrationRepository clientRegistrationRepository,
            final Map<String, Object> additionalParameters) {

        this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, this.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        this.additionalParameters = additionalParameters;
    }

    public CustomAuthorizationRequestResolver(final ClientRegistrationRepository clientRegistrationRepository,
            final Map<String, Object> additionalParameters, final String authorizationRequestBaseUri) {

        this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, authorizationRequestBaseUri);
        this.additionalParameters = additionalParameters;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(final HttpServletRequest request) {
        final OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver
                .resolve(request);

        return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest) : null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(final HttpServletRequest request, final String clientRegistrationId) {

        final OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver
                .resolve(request, clientRegistrationId);

        return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest) : null;
    }

    private OAuth2AuthorizationRequest customAuthorizationRequest(
            final OAuth2AuthorizationRequest authorizationRequest) {

        if (additionalParameters != null) {
            return OAuth2AuthorizationRequest.from(authorizationRequest).additionalParameters(additionalParameters)
                    .build();
        } else {
            return authorizationRequest;
        }
    }
}