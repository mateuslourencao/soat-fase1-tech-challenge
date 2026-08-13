package com.oficina.administrativo.domain.ports.outbound;

public interface SenhaCriptografadaPort {
    boolean confere(String senha, String senhaHash);
}
