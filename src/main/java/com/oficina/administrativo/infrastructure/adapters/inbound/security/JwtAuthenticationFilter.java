package com.oficina.administrativo.infrastructure.adapters.inbound.security;

import com.oficina.administrativo.domain.ports.outbound.TokenJwtPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenJwtPort tokenJwt;

    public JwtAuthenticationFilter(TokenJwtPort tokenJwt) {
        this.tokenJwt = tokenJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var identidade = tokenJwt.validar(authorization.substring(7));
        if (identidade.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT inválido ou expirado");
            return;
        }

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + identidade.get().perfil().name()));
        var authentication = new UsernamePasswordAuthenticationToken(identidade.get().funcionarioId(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
