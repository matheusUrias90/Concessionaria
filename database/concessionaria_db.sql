-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 30/10/2026 às 22:20
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `concessionaria_db`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `checklist_entrega`
--

CREATE TABLE `checklist_entrega` (
  `id` bigint(20) NOT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `entrega_concluida` bit(1) NOT NULL,
  `data_entrega` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `checklist_entrega`
--

INSERT INTO `checklist_entrega` (`id`, `veiculo_id`, `entrega_concluida`, `data_entrega`) VALUES
(1, 29, b'0', NULL),
(3, 3, b'0', NULL),
(4, 1, b'1', '2026-10-16'),
(5, 4, b'1', NULL),
(6, 7, b'0', NULL),
(7, 8, b'0', NULL),
(8, 9, b'1', '2026-10-09'),
(9, 11, b'0', NULL),
(10, 14, b'0', NULL),
(11, 15, b'0', NULL),
(12, 16, b'0', NULL),
(13, 17, b'0', NULL),
(14, 18, b'0', NULL),
(15, 19, b'0', NULL),
(16, 20, b'0', NULL),
(17, 22, b'0', NULL),
(18, 23, b'0', NULL),
(19, 24, b'1', '2026-10-05'),
(20, 25, b'0', NULL),
(21, 26, b'0', NULL),
(22, 27, b'0', NULL),
(23, 28, b'1', '2026-10-03'),
(24, 48, b'0', NULL);

-- --------------------------------------------------------

--
-- Estrutura para tabela `checklist_entrega_itens`
--

CREATE TABLE `checklist_entrega_itens` (
  `checklist_entrega_id` bigint(20) NOT NULL,
  `itens_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `clientes`
--

CREATE TABLE `clientes` (
  `id` bigint(20) NOT NULL,
  `cpf` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `telefone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `clientes`
--

INSERT INTO `clientes` (`id`, `cpf`, `email`, `nome`, `telefone`) VALUES
(1, '574.235.652-00', 'jose@email.com', 'José da Silva', '(71) 95321-4200'),
(2, '23456789012', 'maria@email.com', 'Maria Oliveira', '(43) 98475-2142'),
(3, '34567890123', 'pedro@email.com', 'Pedro Santos', '(11) 98542-1066'),
(4, '45678901234', 'ana@email.com', 'Ana Souza', '(43) 99674-3695'),
(5, '56789012345', 'lucas@email.com', 'Lucas Ferreira', '(43) 99963-2044'),
(6, '67890123456', 'fernanda@email.com', 'Fernanda Costa', '(43) 99635-2515'),
(7, '78901234567', 'ricardo@email.com', 'Ricardo Almeida', '(43) 98422-1249'),
(8, '89012345678', 'juliana@email.com', 'Juliana Ferreira', '(43) 99820-3544'),
(9, '17482956310', 'eduardo.martins@email.com', 'Eduardo Martins', '(43) 98417-3625'),
(10, '59271483602', 'camila.ribeiro@email.com', 'Camila Ribeiro', '(43) 99128-7451'),
(11, '80341692755', 'felipe.g@email.com', 'Felipe Gonçalves', '(43) 99641-2387'),
(12, '21864397580', 'patricia.nunes@email.com', 'Patrícia Nunes', '(43) 98873-5196'),
(13, '46719283501', 'andre.carvalho@email.com', 'André Carvalho', '(43) 99712-8640'),
(14, '73152048619', 'juliana.mendes@email.com', 'Juliana Mendes', '(43) 98463-2759'),
(15, '95037218466', 'roberto.lima@email.com', 'Roberto Lima', '(43) 99254-1836'),
(16, '38695172044', 'vanessa.rocha@email.com', 'Vanessa Rocha', '(43) 98741-6208'),
(17, '62418370915', 'gustavo.freitas@email.com', 'Gustavo Freitas', '(43) 99416-5372'),
(18, '15904738268', 'larissa.azevedo@email.com', 'Larissa Azevedo', '(43) 98392-7145'),
(19, '555.632.449-03', '', 'Ederson macedo osório', '(43) 99635-8544'),
(20, '874.236.519-02', 'adalto.noco@gmail.com', 'Adalto lezer nicome', '(43) 98472-1015'),
(21, '975.442.369-40', '', 'Bruno dantas coelho', '(44) 98835-4122'),
(22, '987.521.469-04', 'geovannydurlony@gmail.com', 'Geovanny silve durlony', '(43) 99852-1460');

-- --------------------------------------------------------

--
-- Estrutura para tabela `configuracao_concessionaria`
--

CREATE TABLE `configuracao_concessionaria` (
  `id` bigint(20) NOT NULL,
  `cnpj` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `endereco` varchar(255) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `nome_empresa` varchar(255) DEFAULT NULL,
  `telefone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `configuracao_concessionaria`
--

INSERT INTO `configuracao_concessionaria` (`id`, `cnpj`, `email`, `endereco`, `logo`, `nome_empresa`, `telefone`) VALUES
(1, '12.345.678/0001-90', 'contato@uriasveiculos.com', 'Av. Tiradentes, 1650 Londrina - PR', 'logo-documentos.png', 'Urias Veículos', '(43) 99632-1475');

-- --------------------------------------------------------

--
-- Estrutura para tabela `forma_pagamento_negociacao`
--

CREATE TABLE `forma_pagamento_negociacao` (
  `id` bigint(20) NOT NULL,
  `administradora_consorcio` varchar(255) DEFAULT NULL,
  `bandeira_cartao` varchar(255) DEFAULT NULL,
  `data_prevista` date DEFAULT NULL,
  `instituicao_financeira` varchar(255) DEFAULT NULL,
  `numero_carta_credito` varchar(255) DEFAULT NULL,
  `parcelas` int(11) DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `tipo` enum('CARTAO','CONSORCIO','DINHEIRO','FINANCIAMENTO','PIX','VEICULO_ENTRADA') NOT NULL,
  `valor` double DEFAULT NULL,
  `valor_parcela` double DEFAULT NULL,
  `negociacao_id` bigint(20) NOT NULL,
  `veiculo_entrada_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `forma_pagamento_negociacao`
--

INSERT INTO `forma_pagamento_negociacao` (`id`, `administradora_consorcio`, `bandeira_cartao`, `data_prevista`, `instituicao_financeira`, `numero_carta_credito`, `parcelas`, `status`, `tipo`, `valor`, `valor_parcela`, `negociacao_id`, `veiculo_entrada_id`) VALUES
(1, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 128500, NULL, 1, NULL),
(2, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 40000, NULL, 2, NULL),
(3, NULL, NULL, NULL, 'brasdesco', NULL, 72, 'CONFIRMADA', 'FINANCIAMENTO', 122900, 1706.94, 2, NULL),
(4, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 22700, NULL, 3, NULL),
(5, NULL, NULL, NULL, 'bradesco', NULL, 48, 'RECUSADA', 'FINANCIAMENTO', 63000, 1312.5, 3, NULL),
(6, NULL, NULL, NULL, NULL, NULL, NULL, 'PENDENTE', 'VEICULO_ENTRADA', 23000, NULL, 3, 1),
(7, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 38900, NULL, 4, NULL),
(8, NULL, NULL, NULL, 'bradesco', NULL, 48, 'RECUSADA', 'FINANCIAMENTO', 100000, 2083.33, 4, NULL),
(9, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 26900, NULL, 5, NULL),
(10, 'servopa', NULL, NULL, NULL, '25817544', NULL, 'CONFIRMADA', 'CONSORCIO', 88000, NULL, 5, NULL),
(11, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 12000, NULL, 6, NULL),
(12, NULL, NULL, NULL, 'itau', NULL, 36, 'CONFIRMADA', 'FINANCIAMENTO', 40900, 1136.11, 6, NULL),
(13, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 20000, NULL, 7, NULL),
(14, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'VEICULO_ENTRADA', 69500, NULL, 7, 2),
(15, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'VEICULO_ENTRADA', 48000, NULL, 8, 3),
(16, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 30000, NULL, 8, NULL),
(17, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 16900, NULL, 8, NULL),
(18, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 10500, NULL, 9, NULL),
(19, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 3400, NULL, 9, NULL),
(20, 'magazine luiza', NULL, NULL, NULL, '92563447', NULL, 'CONFIRMADA', 'CONSORCIO', 50000, NULL, 9, NULL),
(21, NULL, NULL, NULL, 'bradesco', NULL, 60, 'CONFIRMADA', 'FINANCIAMENTO', 126500, 2108.33, 10, NULL),
(22, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 60000, NULL, 11, NULL),
(23, NULL, NULL, NULL, 'itau', NULL, 48, 'CONFIRMADA', 'FINANCIAMENTO', 108000, 2250, 11, NULL),
(24, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 56000, NULL, 12, NULL),
(25, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 80000, NULL, 13, NULL),
(26, NULL, NULL, NULL, 'itau', NULL, 60, 'RECUSADA', 'FINANCIAMENTO', 109500, 1825, 13, NULL),
(27, NULL, NULL, NULL, NULL, NULL, NULL, 'RECUSADA', 'DINHEIRO', 100000, NULL, 14, NULL),
(28, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 49900, NULL, 14, NULL),
(29, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 37800, NULL, 15, NULL),
(30, 'Servopa', NULL, NULL, NULL, '411126589', NULL, 'RECUSADA', 'CONSORCIO', 100000, NULL, 15, NULL),
(49, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 76900, NULL, 22, NULL),
(50, 'Servopa', NULL, NULL, NULL, '175645321', NULL, 'RECUSADA', 'CONSORCIO', 100000, NULL, 22, NULL),
(51, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 20000, NULL, 23, NULL),
(52, NULL, NULL, NULL, 'Itaú ', NULL, 30, 'PENDENTE', 'FINANCIAMENTO', 48500, 1616.67, 23, NULL),
(53, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 25000, NULL, 24, NULL),
(54, NULL, NULL, NULL, 'Itaú ', NULL, 60, 'RECUSADA', 'FINANCIAMENTO', 83700, 1395, 24, NULL),
(59, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 25000, NULL, 27, NULL),
(60, 'Servopa', NULL, NULL, NULL, '44525577', NULL, 'CONFIRMADA', 'CONSORCIO', 40000, NULL, 27, NULL),
(61, NULL, NULL, NULL, NULL, NULL, NULL, 'PENDENTE', 'DINHEIRO', 137800, NULL, 28, NULL),
(62, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 116900, NULL, 29, NULL),
(63, NULL, NULL, NULL, 'Banco do Brasil ', NULL, 60, 'CONFIRMADA', 'FINANCIAMENTO', 108700, 1811.67, 30, NULL),
(64, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 51000, NULL, 31, NULL),
(65, NULL, 'Mastercard ', NULL, NULL, NULL, 60, 'CONFIRMADA', 'CARTAO', 54900, 915, 31, NULL),
(66, NULL, NULL, NULL, NULL, NULL, NULL, 'PENDENTE', 'DINHEIRO', 51000, NULL, 32, NULL),
(67, NULL, 'Mastercard ', NULL, NULL, NULL, 60, 'PENDENTE', 'CARTAO', 54900, 915, 32, NULL),
(68, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 40000, NULL, 33, NULL),
(69, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'VEICULO_ENTRADA', 49900, NULL, 33, 4),
(70, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 45000, NULL, 34, NULL),
(71, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'VEICULO_ENTRADA', 46500, NULL, 34, 5),
(72, NULL, NULL, NULL, 'Banco do Brasil ', NULL, 72, 'CONFIRMADA', 'FINANCIAMENTO', 142900, 1984.72, 35, NULL),
(73, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'PIX', 30000, NULL, 36, NULL),
(74, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'VEICULO_ENTRADA', 78500, NULL, 36, 6),
(75, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 50000, NULL, 37, NULL),
(76, 'servopa', NULL, NULL, NULL, '403698722', NULL, 'CONFIRMADA', 'CONSORCIO', 30000, NULL, 37, NULL),
(77, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'VEICULO_ENTRADA', 59900, NULL, 37, 7),
(78, NULL, NULL, NULL, NULL, NULL, NULL, 'CONFIRMADA', 'DINHEIRO', 68500, NULL, 38, NULL),
(79, NULL, NULL, NULL, NULL, NULL, NULL, 'PENDENTE', 'DINHEIRO', 65900, NULL, 39, NULL);

-- --------------------------------------------------------

--
-- Estrutura para tabela `item_checklist_entrega`
--

CREATE TABLE `item_checklist_entrega` (
  `id` bigint(20) NOT NULL,
  `conferido` bit(1) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `checklist_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `item_checklist_entrega`
--

INSERT INTO `item_checklist_entrega` (`id`, `conferido`, `descricao`, `checklist_id`) VALUES
(11, b'1', 'Chave principal', 4),
(14, b'0', 'Chave principal', 6),
(15, b'1', 'Chave principal', 7),
(16, b'1', 'Chave principal', 8),
(17, b'0', 'Chave principal', 9),
(18, b'1', 'Chave principal', 10),
(19, b'0', 'Chave principal', 11),
(20, b'0', 'Chave principal', 12),
(21, b'0', 'Chave principal', 13),
(22, b'0', 'Chave principal', 14),
(23, b'0', 'Chave principal', 15),
(24, b'0', 'Chave principal', 16),
(25, b'0', 'Chave principal', 17),
(26, b'0', 'Chave principal', 18),
(27, b'1', 'Chave principal', 19),
(28, b'0', 'Chave principal', 20),
(29, b'0', 'Chave principal', 21),
(30, b'0', 'Chave principal', 22),
(31, b'1', 'Chave principal', 23),
(42, b'1', 'Chave reserva', 4),
(44, b'0', 'Chave reserva', 6),
(45, b'1', 'Chave reserva', 7),
(46, b'1', 'Chave reserva', 8),
(47, b'0', 'Chave reserva', 9),
(48, b'1', 'Chave reserva', 10),
(49, b'0', 'Chave reserva', 11),
(50, b'0', 'Chave reserva', 12),
(51, b'0', 'Chave reserva', 13),
(52, b'0', 'Chave reserva', 14),
(53, b'0', 'Chave reserva', 15),
(54, b'0', 'Chave reserva', 16),
(55, b'0', 'Chave reserva', 17),
(56, b'0', 'Chave reserva', 18),
(57, b'1', 'Chave reserva', 19),
(58, b'0', 'Chave reserva', 20),
(59, b'0', 'Chave reserva', 21),
(60, b'0', 'Chave reserva', 22),
(61, b'1', 'Chave reserva', 23),
(73, b'0', 'chave pricipal', 3),
(74, b'0', 'chave reserva', 3),
(75, b'0', 'Chave principal', 1),
(76, b'0', 'Chave reserva', 1),
(77, b'0', 'manual', 1),
(80, b'1', 'Chave principal', 5),
(81, b'1', 'Chave reserva', 5),
(82, b'1', 'manual', 5),
(83, b'0', 'Chave principal', 24),
(84, b'0', 'Chave reserva', 24),
(85, b'0', 'Manual do proprietario', 24);

-- --------------------------------------------------------

--
-- Estrutura para tabela `negociacao`
--

CREATE TABLE `negociacao` (
  `id` bigint(20) NOT NULL,
  `data_encerramento` datetime(6) DEFAULT NULL,
  `data_inicio` datetime(6) NOT NULL,
  `motivo_cancelamento` varchar(500) DEFAULT NULL,
  `observacao` varchar(1000) DEFAULT NULL,
  `status` enum('CANCELADA','CONCLUIDA','EM_ANDAMENTO') NOT NULL,
  `valor_proposto` double NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  `vendedor_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `negociacao`
--

INSERT INTO `negociacao` (`id`, `data_encerramento`, `data_inicio`, `motivo_cancelamento`, `observacao`, `status`, `valor_proposto`, `cliente_id`, `veiculo_id`, `vendedor_id`) VALUES
(1, '2026-08-15 23:02:20.000000', '2026-08-10 21:48:48.000000', NULL, NULL, 'CONCLUIDA', 128500, 3, 10, 3),
(2, '2026-08-14 18:27:08.000000', '2026-08-11 17:21:02.000000', NULL, NULL, 'CONCLUIDA', 162900, 10, 13, 3),
(3, '2026-08-14 21:29:14.000000', '2026-08-11 17:25:37.000000', 'Tentou financiamento em dois bancos diferentes, ambos cancelados, sendo assim o Sr roberto desistiu do negocio, pedindo a devolução da quantia de 22,700 feita no pix', NULL, 'CANCELADA', 108700, 15, 14, 4),
(4, '2026-08-15 22:29:20.000000', '2026-08-15 22:24:47.000000', 'Financiamento recusado. O cliente optou pela desistência da negociação e solicitou a devolução do valor de R$ 38.900,00 pago via PIX.', NULL, 'CANCELADA', 138900, 4, 7, 1),
(5, '2026-08-20 12:52:22.000000', '2026-08-20 12:50:37.000000', NULL, NULL, 'CONCLUIDA', 114900, 11, 21, 1),
(6, '2026-08-21 23:13:27.000000', '2026-08-21 22:22:22.000000', NULL, NULL, 'CONCLUIDA', 52900, 19, 5, 4),
(7, '2026-08-26 00:43:17.000000', '2026-08-25 23:47:44.000000', NULL, NULL, 'CONCLUIDA', 89500, 2, 6, 1),
(8, '2026-09-07 17:51:51.000000', '2026-09-07 17:50:16.000000', NULL, NULL, 'CONCLUIDA', 94900, 20, 12, 4),
(9, '2026-09-19 21:45:36.000000', '2026-09-19 21:39:08.000000', NULL, NULL, 'CONCLUIDA', 63900, 15, 2, 4),
(10, '2026-09-23 21:57:21.000000', '2026-09-23 21:57:08.000000', NULL, NULL, 'CONCLUIDA', 126500, 7, 8, 1),
(11, '2026-09-23 23:20:11.000000', '2026-09-23 23:07:19.000000', NULL, NULL, 'CONCLUIDA', 168000, 21, 4, 4),
(12, '2026-10-03 00:08:37.000000', '2026-10-03 00:07:52.000000', NULL, NULL, 'CONCLUIDA', 56000, 19, 28, 3),
(13, '2026-10-03 23:16:06.000000', '2026-10-03 19:34:20.000000', 'Cancelado pois banco nao aceitou o financiamento', NULL, 'CANCELADA', 189500, 15, 18, 3),
(14, '2026-10-03 23:49:48.000000', '2026-10-03 23:36:30.000000', 'Cancelamento de teste', NULL, 'CANCELADA', 149900, 4, 3, 1),
(15, '2026-10-05 13:08:35.000000', '2026-10-05 13:07:40.000000', 'Carta de crédito não liberada', NULL, 'CANCELADA', 137800, 1, 11, 3),
(22, '2026-10-05 14:25:25.000000', '2026-10-05 14:24:46.000000', 'Consórcio não liberado', NULL, 'CANCELADA', 176900, 9, 23, 4),
(23, '2026-10-05 16:50:00.000000', '2026-10-05 16:48:50.000000', 'Banco não aprovou o financiamento', NULL, 'CANCELADA', 68500, 8, 1, 3),
(24, '2026-10-05 17:07:41.000000', '2026-10-05 17:00:18.000000', 'Teste de cancelamento', NULL, 'CANCELADA', 108700, 6, 14, 4),
(27, '2026-10-05 17:09:44.000000', '2026-10-05 17:03:55.000000', NULL, NULL, 'CONCLUIDA', 65000, 9, 24, 4),
(28, '2026-10-05 17:30:17.000000', '2026-10-05 17:29:55.000000', 'Desistência do cliente', NULL, 'CANCELADA', 137800, 4, 11, 3),
(29, '2026-10-05 17:45:38.000000', '2026-10-05 17:45:22.000000', NULL, NULL, 'CONCLUIDA', 116900, 13, 19, 4),
(30, '2026-10-05 18:24:11.000000', '2026-10-05 18:23:46.000000', NULL, NULL, 'CONCLUIDA', 108700, 21, 14, 3),
(31, '2026-10-05 18:50:42.000000', '2026-10-05 18:47:10.000000', NULL, NULL, 'CONCLUIDA', 105900, 12, 40, 4),
(32, '2026-10-05 18:51:00.000000', '2026-10-05 18:47:12.000000', 'Duplicado', NULL, 'CANCELADA', 105900, 12, 40, 4),
(33, '2026-10-09 12:54:29.000000', '2026-10-09 12:53:45.000000', NULL, NULL, 'CONCLUIDA', 89900, 17, 41, 3),
(34, '2026-10-09 13:06:57.000000', '2026-10-09 13:06:33.000000', NULL, NULL, 'CONCLUIDA', 91500, 6, 43, 4),
(35, '2026-10-09 13:08:49.000000', '2026-10-09 13:08:39.000000', NULL, NULL, 'CONCLUIDA', 142900, 10, 9, 1),
(36, '2026-10-16 12:05:58.000000', '2026-10-16 11:58:27.000000', NULL, NULL, 'CONCLUIDA', 108500, 3, 34, 3),
(37, '2026-10-16 14:03:27.000000', '2026-10-16 13:57:10.000000', NULL, NULL, 'CONCLUIDA', 139900, 16, 35, 3),
(38, '2026-10-16 22:20:08.000000', '2026-10-16 22:19:56.000000', NULL, NULL, 'CONCLUIDA', 68500, 22, 1, 3),
(39, '2026-10-16 22:31:31.000000', '2026-10-16 22:30:20.000000', 'Desistencia do comprador', NULL, 'CANCELADA', 65900, 4, 32, 4);

-- --------------------------------------------------------

--
-- Estrutura para tabela `notificacoes`
--

CREATE TABLE `notificacoes` (
  `id` bigint(20) NOT NULL,
  `data_criacao` datetime(6) NOT NULL,
  `lida` bit(1) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `mensagem` varchar(500) NOT NULL,
  `tipo` enum('ENTREGA_CONCLUIDA','NEGOCIACAO_CANCELADA','NEGOCIACAO_INICIADA','PENDENCIA_CRIADA','PENDENCIA_RESOLVIDA','REPASSE_QUITADO','SENHA_ALTERADA','SOLICITACAO_LOGIN','VENDA_REALIZADA') NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `referencia_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `notificacoes`
--

INSERT INTO `notificacoes` (`id`, `data_criacao`, `lida`, `link`, `mensagem`, `tipo`, `titulo`, `usuario_id`, `referencia_id`) VALUES
(1, '2026-10-02 23:26:43.000000', b'1', '/admin/solicitacoes-login', 'pietro mascolvis solicitou a alteração do login de pietro22 para PietroTest.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, NULL),
(2, '2026-10-02 23:26:43.000000', b'1', '/admin/solicitacoes-login', 'pietro mascolvis solicitou a alteração do login de pietro22 para PietroTest.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, NULL),
(3, '2026-10-03 00:00:48.000000', b'1', '/admin/solicitacoes-login', 'João Pedro Lisboa solicitou a alteração do login de joaopedro para jpedro.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, NULL),
(4, '2026-10-03 00:00:48.000000', b'1', '/admin/solicitacoes-login', 'João Pedro Lisboa solicitou a alteração do login de joaopedro para jpedro.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, NULL),
(5, '2026-10-03 14:47:38.000000', b'1', '/admin/solicitacoes-login', 'pietro mascolvis solicitou a alteração do login de pietro22 para p22.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, NULL),
(6, '2026-10-03 14:47:38.000000', b'1', '/admin/solicitacoes-login', 'pietro mascolvis solicitou a alteração do login de pietro22 para p22.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, NULL),
(7, '2026-10-03 15:51:12.000000', b'1', NULL, 'Carlos Almeida solicitou a alteração do login de almeida para carlos.almeida.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, 10),
(8, '2026-10-03 15:51:12.000000', b'1', NULL, 'Carlos Almeida solicitou a alteração do login de almeida para carlos.almeida.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, 10),
(9, '2026-10-03 16:13:54.000000', b'1', NULL, 'pietro mascolvis solicitou a alteração do login de pietro22 para p22.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, 11),
(10, '2026-10-03 16:13:54.000000', b'1', NULL, 'pietro mascolvis solicitou a alteração do login de pietro22 para p22.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, 11),
(11, '2026-10-03 16:19:31.000000', b'1', NULL, 'Carlos Almeida solicitou a alteração do login de carlos.almeida para calmeida.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, 12),
(12, '2026-10-03 16:19:31.000000', b'1', NULL, 'Carlos Almeida solicitou a alteração do login de carlos.almeida para calmeida.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, 12),
(13, '2026-10-03 18:49:11.000000', b'1', NULL, 'pietro mascolvis alterou a própria senha.', 'SENHA_ALTERADA', 'Senha alterada', 1, NULL),
(14, '2026-10-03 18:49:11.000000', b'1', NULL, 'pietro mascolvis alterou a própria senha.', 'SENHA_ALTERADA', 'Senha alterada', 2, NULL),
(15, '2026-10-03 19:34:20.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Roberto Lima para o veículo Toro Volcano.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 13),
(16, '2026-10-03 19:34:20.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Roberto Lima para o veículo Toro Volcano.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 13),
(17, '2026-10-03 23:16:06.000000', b'1', '/negociacoes/13', 'Devolver DINHEIRO ao comprador Roberto Lima.', 'PENDENCIA_CRIADA', 'Pendência criada', 3, NULL),
(18, '2026-10-03 23:16:06.000000', b'1', '/negociacoes/13', 'Devolver DINHEIRO ao comprador Roberto Lima.', 'PENDENCIA_CRIADA', 'Pendência criada', 1, NULL),
(19, '2026-10-03 23:16:06.000000', b'1', '/negociacoes/13', 'Devolver DINHEIRO ao comprador Roberto Lima.', 'PENDENCIA_CRIADA', 'Pendência criada', 2, NULL),
(20, '2026-10-03 23:36:30.000000', b'1', NULL, 'Matheus Urias iniciou uma negociação com Ana Souza para o veículo Corolla XEi.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 14),
(21, '2026-10-03 23:36:30.000000', b'1', NULL, 'Matheus Urias iniciou uma negociação com Ana Souza para o veículo Corolla XEi.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 14),
(22, '2026-10-03 23:49:48.000000', b'1', '/negociacoes/14', 'Devolver PIX ao comprador Ana Souza.', 'PENDENCIA_CRIADA', 'Pendência criada', 1, NULL),
(23, '2026-10-03 23:49:48.000000', b'1', '/negociacoes/14', 'Devolver PIX ao comprador Ana Souza.', 'PENDENCIA_CRIADA', 'Pendência criada', 2, NULL),
(24, '2026-10-05 13:07:40.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com José da Silva para o veículo City Touring.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 15),
(25, '2026-10-05 13:07:40.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com José da Silva para o veículo City Touring.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 15),
(26, '2026-10-05 13:08:35.000000', b'1', '/negociacoes/15', 'Devolver DINHEIRO ao comprador José da Silva.', 'PENDENCIA_CRIADA', 'Pendência criada', 3, NULL),
(27, '2026-10-05 13:08:35.000000', b'1', '/negociacoes/15', 'Devolver DINHEIRO ao comprador José da Silva.', 'PENDENCIA_CRIADA', 'Pendência criada', 1, NULL),
(28, '2026-10-05 13:08:35.000000', b'1', '/negociacoes/15', 'Devolver DINHEIRO ao comprador José da Silva.', 'PENDENCIA_CRIADA', 'Pendência criada', 2, NULL),
(41, '2026-10-05 14:24:46.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Eduardo Martins para o veículo Tiggo 7 Sport.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 22),
(42, '2026-10-05 14:24:46.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Eduardo Martins para o veículo Tiggo 7 Sport.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 22),
(43, '2026-10-05 14:25:25.000000', b'1', '/negociacoes/22', 'Devolver PIX ao comprador Eduardo Martins.', 'PENDENCIA_CRIADA', 'Pendência criada', 4, NULL),
(44, '2026-10-05 14:25:25.000000', b'1', '/negociacoes/22', 'Devolver PIX ao comprador Eduardo Martins.', 'PENDENCIA_CRIADA', 'Pendência criada', 1, NULL),
(45, '2026-10-05 14:25:25.000000', b'1', '/negociacoes/22', 'Devolver PIX ao comprador Eduardo Martins.', 'PENDENCIA_CRIADA', 'Pendência criada', 2, NULL),
(46, '2026-10-05 14:27:04.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Eduardo Martins.\" foi resolvida por Matheus Urias.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 4, 5),
(47, '2026-10-05 14:47:14.000000', b'1', NULL, 'pietro mascolvis solicitou a alteração do login de pietro22 para p22.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 1, 13),
(48, '2026-10-05 14:47:14.000000', b'1', NULL, 'pietro mascolvis solicitou a alteração do login de pietro22 para p22.', 'SOLICITACAO_LOGIN', 'Solicitação de alteração de login', 2, 13),
(49, '2026-10-05 16:48:50.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Juliana Ferreira para o veículo Onix LT.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 23),
(50, '2026-10-05 16:48:50.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Juliana Ferreira para o veículo Onix LT.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 23),
(51, '2026-10-05 16:50:00.000000', b'1', '/negociacoes/23', 'Devolver PIX ao comprador Juliana Ferreira.', 'PENDENCIA_CRIADA', 'Pendência criada', 3, NULL),
(52, '2026-10-05 16:50:00.000000', b'1', '/negociacoes/23', 'Devolver PIX ao comprador Juliana Ferreira.', 'PENDENCIA_CRIADA', 'Pendência criada', 1, NULL),
(53, '2026-10-05 16:50:00.000000', b'1', '/negociacoes/23', 'Devolver PIX ao comprador Juliana Ferreira.', 'PENDENCIA_CRIADA', 'Pendência criada', 2, NULL),
(54, '2026-10-05 16:56:46.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Juliana Ferreira.\" foi resolvida por João Pedro Lisboa.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 3, 6),
(55, '2026-10-05 16:56:48.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Juliana Ferreira.\" foi resolvida por João Pedro Lisboa.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 3, 6),
(56, '2026-10-05 16:56:48.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Juliana Ferreira.\" foi resolvida por João Pedro Lisboa.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 3, 6),
(57, '2026-10-05 16:56:49.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Juliana Ferreira.\" foi resolvida por João Pedro Lisboa.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 3, 6),
(58, '2026-10-05 16:56:49.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Juliana Ferreira.\" foi resolvida por João Pedro Lisboa.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 3, 6),
(59, '2026-10-05 16:56:52.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Juliana Ferreira.\" foi resolvida por João Pedro Lisboa.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 3, 6),
(60, '2026-10-05 17:00:47.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Fernanda Costa para o veículo Yaris XLS.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 24),
(61, '2026-10-05 17:00:47.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Fernanda Costa para o veículo Yaris XLS.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 24),
(66, '2026-10-05 17:04:14.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Eduardo Martins para o veículo Duster.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 27),
(67, '2026-10-05 17:04:14.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Eduardo Martins para o veículo Duster.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 27),
(68, '2026-10-05 17:07:41.000000', b'1', NULL, 'Devolver PIX ao comprador Fernanda Costa.', 'PENDENCIA_CRIADA', 'Pendência criada', 4, 7),
(69, '2026-10-05 17:07:42.000000', b'1', NULL, 'Devolver PIX ao comprador Fernanda Costa.', 'PENDENCIA_CRIADA', 'Pendência criada', 1, 7),
(70, '2026-10-05 17:07:42.000000', b'1', NULL, 'Devolver PIX ao comprador Fernanda Costa.', 'PENDENCIA_CRIADA', 'Pendência criada', 2, 7),
(71, '2026-10-05 17:11:28.000000', b'1', NULL, 'A pendência \"Devolver PIX ao comprador Fernanda Costa.\" foi resolvida por Matheus Urias.', 'PENDENCIA_RESOLVIDA', 'Pendência resolvida', 4, 7),
(72, '2026-10-05 17:29:55.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Ana Souza para o veículo City Touring.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 28),
(73, '2026-10-05 17:29:55.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Ana Souza para o veículo City Touring.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 28),
(74, '2026-10-05 17:30:17.000000', b'1', NULL, 'A negociação #28 com Ana Souza foi cancelada por João Pedro Lisboa.', 'NEGOCIACAO_CANCELADA', 'Negociação cancelada', 1, 28),
(75, '2026-10-05 17:30:18.000000', b'1', NULL, 'A negociação #28 com Ana Souza foi cancelada por João Pedro Lisboa.', 'NEGOCIACAO_CANCELADA', 'Negociação cancelada', 2, 28),
(76, '2026-10-05 17:45:22.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com André Carvalho para o veículo Versa Exclusive.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 29),
(77, '2026-10-05 17:45:22.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com André Carvalho para o veículo Versa Exclusive.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 29),
(78, '2026-10-05 18:23:48.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Bruno dantas coelho para o veículo Yaris XLS.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 30),
(79, '2026-10-05 18:23:51.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Bruno dantas coelho para o veículo Yaris XLS.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 30),
(80, '2026-10-05 18:24:12.000000', b'1', NULL, 'João Pedro Lisboa realizou a venda do veículo Yaris XLS para Bruno dantas coelho.', 'VENDA_REALIZADA', 'Venda realizada', 1, 14),
(81, '2026-10-05 18:24:12.000000', b'1', NULL, 'João Pedro Lisboa realizou a venda do veículo Yaris XLS para Bruno dantas coelho.', 'VENDA_REALIZADA', 'Venda realizada', 2, 14),
(82, '2026-10-05 18:47:24.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Patrícia Nunes para o veículo Tracker LT 1.0 Turbo.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 31),
(83, '2026-10-05 18:47:24.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Patrícia Nunes para o veículo Tracker LT 1.0 Turbo.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 31),
(84, '2026-10-05 18:49:51.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Patrícia Nunes para o veículo Tracker LT 1.0 Turbo.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 32),
(85, '2026-10-05 18:49:51.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Patrícia Nunes para o veículo Tracker LT 1.0 Turbo.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 32),
(86, '2026-10-05 18:50:42.000000', b'1', NULL, 'pietro mascolvis realizou a venda do veículo Tracker LT 1.0 Turbo para Patrícia Nunes.', 'VENDA_REALIZADA', 'Venda realizada', 1, 15),
(87, '2026-10-05 18:50:42.000000', b'1', NULL, 'pietro mascolvis realizou a venda do veículo Tracker LT 1.0 Turbo para Patrícia Nunes.', 'VENDA_REALIZADA', 'Venda realizada', 2, 15),
(88, '2026-10-05 18:51:00.000000', b'1', NULL, 'A negociação #32 com Patrícia Nunes foi cancelada por pietro mascolvis.', 'NEGOCIACAO_CANCELADA', 'Negociação cancelada', 1, 32),
(89, '2026-10-05 18:51:00.000000', b'1', NULL, 'A negociação #32 com Patrícia Nunes foi cancelada por pietro mascolvis.', 'NEGOCIACAO_CANCELADA', 'Negociação cancelada', 2, 32),
(90, '2026-10-07 22:02:13.000000', b'1', NULL, 'O repasse do veículo Civic EXL foi quitado por Carlos Almeida.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 9),
(91, '2026-10-07 22:02:13.000000', b'1', NULL, 'O repasse do veículo Civic EXL foi quitado por Carlos Almeida.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 9),
(92, '2026-10-07 22:03:00.000000', b'1', NULL, 'O repasse do veículo Tracker LT 1.0 Turbo foi quitado por Carlos Almeida.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 10),
(93, '2026-10-07 22:03:00.000000', b'1', NULL, 'O repasse do veículo Tracker LT 1.0 Turbo foi quitado por Carlos Almeida.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 10),
(94, '2026-10-07 22:26:30.000000', b'1', NULL, 'O repasse do veículo Compass Longitude foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 11),
(95, '2026-10-07 22:26:30.000000', b'1', NULL, 'O repasse do veículo Compass Longitude foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 11),
(96, '2026-10-07 22:27:02.000000', b'1', NULL, 'O repasse do veículo Yaris XLS foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 12),
(97, '2026-10-07 22:27:02.000000', b'1', NULL, 'O repasse do veículo Yaris XLS foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 12),
(98, '2026-10-07 22:27:34.000000', b'1', NULL, 'O repasse do veículo Versa Exclusive foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 13),
(99, '2026-10-07 22:27:34.000000', b'1', NULL, 'O repasse do veículo Versa Exclusive foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 13),
(100, '2026-10-07 22:28:24.000000', b'1', NULL, 'O repasse do veículo Duster foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 14),
(101, '2026-10-07 22:28:24.000000', b'1', NULL, 'O repasse do veículo Duster foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 14),
(102, '2026-10-07 22:29:14.000000', b'1', NULL, 'O repasse do veículo civic foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 15),
(103, '2026-10-07 22:29:14.000000', b'1', NULL, 'O repasse do veículo civic foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 15),
(104, '2026-10-09 12:53:46.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Gustavo Freitas para o veículo Virtus Comfortline 1.0 TSI.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 33),
(105, '2026-10-09 12:53:46.000000', b'0', NULL, 'João Pedro Lisboa iniciou uma negociação com Gustavo Freitas para o veículo Virtus Comfortline 1.0 TSI.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 33),
(106, '2026-10-09 13:06:33.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Fernanda Costa para o veículo Creta Action 1.6.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 34),
(107, '2026-10-09 13:06:33.000000', b'0', NULL, 'pietro mascolvis iniciou uma negociação com Fernanda Costa para o veículo Creta Action 1.6.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 34),
(108, '2026-10-09 13:06:57.000000', b'1', NULL, 'pietro mascolvis realizou a venda do veículo Creta Action 1.6 para Fernanda Costa.', 'VENDA_REALIZADA', 'Venda realizada', 1, 17),
(109, '2026-10-09 13:06:57.000000', b'0', NULL, 'pietro mascolvis realizou a venda do veículo Creta Action 1.6 para Fernanda Costa.', 'VENDA_REALIZADA', 'Venda realizada', 2, 17),
(110, '2026-10-09 13:08:39.000000', b'1', NULL, 'Matheus Urias iniciou uma negociação com Camila Ribeiro para o veículo Nivus Highline.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 35),
(111, '2026-10-09 13:08:39.000000', b'0', NULL, 'Matheus Urias iniciou uma negociação com Camila Ribeiro para o veículo Nivus Highline.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 35),
(112, '2026-10-09 13:08:49.000000', b'1', NULL, 'Matheus Urias realizou a venda do veículo Nivus Highline para Camila Ribeiro.', 'VENDA_REALIZADA', 'Venda realizada', 1, 18),
(113, '2026-10-09 13:08:49.000000', b'0', NULL, 'Matheus Urias realizou a venda do veículo Nivus Highline para Camila Ribeiro.', 'VENDA_REALIZADA', 'Venda realizada', 2, 18),
(114, '2026-10-09 13:08:56.000000', b'1', NULL, 'A entrega do veículo Nivus Highline ao comprador Camila Ribeiro foi concluída.', 'ENTREGA_CONCLUIDA', 'Entrega concluída', 1, 18),
(115, '2026-10-09 13:08:56.000000', b'0', NULL, 'A entrega do veículo Nivus Highline ao comprador Camila Ribeiro foi concluída.', 'ENTREGA_CONCLUIDA', 'Entrega concluída', 2, 18),
(116, '2026-10-10 17:59:48.000000', b'1', NULL, 'O repasse do veículo Virtus Comfortline 1.0 TSI foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 16),
(117, '2026-10-10 17:59:48.000000', b'0', NULL, 'O repasse do veículo Virtus Comfortline 1.0 TSI foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 16),
(118, '2026-10-13 23:28:14.000000', b'1', NULL, 'O repasse do veículo Nivus Highline foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 17),
(119, '2026-10-13 23:28:14.000000', b'0', NULL, 'O repasse do veículo Nivus Highline foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 17),
(120, '2026-10-16 11:58:28.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Pedro Santos para o veículo T-Cross 200 TSI.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 36),
(121, '2026-10-16 11:58:28.000000', b'0', NULL, 'João Pedro Lisboa iniciou uma negociação com Pedro Santos para o veículo T-Cross 200 TSI.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 36),
(122, '2026-10-16 12:05:58.000000', b'1', NULL, 'João Pedro Lisboa realizou a venda do veículo T-Cross 200 TSI para Pedro Santos.', 'VENDA_REALIZADA', 'Venda realizada', 1, 19),
(123, '2026-10-16 12:05:58.000000', b'0', NULL, 'João Pedro Lisboa realizou a venda do veículo T-Cross 200 TSI para Pedro Santos.', 'VENDA_REALIZADA', 'Venda realizada', 2, 19),
(124, '2026-10-16 13:57:10.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Vanessa Rocha para o veículo Compass Longitude 1.3.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 37),
(125, '2026-10-16 13:57:10.000000', b'0', NULL, 'João Pedro Lisboa iniciou uma negociação com Vanessa Rocha para o veículo Compass Longitude 1.3.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 37),
(126, '2026-10-16 14:03:27.000000', b'1', NULL, 'João Pedro Lisboa realizou a venda do veículo Compass Longitude 1.3 para Vanessa Rocha.', 'VENDA_REALIZADA', 'Venda realizada', 1, 20),
(127, '2026-10-16 14:03:27.000000', b'0', NULL, 'João Pedro Lisboa realizou a venda do veículo Compass Longitude 1.3 para Vanessa Rocha.', 'VENDA_REALIZADA', 'Venda realizada', 2, 20),
(128, '2026-10-16 22:19:56.000000', b'1', NULL, 'João Pedro Lisboa iniciou uma negociação com Geovanny silve durlony para o veículo Onix LT.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 38),
(129, '2026-10-16 22:19:56.000000', b'0', NULL, 'João Pedro Lisboa iniciou uma negociação com Geovanny silve durlony para o veículo Onix LT.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 38),
(130, '2026-10-16 22:20:08.000000', b'1', NULL, 'João Pedro Lisboa realizou a venda do veículo Onix LT para Geovanny silve durlony.', 'VENDA_REALIZADA', 'Venda realizada', 1, 21),
(131, '2026-10-16 22:20:08.000000', b'0', NULL, 'João Pedro Lisboa realizou a venda do veículo Onix LT para Geovanny silve durlony.', 'VENDA_REALIZADA', 'Venda realizada', 2, 21),
(132, '2026-10-16 22:20:27.000000', b'1', NULL, 'A entrega do veículo Onix LT ao comprador Geovanny silve durlony foi concluída.', 'ENTREGA_CONCLUIDA', 'Entrega concluída', 1, 21),
(133, '2026-10-16 22:20:27.000000', b'0', NULL, 'A entrega do veículo Onix LT ao comprador Geovanny silve durlony foi concluída.', 'ENTREGA_CONCLUIDA', 'Entrega concluída', 2, 21),
(134, '2026-10-16 22:20:27.000000', b'1', NULL, 'A entrega do veículo Onix LT ao comprador Geovanny silve durlony foi concluída.', 'ENTREGA_CONCLUIDA', 'Entrega concluída', 3, 21),
(135, '2026-10-16 22:23:53.000000', b'1', NULL, 'O repasse do veículo Creta Action 1.6 foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 18),
(136, '2026-10-16 22:23:53.000000', b'0', NULL, 'O repasse do veículo Creta Action 1.6 foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 18),
(137, '2026-10-16 22:24:43.000000', b'1', NULL, 'O repasse do veículo T-Cross 200 TSI foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 19),
(138, '2026-10-16 22:24:43.000000', b'0', NULL, 'O repasse do veículo T-Cross 200 TSI foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 19),
(139, '2026-10-16 22:25:22.000000', b'1', NULL, 'O repasse do veículo Compass Longitude 1.3 foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 20),
(140, '2026-10-16 22:25:22.000000', b'0', NULL, 'O repasse do veículo Compass Longitude 1.3 foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 20),
(141, '2026-10-16 22:28:36.000000', b'1', NULL, 'O repasse do veículo Onix LT foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 1, 21),
(142, '2026-10-16 22:28:36.000000', b'0', NULL, 'O repasse do veículo Onix LT foi quitado por Matheus Urias.', 'REPASSE_QUITADO', 'Repasse quitado', 2, 21),
(143, '2026-10-16 22:30:20.000000', b'1', NULL, 'pietro mascolvis iniciou uma negociação com Ana Souza para o veículo Argo Drive 1.0.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 1, 39),
(144, '2026-10-16 22:30:20.000000', b'0', NULL, 'pietro mascolvis iniciou uma negociação com Ana Souza para o veículo Argo Drive 1.0.', 'NEGOCIACAO_INICIADA', 'Negociação iniciada', 2, 39),
(145, '2026-10-16 22:31:31.000000', b'1', NULL, 'A negociação #39 com Ana Souza foi cancelada por pietro mascolvis.', 'NEGOCIACAO_CANCELADA', 'Negociação cancelada', 1, 39),
(146, '2026-10-16 22:31:31.000000', b'0', NULL, 'A negociação #39 com Ana Souza foi cancelada por pietro mascolvis.', 'NEGOCIACAO_CANCELADA', 'Negociação cancelada', 2, 39);

-- --------------------------------------------------------

--
-- Estrutura para tabela `pagamentos`
--

CREATE TABLE `pagamentos` (
  `id` bigint(20) NOT NULL,
  `entrada` double DEFAULT NULL,
  `parcelas` int(11) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `valor_parcela` double DEFAULT NULL,
  `instituicao_financeira` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `pagamentos_repasse`
--

CREATE TABLE `pagamentos_repasse` (
  `id` bigint(20) NOT NULL,
  `data_pagamento` date NOT NULL,
  `forma_pagamento` varchar(255) NOT NULL,
  `observacao` varchar(500) DEFAULT NULL,
  `valor` double NOT NULL,
  `repasse_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `pagamentos_repasse`
--

INSERT INTO `pagamentos_repasse` (`id`, `data_pagamento`, `forma_pagamento`, `observacao`, `valor`, `repasse_id`) VALUES
(1, '2026-09-01', 'PIX', 'Pago de uma só vez', 162900, 2),
(2, '2026-09-01', 'TRANSFERENCIA', 'Pago de uma só vez', 128500, 3),
(3, '2026-08-31', 'TRANSFERENCIA', '', 45000, 6),
(4, '2026-09-01', 'PIX', '', 44500, 6),
(5, '2026-08-26', 'DINHEIRO', '', 50000, 4),
(6, '2026-09-01', 'PIX', '', 64900, 4),
(7, '2026-09-01', 'TRANSFERENCIA', 'Pago de uma só vez', 52900, 5),
(8, '2026-09-18', 'PIX', '', 53000, 7),
(9, '2026-09-18', 'TRANSFERENCIA', 'Quintando', 41900, 7),
(10, '2026-09-30', 'PIX', 'Valor integral de uma só vez ', 63900, 8),
(11, '2026-09-18', 'TRANSFERENCIA', 'Parcial', 70000, 9),
(12, '2026-10-03', 'TRANSFERENCIA', '', 56500, 9),
(13, '2026-10-05', 'TRANSFERENCIA', '', 105900, 10),
(14, '2026-10-07', 'PIX', '', 168000, 11),
(15, '2026-10-02', 'TRANSFERENCIA', '', 108700, 12),
(16, '2026-10-02', 'TRANSFERENCIA', '', 116900, 13),
(17, '2026-10-01', 'TRANSFERENCIA', '', 65000, 14),
(18, '2026-10-05', 'PIX', '', 56000, 15),
(19, '2026-10-10', 'TRANSFERENCIA', '', 89900, 16),
(20, '2026-10-08', 'PIX', 'Valor parcial', 65000, 17),
(21, '2026-10-13', 'PIX', 'Quintando', 77900, 17),
(22, '2026-10-07', 'TRANSFERENCIA', '', 91500, 18),
(23, '2026-10-01', 'PIX', '', 52000, 19),
(24, '2026-10-06', 'DINHEIRO', '', 56500, 19),
(25, '2026-10-09', 'PIX', '', 139900, 20),
(26, '2026-10-08', 'PIX', '', 58500, 21),
(27, '2026-10-13', 'PIX', '', 10000, 21);

-- --------------------------------------------------------

--
-- Estrutura para tabela `pendencia_negociacao`
--

CREATE TABLE `pendencia_negociacao` (
  `id` bigint(20) NOT NULL,
  `data_criacao` datetime(6) NOT NULL,
  `data_resolucao` datetime(6) DEFAULT NULL,
  `observacao_resolucao` varchar(500) DEFAULT NULL,
  `status` enum('PENDENTE','RESOLVIDA') NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `negociacao_id` bigint(20) NOT NULL,
  `resolvida_por_id` bigint(20) DEFAULT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `forma_pagamento_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `pendencia_negociacao`
--

INSERT INTO `pendencia_negociacao` (`id`, `data_criacao`, `data_resolucao`, `observacao_resolucao`, `status`, `tipo`, `negociacao_id`, `resolvida_por_id`, `descricao`, `valor`, `forma_pagamento_id`) VALUES
(1, '2026-08-15 22:29:20.000000', '2026-10-04 00:04:24.000000', NULL, 'RESOLVIDA', 'DEVOLUCAO_CLIENTE', 4, 1, 'Devolver PIX ao comprador Ana Souza.', 38900, 7),
(2, '2026-10-03 23:16:06.000000', NULL, NULL, 'PENDENTE', 'DEVOLUCAO_CLIENTE', 13, NULL, 'Devolver DINHEIRO ao comprador Roberto Lima.', 80000, 25),
(3, '2026-10-03 23:49:48.000000', '2026-10-04 00:04:08.000000', NULL, 'RESOLVIDA', 'DEVOLUCAO_CLIENTE', 14, 1, 'Devolver PIX ao comprador Ana Souza.', 49900, 28),
(4, '2026-10-05 13:08:35.000000', '2026-10-05 13:08:54.000000', NULL, 'RESOLVIDA', 'DEVOLUCAO_CLIENTE', 15, 3, 'Devolver DINHEIRO ao comprador José da Silva.', 37800, 29),
(5, '2026-10-05 14:25:25.000000', '2026-10-05 14:27:03.000000', NULL, 'RESOLVIDA', 'DEVOLUCAO_CLIENTE', 22, 1, 'Devolver PIX ao comprador Eduardo Martins.', 76900, 49),
(6, '2026-10-05 16:50:00.000000', '2026-10-05 16:56:49.000000', NULL, 'RESOLVIDA', 'DEVOLUCAO_CLIENTE', 23, 3, 'Devolver PIX ao comprador Juliana Ferreira.', 20000, 51),
(7, '2026-10-05 17:07:41.000000', '2026-10-05 17:11:28.000000', NULL, 'RESOLVIDA', 'DEVOLUCAO_CLIENTE', 24, 1, 'Devolver PIX ao comprador Fernanda Costa.', 25000, 53);

-- --------------------------------------------------------

--
-- Estrutura para tabela `repasses`
--

CREATE TABLE `repasses` (
  `id` bigint(20) NOT NULL,
  `valor_devido` double NOT NULL,
  `venda_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `repasses`
--

INSERT INTO `repasses` (`id`, `valor_devido`, `venda_id`) VALUES
(2, 162900, 1),
(3, 128500, 2),
(4, 114900, 3),
(5, 52900, 4),
(6, 89500, 5),
(7, 94900, 6),
(8, 63900, 7),
(9, 126500, 8),
(10, 105900, 15),
(11, 168000, 9),
(12, 108700, 14),
(13, 116900, 12),
(14, 65000, 11),
(15, 56000, 10),
(16, 89900, 16),
(17, 142900, 18),
(18, 91500, 17),
(19, 108500, 19),
(20, 139900, 20),
(21, 68500, 21);

-- --------------------------------------------------------

--
-- Estrutura para tabela `solicitacao_alteracao_login`
--

CREATE TABLE `solicitacao_alteracao_login` (
  `id` bigint(20) NOT NULL,
  `data_solicitacao` datetime(6) DEFAULT NULL,
  `login_atual` varchar(255) DEFAULT NULL,
  `novo_login` varchar(255) DEFAULT NULL,
  `status` enum('APROVADA','PENDENTE','RECUSADA') DEFAULT NULL,
  `usuario_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `solicitacao_alteracao_login`
--

INSERT INTO `solicitacao_alteracao_login` (`id`, `data_solicitacao`, `login_atual`, `novo_login`, `status`, `usuario_id`) VALUES
(1, '2026-09-25 22:53:53.000000', 'lisboa', 'joaopedro', 'APROVADA', 3),
(2, '2026-09-27 00:15:47.000000', 'mascolvis', 'pietro.mas', 'RECUSADA', 4),
(3, '2026-09-27 00:23:16.000000', 'mascolvis', 'pietro22', 'APROVADA', 4),
(4, '2026-09-30 18:24:53.000000', 'joaopedro', 'Jpedro', 'RECUSADA', 3),
(5, '2026-09-30 18:49:59.000000', 'pietro22', 'P22', 'RECUSADA', 4),
(6, '2026-10-02 23:02:47.000000', 'pietro22', 'P22', 'RECUSADA', 4),
(7, '2026-10-02 23:26:43.000000', 'pietro22', 'PietroTest', 'RECUSADA', 4),
(8, '2026-10-03 00:00:48.000000', 'joaopedro', 'jpedro', 'RECUSADA', 3),
(9, '2026-10-03 14:47:38.000000', 'pietro22', 'p22', 'RECUSADA', 4),
(10, '2026-10-03 15:51:12.000000', 'almeida', 'carlos.almeida', 'APROVADA', 2),
(11, '2026-10-03 16:13:54.000000', 'pietro22', 'p22', 'RECUSADA', 4),
(12, '2026-10-03 16:19:31.000000', 'carlos.almeida', 'calmeida', 'RECUSADA', 2),
(13, '2026-10-05 14:47:14.000000', 'pietro22', 'p22', 'RECUSADA', 4);

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `login` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `perfil` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `usuarios`
--

INSERT INTO `usuarios` (`id`, `login`, `nome`, `perfil`, `senha`) VALUES
(1, 'urias', 'Matheus Urias', 'DONO', '1234'),
(2, 'carlos.almeida', 'Carlos Almeida', 'GERENTE', '1234'),
(3, 'joaopedro', 'João Pedro Lisboa', 'VENDEDOR', '4321'),
(4, 'pietro22', 'pietro mascolvis', 'VENDEDOR', '2026');

-- --------------------------------------------------------

--
-- Estrutura para tabela `veiculos`
--

CREATE TABLE `veiculos` (
  `id` bigint(20) NOT NULL,
  `ano` int(11) DEFAULT NULL,
  `cor` varchar(255) DEFAULT NULL,
  `cpf_proprietario` varchar(255) DEFAULT NULL,
  `data_entrada` date DEFAULT NULL,
  `email_proprietario` varchar(255) DEFAULT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `nome_proprietario` varchar(255) DEFAULT NULL,
  `placa` varchar(255) DEFAULT NULL,
  `preco` double DEFAULT NULL,
  `telefone_proprietario` varchar(255) DEFAULT NULL,
  `vendido` bit(1) DEFAULT NULL,
  `status` enum('DISPONIVEL','RESERVADO','VENDIDO') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `veiculos`
--

INSERT INTO `veiculos` (`id`, `ano`, `cor`, `cpf_proprietario`, `data_entrada`, `email_proprietario`, `marca`, `nome`, `nome_proprietario`, `placa`, `preco`, `telefone_proprietario`, `vendido`, `status`) VALUES
(1, 2022, 'Prata', '12345678901', '2026-06-15', 'ricardo@email.com', 'Chevrolet', 'Onix LT', 'Ricardo Lima', 'ABC1D23', 68500, '(44) 99752-4688', b'0', 'VENDIDO'),
(2, 2021, 'Branco', '23456789012', '2026-07-04', 'marcos@email.com', 'Hyundai', 'HB20 Comfort', 'Marcos Souza', 'EFG4H56', 63900, '(43) 98430-6552', b'0', 'VENDIDO'),
(3, 2023, 'Preto', '34567890123', '2026-05-21', 'fernando@email.com', 'Toyota', 'Corolla XEi', 'Fernando Alves', 'IJK7L89', 149900, '(43) 99833-3422', b'0', 'DISPONIVEL'),
(4, 2022, 'Cinza', '45678901234', '2026-04-09', 'juliana@email.com', 'Jeep', 'Compass Longitude', 'Juliana Costa', 'MNO1P23', 168000, '(43) 99875-3636', b'0', 'VENDIDO'),
(5, 2020, 'Vermelho', '56789012345', '2026-07-05', 'paulo@email.com', 'Volkswagen', 'Gol 1.0', 'Paulo Henrique', 'QRS4T56', 52900, '(43) 99888-5555', b'0', 'VENDIDO'),
(6, 2023, 'Branco', '81492657318', '2026-07-01', 'marcio.p@email.com', 'Volkswagen', 'Polo Comfortline', 'Márcio Pereira', 'RTA2J41', 89500, '(43) 98482-6139', b'0', 'VENDIDO'),
(7, 2022, 'Cinza', '62571834920', '2026-07-02', 'claudia.f@email.com', 'Hyundai', 'Creta Platinum', 'Cláudia Ferreira', 'GKL8C72', 138900, '(43) 99637-2485', b'0', 'DISPONIVEL'),
(8, 2021, 'Prata', '43729168504', '2026-07-03', 'leonardo.c@email.com', 'Honda', 'Civic EXL', 'Leonardo Costa', 'XNB5A18', 126500, '(43) 99145-7328', b'0', 'VENDIDO'),
(9, 2024, 'Cinza', '31847296501', '2026-07-11', 'bruno.andrade@email.com', 'Volkswagen', 'Nivus Highline', 'Bruno Andrade', 'MTR4A52', 142900, '(43) 98471-2653', b'0', 'VENDIDO'),
(10, 2023, 'Branco', '47291683520', '2026-07-12', 'carla.m@email.com', 'Nissan', 'Kicks Advance', 'Carla Menezes', 'FLM8B17', 128500, '(43) 99163-4582', b'0', 'VENDIDO'),
(11, 2024, 'Preto', '65019284736', '2026-07-13', 'ricardo.s@email.com', 'Honda', 'City Touring', 'Ricardo Souza', 'VGS3C84', 137800, '(43) 98742-6518', b'0', 'DISPONIVEL'),
(12, 2023, 'Prata', '91428367510', '2026-07-14', 'fernanda.l@email.com', 'Fiat', 'Cronos Drive', 'Fernanda Lopes', 'KDP6D45', 94900, '(43) 99538-2746', b'0', 'VENDIDO'),
(13, 2024, 'Azul', '18374652940', '2026-07-15', 'marcelo.r@email.com', 'Nissan', 'Sentra Advance', 'Marcelo Rocha', 'PTA2E91', 162900, '(43) 98642-1937', b'0', 'VENDIDO'),
(14, 2022, 'Branco', '54821693725', '2026-07-16', 'patricia.g@email.com', 'Toyota', 'Yaris XLS', 'Patrícia Gomes', 'LWE5F36', 108700, '(43) 99814-7265', b'0', 'VENDIDO'),
(15, 2023, 'Cinza', '82659317408', '2026-07-17', 'daniel.m@email.com', 'Chevrolet', 'Spin Premier', 'Daniel Martins', 'ZRT7G64', 132500, '(43) 98357-4612', b'0', 'DISPONIVEL'),
(16, 2024, 'Preto', '23981467590', '2026-07-18', 'vanessa.d@email.com', 'Fiat', 'Fastback Limited', 'Vanessa Duarte', 'BNC9H28', 154900, '(43) 99247-3186', b'0', 'DISPONIVEL'),
(17, 2023, 'Prata', '68417392015', '2026-07-19', 'alexandre.p@email.com', 'Honda', 'HR-V EX', 'Alexandre Pinto', 'HGF1I73', 169900, '(43) 98456-7139', b'0', 'DISPONIVEL'),
(18, 2024, 'Vermelho', '97134628540', '2026-07-20', 'cristiane.a@email.com', 'Fiat', 'Toro Volcano', 'Cristiane Alves', 'QAS4J15', 189500, '(43) 99753-8421', b'0', 'DISPONIVEL'),
(19, 2023, 'Prata', '34598276108', '2026-07-21', 'sergio.b@email.com', 'Nissan', 'Versa Exclusive', 'Sérgio Batista', 'RPK8A60', 116900, '(43) 98531-6247', b'0', 'VENDIDO'),
(20, 2022, 'Branco', '12837546920', '2026-07-22', 'priscila.m@email.com', 'Jeep', 'Compass Sport', 'Priscila Moraes', 'XLT3B94', 156800, '(43) 99362-4815', b'0', 'DISPONIVEL'),
(21, 2024, 'Azul', '78346195204', '2026-07-23', 'tiago.f@email.com', 'Chevrolet', 'Onix Premier', 'Tiago Fernandes', 'JFD5C42', 114900, '(43) 98429-5731', b'0', 'VENDIDO'),
(22, 2024, 'Cinza', '51629483701', '2026-07-24', 'juliano.c@email.com', 'Toyota', 'Corolla Cross XRX', 'Juliano Costa', 'NGH6D81', 198500, '(43) 99684-1537', b'0', 'DISPONIVEL'),
(23, 2025, 'Preto', '20493768155', '2026-07-25', 'luciano.r@email.com', 'Caoa Chery', 'Tiggo 7 Sport', 'Luciano Ribeiro', 'SVM2E39', 176900, '(43) 98831-4276', b'0', 'DISPONIVEL'),
(24, 2018, 'Prata', '979.730.259-87', '2026-07-11', 'w.urias@hotmail.com', 'Renault', 'Duster', 'wanderley caetano urias', 'AVB7D77', 65000, '(43) 98409-8200', b'0', 'VENDIDO'),
(25, 2025, 'Branco', '854.751.239-00', '2026-07-12', 'ed-son@gmail.com', 'citroen', 'Basalti', 'edson barison', 'ASD5E77', 120000, '(44) 99725-4242', b'0', 'DISPONIVEL'),
(26, 2022, 'bordo', '854.365.125-97', '2026-07-13', 'antonio_medeiros@gmail.com', 'Jeep', 'jeep renegate', 'antonio maxismos medeiros', 'ASX7B56', 97000, '(43) 99672-0145', b'0', 'DISPONIVEL'),
(27, 2014, 'preto', '234.567.890-12', NULL, 'maria@email.com', 'hyundai', 'azera', 'Maria Oliveira', 'afg2e87', 77500, '(43) 98475-2142', NULL, 'DISPONIVEL'),
(28, 2017, 'preto', '874.236.519-02', '2026-09-07', 'adalto.noco@gmail.com', 'honda', 'civic', 'Adalto lezer nicome', 'aeb4c89', 56000, '(43) 98472-1015', NULL, 'VENDIDO'),
(29, 2018, 'Preto', '751.426.979-35', '2026-09-23', '', 'Toyota', 'Corolla XEi', 'André camargo vaz', 'CDX4C26', 44500, '(43) 99857-6114', NULL, 'DISPONIVEL'),
(30, 2021, 'Prata', '12345678901', '2026-10-05', 'carlos@email.com', 'Chevrolet', 'Onix LT 1.0', 'Carlos Henrique', 'BRA2A15', 67900, '43999990001', NULL, 'DISPONIVEL'),
(31, 2022, 'Branco', '23456789012', '2026-10-05', 'mariana@email.com', 'Hyundai', 'HB20 Comfort 1.0', 'Mariana Souza', 'HYU4B27', 69500, '43999990002', NULL, 'DISPONIVEL'),
(32, 2021, 'Cinza', '34567890123', '2026-10-05', 'ricardo@email.com', 'Fiat', 'Argo Drive 1.0', 'Ricardo Alves', 'ARG5C38', 65900, '43999990003', NULL, 'DISPONIVEL'),
(33, 2020, 'Preto', '45678901234', '2026-10-05', 'fernanda@email.com', 'Toyota', 'Corolla GLi 2.0', 'Fernanda Lima', 'COR7D41', 104900, '43999990004', NULL, 'DISPONIVEL'),
(34, 2022, 'Branco', '56789012345', '2026-10-05', 'paulo@email.com', 'Volkswagen', 'T-Cross 200 TSI', 'Paulo Mendes', 'TCR8E52', 108500, '43999990005', NULL, 'VENDIDO'),
(35, 2022, 'Cinza', '67890123456', '2026-10-05', 'juliana@email.com', 'Jeep', 'Compass Longitude 1.3', 'Juliana Martins', 'JEP3F64', 139900, '43999990006', NULL, 'VENDIDO'),
(36, 2019, 'Prata', '78901234567', '2026-10-05', 'andre@email.com', 'Honda', 'Civic EX 2.0', 'André Oliveira', 'CIV6G75', 105900, '43999990007', NULL, 'DISPONIVEL'),
(37, 2020, 'Vermelho', '89012345678', '2026-10-05', 'patricia@email.com', 'Jeep', 'Renegade Sport 1.8', 'Patricia Gomes', 'REN9H86', 82900, '43999990008', NULL, 'DISPONIVEL'),
(38, 2019, 'Branco', '90123456789', '2026-10-05', 'lucas@email.com', 'Ford', 'Ka SE 1.0', 'Lucas Ferreira', 'FOR2J97', 49900, '43999990009', NULL, 'DISPONIVEL'),
(39, 2020, 'Prata', '01234567890', '2026-10-05', 'renata@email.com', 'Renault', 'Sandero Zen 1.0', 'Renata Costa', 'SAN4K18', 52500, '43999990010', NULL, 'DISPONIVEL'),
(40, 2022, 'Azul', '11223344556', '2026-10-05', 'eduardo@email.com', 'Chevrolet', 'Tracker LT 1.0 Turbo', 'Eduardo Ribeiro', 'TRK5L29', 105900, '43999990011', NULL, 'DISPONIVEL'),
(41, 2021, 'Cinza', '22334455667', '2026-10-05', 'camila@email.com', 'Volkswagen', 'Virtus Comfortline 1.0 TSI', 'Camila Rocha', 'VIR7M31', 89900, '43999990012', NULL, 'VENDIDO'),
(42, 2023, 'Preto', '33445566778', '2026-10-05', 'marcelo@email.com', 'Fiat', 'Cronos Precision 1.3', 'Marcelo Batista', 'CRO8N42', 82900, '43999990013', NULL, 'DISPONIVEL'),
(43, 2021, 'Branco', '44556677889', '2026-10-05', 'vanessa@email.com', 'Hyundai', 'Creta Action 1.6', 'Vanessa Pereira', 'CRE3P53', 91500, '43999990014', NULL, 'VENDIDO'),
(44, 2020, 'Prata', '55667788990', '2026-10-05', 'roberto@email.com', 'Toyota', 'Yaris XL 1.3', 'Roberto Martins', 'YAR6R65', 76900, '43999990015', NULL, 'DISPONIVEL'),
(45, 2022, 'branco', '62418370915', '2026-10-09', 'gustavo.freitas@email.com', 'hyundai', 'hb20', 'Gustavo Freitas', 'gte5b22', 55600, '(43) 99416-5372', NULL, 'DISPONIVEL'),
(46, 2022, 'Vinho', '67890123456', '2026-10-09', 'fernanda@email.com', 'Fiat', 'Toro', 'Fernanda Costa', 'Gte4a22', 52000, '(43) 99635-2515', NULL, 'DISPONIVEL'),
(47, 2019, 'preto', '34567890123', '2026-10-16', 'pedro@email.com', 'chevrolet', 's10', 'Pedro Santos', 'awe4g65', 85900, '(11) 98542-1066', NULL, 'DISPONIVEL'),
(48, 2022, 'vinho', '38695172044', '2026-10-16', 'vanessa.rocha@email.com', 'chevrolet', 'onix', 'Vanessa Rocha', 'glh2e62', 65500, '(43) 98741-6208', NULL, 'DISPONIVEL'),
(49, 2010, 'vermelho', '520.412.539-77', '2026-10-19', '', 'Chevrolet', 'Agile', 'Fabricio host ferman', 'APK4758', 32900, '(47) 99924-1010', NULL, 'DISPONIVEL');

-- --------------------------------------------------------

--
-- Estrutura para tabela `veiculo_entrada`
--

CREATE TABLE `veiculo_entrada` (
  `id` bigint(20) NOT NULL,
  `ano` int(11) DEFAULT NULL,
  `cor` varchar(255) DEFAULT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `placa` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `veiculo_entrada`
--

INSERT INTO `veiculo_entrada` (`id`, `ano`, `cor`, `marca`, `nome`, `placa`) VALUES
(1, 2016, 'preto', 'fiat', 'uno', 'gfd7e65'),
(2, 2014, 'preto', 'hyundai', 'azera', 'afg2e87'),
(3, 2017, 'preto', 'honda', 'civic', 'aeb4c89'),
(4, 2022, 'branco', 'hyundai', 'hb20', 'gte5b22'),
(5, 2022, 'Vinho', 'Fiat', 'Toro', 'Gte4a22'),
(6, 2019, 'preto', 'chevrolet', 's10', 'awe4g65'),
(7, 2022, 'vinho', 'chevrolet', 'onix', 'glh2e62');

-- --------------------------------------------------------

--
-- Estrutura para tabela `vendas`
--

CREATE TABLE `vendas` (
  `id` bigint(20) NOT NULL,
  `data_venda` date DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `pagamento_id` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `vendedor_id` bigint(20) DEFAULT NULL,
  `negociacao_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `vendas`
--

INSERT INTO `vendas` (`id`, `data_venda`, `valor`, `cliente_id`, `pagamento_id`, `veiculo_id`, `vendedor_id`, `negociacao_id`) VALUES
(1, '2026-08-14', 162900, 10, NULL, 13, 3, 2),
(2, '2026-08-15', 128500, 3, NULL, 10, 3, 1),
(3, '2026-08-20', 114900, 11, NULL, 21, 1, 5),
(4, '2026-08-21', 52900, 19, NULL, 5, 4, 6),
(5, '2026-08-26', 89500, 2, NULL, 6, 1, 7),
(6, '2026-09-07', 94900, 20, NULL, 12, 4, 8),
(7, '2026-09-19', 63900, 15, NULL, 2, 4, 9),
(8, '2026-09-23', 126500, 7, NULL, 8, 1, 10),
(9, '2026-09-23', 168000, 21, NULL, 4, 4, 11),
(10, '2026-10-03', 56000, 19, NULL, 28, 3, 12),
(11, '2026-10-05', 65000, 9, NULL, 24, 4, 27),
(12, '2026-10-05', 116900, 13, NULL, 19, 4, 29),
(14, '2026-10-05', 108700, 21, NULL, 14, 3, 30),
(15, '2026-10-05', 105900, 12, NULL, 40, 4, 31),
(16, '2026-10-09', 89900, 17, NULL, 41, 3, 33),
(17, '2026-10-09', 91500, 6, NULL, 43, 4, 34),
(18, '2026-10-09', 142900, 10, NULL, 9, 1, 35),
(19, '2026-10-16', 108500, 3, NULL, 34, 3, 36),
(20, '2026-10-16', 139900, 16, NULL, 35, 3, 37),
(21, '2026-10-16', 68500, 22, NULL, 1, 3, 38);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `checklist_entrega`
--
ALTER TABLE `checklist_entrega`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKphrrnbmdll88yt5kbclt9mrhh` (`veiculo_id`);

--
-- Índices de tabela `checklist_entrega_itens`
--
ALTER TABLE `checklist_entrega_itens`
  ADD UNIQUE KEY `UKojrr53ak42lvhm4l2ofal8sb1` (`itens_id`),
  ADD KEY `FKhy4urljwbkb3d7kwo6sb1ju1n` (`checklist_entrega_id`);

--
-- Índices de tabela `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `configuracao_concessionaria`
--
ALTER TABLE `configuracao_concessionaria`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `forma_pagamento_negociacao`
--
ALTER TABLE `forma_pagamento_negociacao`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKg3onsuw5p54xuhkf0j7aiv0ru` (`veiculo_entrada_id`),
  ADD KEY `FKatc1hchm0o3p30m1dgtmv6882` (`negociacao_id`);

--
-- Índices de tabela `item_checklist_entrega`
--
ALTER TABLE `item_checklist_entrega`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKh1xgshou3xm5hcc1nsewrk159` (`checklist_id`);

--
-- Índices de tabela `negociacao`
--
ALTER TABLE `negociacao`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrb7c53n5bvomri20vywei7s0d` (`cliente_id`),
  ADD KEY `FKqnrmvt5p68mp39amumdywvop` (`veiculo_id`),
  ADD KEY `FKimplyup5kmc8r9qeo2x4ol2e9` (`vendedor_id`);

--
-- Índices de tabela `notificacoes`
--
ALTER TABLE `notificacoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3jcnk0ggmxklk5sjxm2plk5ml` (`usuario_id`);

--
-- Índices de tabela `pagamentos`
--
ALTER TABLE `pagamentos`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `pagamentos_repasse`
--
ALTER TABLE `pagamentos_repasse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4mfvaxovv06ds82ucb9lkn3fy` (`repasse_id`);

--
-- Índices de tabela `pendencia_negociacao`
--
ALTER TABLE `pendencia_negociacao`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKr8fvtlqyot78ig2okvibve61c` (`negociacao_id`),
  ADD KEY `FKmrqvf8trbxkglx7qo66x453ge` (`resolvida_por_id`),
  ADD KEY `FK3eqjxo5r8m73lm4pjdq93nssi` (`forma_pagamento_id`);

--
-- Índices de tabela `repasses`
--
ALTER TABLE `repasses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKd1g8amkhrrv9wd00hwr93p7w8` (`venda_id`);

--
-- Índices de tabela `solicitacao_alteracao_login`
--
ALTER TABLE `solicitacao_alteracao_login`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhxptdpmsn4twicp352j1qm6mx` (`usuario_id`);

--
-- Índices de tabela `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `veiculos`
--
ALTER TABLE `veiculos`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `veiculo_entrada`
--
ALTER TABLE `veiculo_entrada`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `vendas`
--
ALTER TABLE `vendas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKren4c05t3jgmw14i6pruqnyit` (`negociacao_id`),
  ADD KEY `FK18jb6atkgnouxy9b97unm4uue` (`cliente_id`),
  ADD KEY `FKto0nax9qj4dethg71834dc70y` (`pagamento_id`),
  ADD KEY `FK6sefd9hgm1cnkdn1t32612yt9` (`veiculo_id`),
  ADD KEY `FKmxl3oku9fexbd1gcx9xsys3op` (`vendedor_id`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `checklist_entrega`
--
ALTER TABLE `checklist_entrega`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de tabela `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de tabela `configuracao_concessionaria`
--
ALTER TABLE `configuracao_concessionaria`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de tabela `forma_pagamento_negociacao`
--
ALTER TABLE `forma_pagamento_negociacao`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=80;

--
-- AUTO_INCREMENT de tabela `item_checklist_entrega`
--
ALTER TABLE `item_checklist_entrega`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

--
-- AUTO_INCREMENT de tabela `negociacao`
--
ALTER TABLE `negociacao`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT de tabela `notificacoes`
--
ALTER TABLE `notificacoes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=147;

--
-- AUTO_INCREMENT de tabela `pagamentos`
--
ALTER TABLE `pagamentos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `pagamentos_repasse`
--
ALTER TABLE `pagamentos_repasse`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de tabela `pendencia_negociacao`
--
ALTER TABLE `pendencia_negociacao`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de tabela `repasses`
--
ALTER TABLE `repasses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de tabela `solicitacao_alteracao_login`
--
ALTER TABLE `solicitacao_alteracao_login`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de tabela `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de tabela `veiculos`
--
ALTER TABLE `veiculos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT de tabela `veiculo_entrada`
--
ALTER TABLE `veiculo_entrada`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de tabela `vendas`
--
ALTER TABLE `vendas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `checklist_entrega`
--
ALTER TABLE `checklist_entrega`
  ADD CONSTRAINT `FKiiqksvuj0xmsjgd2hcl3phk9i` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculos` (`id`);

--
-- Restrições para tabelas `checklist_entrega_itens`
--
ALTER TABLE `checklist_entrega_itens`
  ADD CONSTRAINT `FK2lxmv7y6xp49irndk4uu4enr7` FOREIGN KEY (`itens_id`) REFERENCES `item_checklist_entrega` (`id`),
  ADD CONSTRAINT `FKhy4urljwbkb3d7kwo6sb1ju1n` FOREIGN KEY (`checklist_entrega_id`) REFERENCES `checklist_entrega` (`id`);

--
-- Restrições para tabelas `forma_pagamento_negociacao`
--
ALTER TABLE `forma_pagamento_negociacao`
  ADD CONSTRAINT `FK1aywvajc04fcgk84t7opqqagu` FOREIGN KEY (`veiculo_entrada_id`) REFERENCES `veiculo_entrada` (`id`),
  ADD CONSTRAINT `FK3bq2f7sfpgyp0tcsqyocur7ww` FOREIGN KEY (`veiculo_entrada_id`) REFERENCES `veiculos` (`id`),
  ADD CONSTRAINT `FKatc1hchm0o3p30m1dgtmv6882` FOREIGN KEY (`negociacao_id`) REFERENCES `negociacao` (`id`);

--
-- Restrições para tabelas `item_checklist_entrega`
--
ALTER TABLE `item_checklist_entrega`
  ADD CONSTRAINT `FKh1xgshou3xm5hcc1nsewrk159` FOREIGN KEY (`checklist_id`) REFERENCES `checklist_entrega` (`id`);

--
-- Restrições para tabelas `negociacao`
--
ALTER TABLE `negociacao`
  ADD CONSTRAINT `FKimplyup5kmc8r9qeo2x4ol2e9` FOREIGN KEY (`vendedor_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FKqnrmvt5p68mp39amumdywvop` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculos` (`id`),
  ADD CONSTRAINT `FKrb7c53n5bvomri20vywei7s0d` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

--
-- Restrições para tabelas `notificacoes`
--
ALTER TABLE `notificacoes`
  ADD CONSTRAINT `FK3jcnk0ggmxklk5sjxm2plk5ml` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Restrições para tabelas `pagamentos_repasse`
--
ALTER TABLE `pagamentos_repasse`
  ADD CONSTRAINT `FK4mfvaxovv06ds82ucb9lkn3fy` FOREIGN KEY (`repasse_id`) REFERENCES `repasses` (`id`);

--
-- Restrições para tabelas `pendencia_negociacao`
--
ALTER TABLE `pendencia_negociacao`
  ADD CONSTRAINT `FK3eqjxo5r8m73lm4pjdq93nssi` FOREIGN KEY (`forma_pagamento_id`) REFERENCES `forma_pagamento_negociacao` (`id`),
  ADD CONSTRAINT `FKmrqvf8trbxkglx7qo66x453ge` FOREIGN KEY (`resolvida_por_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FKr8fvtlqyot78ig2okvibve61c` FOREIGN KEY (`negociacao_id`) REFERENCES `negociacao` (`id`);

--
-- Restrições para tabelas `repasses`
--
ALTER TABLE `repasses`
  ADD CONSTRAINT `FKi8vk0y98kxax43bk119lhfiaa` FOREIGN KEY (`venda_id`) REFERENCES `vendas` (`id`);

--
-- Restrições para tabelas `solicitacao_alteracao_login`
--
ALTER TABLE `solicitacao_alteracao_login`
  ADD CONSTRAINT `FKhxptdpmsn4twicp352j1qm6mx` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Restrições para tabelas `vendas`
--
ALTER TABLE `vendas`
  ADD CONSTRAINT `FK18jb6atkgnouxy9b97unm4uue` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK6sefd9hgm1cnkdn1t32612yt9` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculos` (`id`),
  ADD CONSTRAINT `FKhjjq16i500dnx1idx3lggyxvi` FOREIGN KEY (`negociacao_id`) REFERENCES `negociacao` (`id`),
  ADD CONSTRAINT `FKmxl3oku9fexbd1gcx9xsys3op` FOREIGN KEY (`vendedor_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FKto0nax9qj4dethg71834dc70y` FOREIGN KEY (`pagamento_id`) REFERENCES `pagamentos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
