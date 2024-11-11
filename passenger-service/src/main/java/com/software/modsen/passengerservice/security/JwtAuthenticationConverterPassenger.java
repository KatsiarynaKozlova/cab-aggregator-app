package com.software.modsen.passengerservice.security;

import com.software.modsen.passengerservice.util.SecurityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class JwtAuthenticationConverterPassenger implements Converter<Jwt, Collection<GrantedAuthority>>{
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, List<String>> resourceAccess = jwt.getClaim(SecurityConstants.REALM_ACCESS);
        List<String> resourceRoles;

        if (resourceAccess == null || (resourceRoles = resourceAccess.get(SecurityConstants.ROLES)) == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority(SecurityConstants.PREFIX_ROLE + role))
                .collect(Collectors.toList());
    }
}
