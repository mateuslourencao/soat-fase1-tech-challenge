CREATE DATABASE IF NOT EXISTS oficina
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE oficina;

-- Adicionando UNIQUE
CREATE TABLE clientes (
    id VARCHAR(36) NOT NULL,
    nome VARCHAR(255),
    email VARCHAR(255),
    documento VARCHAR(255) UNIQUE, -- Evita duplicidade
    telefone VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE veiculos (
    id VARCHAR(36) NOT NULL,
    placa VARCHAR(255) UNIQUE,       -- Evita duplicidade
    marca VARCHAR(255),
    modelo VARCHAR(255),
    ano INT,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE pecas (
    id VARCHAR(36) NOT NULL,
    descricao VARCHAR(255),
    valor DECIMAL(10,2),
    quantidade INT,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE servicos (
    id VARCHAR(36) NOT NULL,
    descricao VARCHAR(255),
    valor DECIMAL(10,2),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE ordens_de_servico (
    id VARCHAR(36) NOT NULL,
    id_cliente VARCHAR(36) NOT NULL,
    id_veiculo VARCHAR(36) NOT NULL,
    orcamento DECIMAL(10,2),
    status VARCHAR(50),
    data_criacao DATETIME,
    data_atualizacao DATETIME,
    descricao_queixas VARCHAR(255),
    diagnosticos VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_ordens_de_servico_clientes
        FOREIGN KEY (id_cliente) REFERENCES clientes (id),
    CONSTRAINT fk_ordens_de_servico_veiculos
        FOREIGN KEY (id_veiculo) REFERENCES veiculos (id)
) ENGINE=InnoDB;

-- Adicionando o Valor Histórico
CREATE TABLE ordens_de_servico_servicos (
    ordem_de_servico_id VARCHAR(36) NOT NULL,
    servico_id VARCHAR(36) NOT NULL,
    valor_cobrado DECIMAL(10,2) NOT NULL, -- Salva o preço no dia da OS
    PRIMARY KEY (ordem_de_servico_id, servico_id),
    CONSTRAINT fk_os_servicos_ordens_de_servico FOREIGN KEY (ordem_de_servico_id) REFERENCES ordens_de_servico (id),
    CONSTRAINT fk_os_servicos_servicos FOREIGN KEY (servico_id) REFERENCES servicos (id)
) ENGINE=InnoDB;

CREATE TABLE pecas_necessarias (
    ordem_de_servico_id VARCHAR(36) NOT NULL,
    peca_id VARCHAR(36) NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL, -- Salva o preço no dia da OS
    PRIMARY KEY (ordem_de_servico_id, peca_id),
    CONSTRAINT fk_pecas_necessarias_ordens_de_servico FOREIGN KEY (ordem_de_servico_id) REFERENCES ordens_de_servico (id),
    CONSTRAINT fk_pecas_necessarias_pecas FOREIGN KEY (peca_id) REFERENCES pecas (id)
) ENGINE=InnoDB;