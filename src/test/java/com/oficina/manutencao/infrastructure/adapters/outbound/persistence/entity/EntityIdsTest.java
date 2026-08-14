package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityIdsTest {

    @Test
    void deveTestarOrdemDeServicoServicosId() {
        OrdemDeServicoServicosId id1 = new OrdemDeServicoServicosId(1, 2);
        OrdemDeServicoServicosId id2 = new OrdemDeServicoServicosId(1, 2);
        OrdemDeServicoServicosId id3 = new OrdemDeServicoServicosId(1, 3);
        OrdemDeServicoServicosId id4 = new OrdemDeServicoServicosId(2, 2);

        assertEquals(1, id1.getOrdemDeServicoId());
        assertEquals(2, id1.getServicoId());
        assertEquals(id1, id1);
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);
        assertNotEquals(null, id1);
        assertNotEquals("string", id1);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void deveTestarPecaNecessariaId() {
        PecaNecessariaId id1 = new PecaNecessariaId(1, 2);
        PecaNecessariaId id2 = new PecaNecessariaId(1, 2);
        PecaNecessariaId id3 = new PecaNecessariaId(1, 3);
        PecaNecessariaId id4 = new PecaNecessariaId(2, 2);

        assertEquals(1, id1.getOrdemDeServicoId());
        assertEquals(2, id1.getPecaId());
        assertEquals(id1, id1);
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);
        assertNotEquals(null, id1);
        assertNotEquals("string", id1);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }
}
