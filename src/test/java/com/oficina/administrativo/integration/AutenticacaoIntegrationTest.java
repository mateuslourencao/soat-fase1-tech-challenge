package com.oficina.administrativo.integration;

import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.entity.FuncionarioEntity;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.repository.FuncionarioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class AutenticacaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuncionarioJpaRepository funcionarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        funcionarioRepository.deleteAll();

        FuncionarioEntity admin = new FuncionarioEntity(
                "Administrador Teste",
                "admin@teste.com",
                passwordEncoder.encode("senha123"),
                PerfilFuncionario.ADMIN,
                true
        );
        funcionarioRepository.save(admin);
    }

    @Test
    void deveAutenticarComSucessoERetornarToken() throws Exception {
        String jsonRequest = """
                {
                  "email": "admin@teste.com",
                  "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/api/v1/administrativo/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.nome").value("Administrador Teste"))
                .andExpect(jsonPath("$.perfil").value("ADMIN"));
    }

    @Test
    void deveRetornar401QuandoSenhaIncorreta() throws Exception {
        String jsonRequest = """
                {
                  "email": "admin@teste.com",
                  "senha": "senha_errada"
                }
                """;

        mockMvc.perform(post("/api/v1/administrativo/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar401QuandoEmailNaoExiste() throws Exception {
        String jsonRequest = """
                {
                  "email": "inexistente@teste.com",
                  "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/api/v1/administrativo/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        String jsonRequest = """
                {
                  "email": "email-invalido",
                  "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/api/v1/administrativo/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoCamposObrigatoriosAusentes() throws Exception {
        String jsonRequest = """
                {
                  "email": ""
                }
                """;

        mockMvc.perform(post("/api/v1/administrativo/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}
