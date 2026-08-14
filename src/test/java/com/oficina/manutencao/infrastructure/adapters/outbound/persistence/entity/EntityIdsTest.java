package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityIdsTest {

    @Test
    void deveTestarOrdemDeServicoServicosId() {
        OrdemDeServicoServicosId id1 = new OrdemDeServicoServicosId(1, 2);
        OrdemDeServicoServicosId id2 = new OrdemDeServicoServicosId(1, 2);
        OrdemDeServicoServicosId id3 = new OrdemDeServicoServicosId(1, 3);

        assertEquals(1, id1.getOrdemDeServicoId());
        assertEquals(2, id1.getServicoId());
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(null, id1);
        assertNotEquals("string", id1);
    }

    @Test
    void deveTestarPecaNecessariaId() {
        PecaNecessariaId id1 = new PecaNecessariaId(1, 2);
        PecaNecessariaId id2 = new PecaNecessariaId(1, 2);
        PecaNecessariaId id3 = new PecaNecessariaId(1, 3);

        assertEquals(1, id1.getOrdemDeServicoId());
        assertEquals(2, id1.getPecaId());
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(null, id1);
        assertNotEquals("string", id1);
    }
}
