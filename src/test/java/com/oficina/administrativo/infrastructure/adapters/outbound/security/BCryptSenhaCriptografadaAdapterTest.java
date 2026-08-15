package com.oficina.administrativo.infrastructure.adapters.outbound.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BCryptSenhaCriptografadaAdapterTest {

    private final BCryptSenhaCriptografadaAdapter adapter = new BCryptSenhaCriptografadaAdapter();

    @Test
    void deveCriptografarSenha() {
        String senha = "minhaSenha123";
        String hash = adapter.criptografar(senha);

        assertNotNull(hash);
        assertNotEquals(senha, hash);
        assertTrue(hash.startsWith("$2a$")); // BCrypt prefix
    }

    @Test
    void deveConferirSenhaCorreta() {
        String senha = "minhaSenha123";
        String hash = adapter.criptografar(senha);

        assertTrue(adapter.confere(senha, hash));
    }

    @Test
    void deveRejeitarSenhaIncorreta() {
        String senha = "minhaSenha123";
        String hash = adapter.criptografar(senha);

        assertFalse(adapter.confere("senhaErrada", hash));
    }
}
