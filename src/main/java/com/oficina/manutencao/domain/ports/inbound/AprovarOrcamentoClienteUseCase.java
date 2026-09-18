package com.oficina.manutencao.domain.ports.inbound;

public interface AprovarOrcamentoClienteUseCase {
    void aprovarOrcamento(int ordemDeServicoId, String documentoCliente, boolean aprovado);
}
