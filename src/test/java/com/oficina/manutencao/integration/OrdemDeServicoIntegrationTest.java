package com.oficina.manutencao.integration;

import com.jayway.jsonpath.JsonPath;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.ClienteJpaRepository;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.OrdemDeServicoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class OrdemDeServicoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrdemDeServicoJpaRepository osRepository;

    @Autowired
    private PecaJpaRepository pecaRepository;

    @Autowired
    private ServicoJpaRepository servicoRepository;

    @Autowired
    private ClienteJpaRepository clienteRepository;

    private PecaEntity peca;
    private ServicoEntity servico;

    @BeforeEach
    void setUp() {
        osRepository.deleteAll();
        pecaRepository.deleteAll();
        servicoRepository.deleteAll();
        clienteRepository.deleteAll();

        peca = pecaRepository.save(new PecaEntity("Amortecedor", BigDecimal.valueOf(350.00), 20));
        servico = servicoRepository.save(new ServicoEntity("Troca de Amortecedor", BigDecimal.valueOf(150.00)));
        clienteRepository.save(new com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.ClienteEntity("12345678901", "Cliente Teste", "cliente@teste.com", "11999999999"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveExecutarFluxoCompletoOrdemDeServicoComSucesso() throws Exception {
        // 1. Criar Ordem de Serviço
        String criarJson = """
                {
                  "documentoCliente": "12345678901",
                  "placaVeiculo": "ABC1234",
                  "descricaoQueixas": "Barulho na suspensão dianteira"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/v1/ordensdeservico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("RECEBIDA"))
                .andExpect(jsonPath("$.documentoCliente").value("12345678901"))
                .andExpect(jsonPath("$.placaVeiculo").value("ABC1234"))
                .andReturn();

        int osId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        // 2. Iniciar Diagnóstico
        mockMvc.perform(patch("/api/v1/ordensdeservico/" + osId + "/iniciar-diagnostico"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/ordensdeservico/" + osId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_DIAGNOSTICO"));

        // 3. Atualizar Itens (Peças e Serviços)
        String itensJson = String.format("""
                {
                  "pecasNecessarias": [
                    {
                      "pecaId": %d,
                      "quantidade": 2
                    }
                  ],
                  "servicosIds": [%d]
                }
                """, peca.getId(), servico.getId());

        mockMvc.perform(post("/api/v1/ordensdeservico/" + osId + "/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itensJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pecasNecessarias", hasSize(1)))
                .andExpect(jsonPath("$.servicos", hasSize(1)))
                .andExpect(jsonPath("$.orcamento").value(850.0)); // (350 * 2) + 150 = 700 + 150 = 850

        // 4. Enviar Orçamento
        mockMvc.perform(patch("/api/v1/ordensdeservico/" + osId + "/enviar-orcamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AGUARDANDO_APROVACAO"));

        // 5. Aprovar Orçamento
        mockMvc.perform(patch("/api/v1/ordensdeservico/" + osId + "/aprovar-orcamento"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/ordensdeservico/" + osId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_EXECUCAO"));

        // 6. Finalizar Reparo
        mockMvc.perform(patch("/api/v1/ordensdeservico/" + osId + "/finalizar-reparo"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/ordensdeservico/" + osId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"));

        // 7. Entregar Veículo
        mockMvc.perform(patch("/api/v1/ordensdeservico/" + osId + "/entregar-veiculo"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/ordensdeservico/" + osId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENTREGUE"));
    }
}
