CREATE DATABASE IF NOT EXISTS oficina
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE oficina;

CREATE TABLE clientes (
    nome VARCHAR(255),
    email VARCHAR(255),
    documento VARCHAR(20) NOT NULL,
    telefone VARCHAR(50),
    PRIMARY KEY (documento)
) ENGINE=InnoDB;

CREATE TABLE veiculos (
    placa VARCHAR(10) NOT NULL,
    marca VARCHAR(255),
    modelo VARCHAR(255),
    ano INT,
    PRIMARY KEY (placa)
) ENGINE=InnoDB;

CREATE TABLE pecas (
    id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255),
    valor DECIMAL(10,2),
    quantidade INT,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE servicos (
    id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255),
    valor DECIMAL(10,2),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE ordens_de_servico (
    id INT NOT NULL AUTO_INCREMENT,
    documento_cliente VARCHAR(20) NOT NULL,
    placa_veiculo VARCHAR(10) NOT NULL,
    orcamento DECIMAL(10,2),
    status VARCHAR(50),
    data_criacao DATETIME,
    data_atualizacao DATETIME,
    descricao_queixas VARCHAR(255),
    diagnosticos VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE ordens_de_servico_servicos (
    ordem_de_servico_id INT NOT NULL,
    servico_id INT NOT NULL,
    valor_cobrado DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (ordem_de_servico_id, servico_id)
) ENGINE=InnoDB;

CREATE TABLE pecas_necessarias (
    ordem_de_servico_id INT NOT NULL,
    peca_id INT NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (ordem_de_servico_id, peca_id)
) ENGINE=InnoDB;