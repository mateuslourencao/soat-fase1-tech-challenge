package com.oficina.estoque.integration;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class PecaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PecaJpaRepository pecaRepository;

    private PecaEntity pecaSalva;

    @BeforeEach
    void setUp() {
        pecaRepository.deleteAll();
        PecaEntity peca = new PecaEntity("Pastilha de Freio", BigDecimal.valueOf(120.50), 5);
        pecaSalva = pecaRepository.save(peca);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarUmaPecaComSucesso() throws Exception {
        String jsonRequest = """
                {
                  "descricao": "Filtro de Óleo",
                  "valor": 35.50,
                  "quantidade": 10
                }
                """;

        mockMvc.perform(post("/api/v1/pecas/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao").value("Filtro de Óleo"))
                .andExpect(jsonPath("$.valor").value(35.50))
                .andExpect(jsonPath("$.quantidade").value(10));
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    void deveFazerReposicaoDeEstoque() throws Exception {
        String jsonReposicao = String.format("""
                {
                  "id": %d,
                  "quantidade": 15
                }
                """, pecaSalva.getId());

        mockMvc.perform(put("/api/v1/pecas/repor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReposicao))
                .andExpect(status().isOk());

        PecaEntity pecaAtualizada = pecaRepository.findById(pecaSalva.getId()).orElseThrow();
        assertEquals(20, pecaAtualizada.getQuantidade());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarUmaPecaComSucesso() throws Exception {
        String jsonRequest = """
                {
                  "descricao": "Pastilha de Freio Cerâmica",
                  "valor": 150.00,
                  "quantidade": 8
                }
                """;

        mockMvc.perform(put("/api/v1/pecas/" + pecaSalva.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Pastilha de Freio Cerâmica"))
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.quantidade").value(8));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDeletarUmaPecaComSucesso() throws Exception {
        mockMvc.perform(delete("/api/v1/pecas/" + pecaSalva.getId()))
                .andExpect(status().isNoContent());

        var optionalPeca = pecaRepository.findById(pecaSalva.getId());
        assertThrows(NoSuchElementException.class, optionalPeca::orElseThrow);
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    void deveListarPecasComSucesso() throws Exception {
        mockMvc.perform(get("/api/v1/pecas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].descricao").value("Pastilha de Freio"));
    }
}