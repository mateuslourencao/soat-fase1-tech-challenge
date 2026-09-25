package com.oficina.manutencao.infrastructure.adapters.outbound.notification;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificarClienteAdapter implements NotificarClientePort {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificarClienteAdapter.class);
    private final JavaMailSender mailSender;
    private final String remetente;

    public EmailNotificarClienteAdapter(JavaMailSender mailSender,
                                        @Value("${app.mail.from}") String remetente) {
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    @Override
    public void notificarAtualizacaoStatus(Cliente cliente, OrdemDeServico os) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(cliente.getEmail());
        mensagem.setSubject("Atualização da ordem de serviço");
        mensagem.setText("""
                Olá, %s!

                A OS referente à placa %s foi atualizada para o status %s.
                """.formatted(cliente.getNome(), os.getPlacaVeiculo(), os.getStatus()));
        try {
            mailSender.send(mensagem);
        } catch (RuntimeException exception) {
            logger.error("Não foi possível enviar a atualização da OS #{} por e-mail para {}",
                    os.getId(), cliente.getEmail(), exception);
        }
    }
}
