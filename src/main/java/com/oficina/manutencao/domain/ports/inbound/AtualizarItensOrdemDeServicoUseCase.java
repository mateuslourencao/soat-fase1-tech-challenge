package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;

import java.util.List;
import java.util.UUID;

public interface AtualizarItensOrdemDeServicoUseCase {
    OrdemDeServico AtualizarOrdemDeServico(UUID ordemDeServicoID, List<Peca> Pecas, List<Servico> Sevicos);
}
