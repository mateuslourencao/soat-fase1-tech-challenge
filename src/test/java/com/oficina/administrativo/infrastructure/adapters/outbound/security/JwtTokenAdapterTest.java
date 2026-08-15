package com.oficina.administrativo.infrastructure.adapters.outbound.security;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenAdapterTest {
    private static final String SECRET = "uma-chave-de-teste-com-no-minimo-32-bytes";

    @Test
    void deveGerarEValidarTokenAssinado() {
        JwtTokenAdapter adapter = new JwtTokenAdapter(SECRET, 10);
        Funcionario funcionario = new Funcionario(15, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.ADMIN, true);

        String token = adapter.gerar(funcionario);

        assertEquals(3, token.split("\\.").length);
        assertEquals(15, adapter.validar(token).orElseThrow().funcionarioId());
        assertEquals(PerfilFuncionario.ADMIN, adapter.validar(token).orElseThrow().perfil());
    }

    @Test
    void deveRejeitarConfiguracaoOuTokensInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new JwtTokenAdapter("curta", 1));
        assertThrows(IllegalArgumentException.class, () -> new JwtTokenAdapter(SECRET, 0));
        JwtTokenAdapter adapter = new JwtTokenAdapter(SECRET, 1);
        assertAll(
                () -> assertTrue(adapter.validar("invalido").isEmpty()),
                () -> assertTrue(adapter.validar("a.b.c").isEmpty()),
                () -> assertTrue(adapter.validar(tokenComPayload("{\"sub\":15,\"perfil\":\"ADMIN\",\"exp\":1")).isEmpty()));
    }


    private String tokenComPayload(String payload) {
        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\"}".getBytes(StandardCharsets.UTF_8));
        String corpo = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return header + "." + corpo + ".assinatura";
    }
}
