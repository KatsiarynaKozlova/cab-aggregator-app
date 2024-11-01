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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        if (jwt.getClaim(REALM_ACCESS) != null) {
            Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
            ObjectMapper mapper = new ObjectMapper();
            List<String> keycloakRoles = mapper.convertValue(realmAccess.get(ROLES), new TypeReference<List<String>>() {});
            List<GrantedAuthority> roles = new ArrayList<>();

            for (String keyCloakRole : keycloakRoles) {
                roles.add(new SimpleGrantedAuthority(PREFIX_ROLE + keyCloakRole));
            }

            return roles;
        }
        return new ArrayList<>();
    }
}
