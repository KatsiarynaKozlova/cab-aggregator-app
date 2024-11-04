package com.software.modsen.authservice.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.software.modsen.authservice.util.KeycloakConstants.PREFIX_ROLE;
import static com.software.modsen.authservice.util.KeycloakConstants.REALM_ACCESS;
import static com.software.modsen.authservice.util.KeycloakConstants.ROLES;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, roles);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, List<String>> resourceAccess = jwt.getClaim(REALM_ACCESS);
        List<String> resourceRoles;

        if (resourceAccess == null || (resourceRoles = resourceAccess.get(ROLES)) == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority(PREFIX_ROLE + role))
                .collect(Collectors.toList());
    }
}
