package com.oficina.manutencao.domain.ports.inbound;

import java.util.UUID;

public interface IniciarDiagnosticoUseCase {
    void IniciarDiagnostico(UUID ordemDeServicoID);
}
