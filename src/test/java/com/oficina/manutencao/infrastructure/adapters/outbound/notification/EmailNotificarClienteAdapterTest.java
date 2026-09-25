package com.oficina.manutencao.infrastructure.adapters.outbound.notification;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailNotificarClienteAdapterTest {
    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final EmailNotificarClienteAdapter adapter =
            new EmailNotificarClienteAdapter(mailSender, "no-reply@oficina.test");

    @Test
    void deveEnviarEmailComDestinatarioEStatusDaOrdem() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "123456789");
        OrdemDeServico ordem = OrdemDeServico.builder()
                .id(1)
                .documentoCliente("123")
                .placaVeiculo("ABC1234")
                .descricaoQueixas("Barulho")
                .status(StatusOS.EM_DIAGNOSTICO)
                .build();

        adapter.notificarAtualizacaoStatus(cliente, ordem);

        var captor = org.mockito.ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage mensagem = captor.getValue();
        assertEquals("no-reply@oficina.test", mensagem.getFrom());
        assertArrayEquals(new String[]{"joao@email.com"}, mensagem.getTo());
        assertEquals("Atualização da ordem de serviço", mensagem.getSubject());
        assertEquals("""
                Olá, Joao!

                A OS referente à placa ABC1234 foi atualizada para o status EM_DIAGNOSTICO.
                """, mensagem.getText());
    }

    @Test
    void deveIgnorarFalhaAoEnviarEmail() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "123456789");
        OrdemDeServico ordem = OrdemDeServico.builder()
                .id(1)
                .documentoCliente("123")
                .placaVeiculo("ABC1234")
                .descricaoQueixas("Barulho")
                .status(StatusOS.EM_DIAGNOSTICO)
                .build();
        doThrow(new RuntimeException("Falha no provedor de e-mail"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> adapter.notificarAtualizacaoStatus(cliente, ordem));
    }
}
