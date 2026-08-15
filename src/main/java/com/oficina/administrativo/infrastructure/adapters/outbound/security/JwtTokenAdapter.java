package com.oficina.administrativo.infrastructure.adapters.outbound.security;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.outbound.TokenJwtPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtTokenAdapter implements TokenJwtPort {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
    private static final Pattern SUBJECT_PATTERN = Pattern.compile("\\\"sub\\\":(\\d+)");
    private static final Pattern PERFIL_PATTERN = Pattern.compile("\\\"perfil\\\":\\\"(ADMIN|MECANICO|ATENDENTE)\\\"");
    private static final Pattern EXPIRATION_PATTERN = Pattern.compile("\\\"exp\\\":(\\d+)");

    private final byte[] secret;
    private final long expirationMinutes;

    public JwtTokenAdapter(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.expiration-minutes}") long expirationMinutes) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT_SECRET deve ter ao menos 32 caracteres");
        }
        if (expirationMinutes <= 0) {
            throw new IllegalArgumentException("A expiração do JWT deve ser positiva");
        }
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String gerar(Funcionario funcionario) {
        long now = Instant.now().getEpochSecond();
        long expiration = Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES).getEpochSecond();
        String header = codificar("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = codificar("{\"sub\":" + funcionario.getId() + ",\"perfil\":\""
                + funcionario.getPerfil().name() + "\",\"iat\":" + now + ",\"exp\":" + expiration + "}");
        String assinatura = codificar(assinar(header + "." + payload));
        return header + "." + payload + "." + assinatura;
    }

    @Override
    public Optional<IdentidadeToken> validar(String token) {
        try {
            String[] partes = token.split("\\.");
            if (partes.length != 3 || !assinaturaValida(partes)) {
                return Optional.empty();
            }

            String payload = new String(DECODER.decode(partes[1]), StandardCharsets.UTF_8);
            Matcher subject = SUBJECT_PATTERN.matcher(payload);
            Matcher perfil = PERFIL_PATTERN.matcher(payload);
            Matcher expiration = EXPIRATION_PATTERN.matcher(payload);
            if (!subject.find() || !perfil.find() || !expiration.find()
                    || Instant.now().getEpochSecond() >= Long.parseLong(expiration.group(1))) {
                return Optional.empty();
            }

            return Optional.of(new IdentidadeToken(
                    Integer.parseInt(subject.group(1)), PerfilFuncionario.valueOf(perfil.group(1))));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private boolean assinaturaValida(String[] partes) {
        byte[] recebida = DECODER.decode(partes[2]);
        byte[] esperada = assinar(partes[0] + "." + partes[1]);
        return MessageDigest.isEqual(esperada, recebida);
    }

    private byte[] assinar(String conteudo) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(conteudo.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            throw new IllegalStateException("Não foi possível assinar o token JWT", exception);
        }
    }

    private String codificar(String valor) {
        return ENCODER.encodeToString(valor.getBytes(StandardCharsets.UTF_8));
    }

    private String codificar(byte[] valor) {
        return ENCODER.encodeToString(valor);
    }
}
