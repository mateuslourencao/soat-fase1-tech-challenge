package com.oficina.administrativo.infrastructure.adapters.outbound.security;

import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptSenhaCriptografadaAdapter implements SenhaCriptografadaPort {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean confere(String senha, String senhaHash) {
        return passwordEncoder.matches(senha, senhaHash);
    }

    @Override
    public String criptografar(String senha) {
        return passwordEncoder.encode(senha);
    }
}
