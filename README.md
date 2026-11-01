
# 🚗 Sistema de Gestão de Concessionária

Sistema web desenvolvido para o gerenciamento interno de uma concessionária de veículos.

O projeto tem como objetivo centralizar e organizar os processos de uma concessionária, desde o cadastro de veículos e compradores até a realização de negociações, vendas e repasses financeiros.

O sistema possui controle de acesso por perfis de usuários, acompanhamento de negociações, geração de documentos e relatórios gerenciais.

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- MySQL
- HTML, CSS e JavaScript
- Maven

## 🚀 Funcionalidades do Sistema

### 👥 Gestão de Usuários
- Cadastro, edição e gerenciamento de usuários.
- Controle de acesso por perfis: Dono, Gerente e Vendedor.
- Painéis específicos conforme o perfil do usuário.
- Solicitação de alteração de login.

### 🚗 Gestão de Veículos
- Cadastro e edição de veículos.
- Registro dos dados do proprietário.
- Controle de veículos disponíveis, reservados e vendidos.
- Pesquisa e paginação de registros.
- Geração de contrato de consignação.

### 🤝 Negociações
- Abertura e acompanhamento de negociações.
- Registro de múltiplas formas de pagamento.
- Controle de financiamentos, consórcios e veículos recebidos como entrada.
- Gerenciamento de pendências.
- Conclusão e cancelamento de negociações.

### 💰 Vendas e Repasses
- Registro e histórico de vendas.
- Controle dos valores devidos aos proprietários.
- Registro de pagamentos parciais e integrais dos repasses.
- Emissão de comprovantes e recibos.

### 📊 Painéis e Relatórios
- Indicadores gerenciais e financeiros.
- Histórico de vendas por vendedor.
- Consultas com filtros e paginação.
- Relatórios disponíveis para impressão.

### 📄 Documentos e Entrega
- Comprovante de venda.
- Contrato de consignação.
- Termo de veículo recebido como entrada.
- Recibo de quitação de repasse.
- Checklist de entrega do veículo.

### 🔔 Recursos Adicionais
- Notificações internas.
- Configuração dos dados da concessionária.
- Interface responsiva para computadores e dispositivos móveis.

## 🏗️ Estrutura e Arquitetura

O sistema foi desenvolvido utilizando uma arquitetura baseada no padrão MVC (Model-View-Controller), com separação de responsabilidades entre as camadas da aplicação.

### Organização do Projeto

- **Model:** representa as entidades e regras associadas aos dados do sistema.
- **Repository:** realiza o acesso ao banco de dados utilizando Spring Data JPA.
- **Controller:** recebe as requisições, coordena as operações e controla a navegação entre as páginas.
- **Templates:** contém as páginas HTML renderizadas pelo Thymeleaf.
- **Static:** armazena os arquivos CSS, JavaScript e demais recursos estáticos.

### Persistência de Dados

O sistema utiliza MySQL para armazenar as informações, com mapeamento objeto-relacional realizado pelo JPA/Hibernate.

As entidades possuem relacionamentos que representam os processos da concessionária, como negociações, vendas, pagamentos e repasses.

## 📐 Documentação UML

O projeto possui documentação UML para representar a estrutura, o comportamento e os processos do sistema.

Foram desenvolvidos 13 diagramas, distribuídos entre:

- **Diagramas de Classes:** representam as entidades, seus atributos e relacionamentos.
- **Diagramas de Casos de Uso:** apresentam as funcionalidades e a interação dos usuários com o sistema.
- **Diagramas de Sequência:** demonstram a comunicação entre os componentes durante determinadas operações.
- **Diagramas de Atividades:** representam os fluxos dos processos de negócio.
- **Diagrama de Componentes:** apresenta a organização dos principais componentes da aplicação.
- **Diagrama de Implantação:** representa a estrutura de execução do sistema.

Os arquivos estão disponíveis na pasta `documentação/`, nos formatos PlantUML (`.puml`) e SVG (`.svg`).


## ⚙️ Instalação e Execução

### Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- Java JDK 17.
- MySQL.
- Maven.
- XAMPP (opcional, para iniciar o serviço MySQL).

### 1. Obter o Projeto

Clone o repositório:

```bash
git clone https://github.com/matheusUrias90/Concessionaria.git
```

Entre na pasta do projeto:

```bash
cd Concessionaria
```

### 2. Configurar o Banco de Dados

1. Inicie o serviço MySQL.
2. Acesse o phpMyAdmin.
3. Crie o banco de dados `concessionaria_db`.
4. Importe o arquivo `database/concessionaria_db.sql`.

### 3. Configurar a Conexão

Configure as propriedades de conexão com o MySQL no arquivo `application.properties`, localizado em `src/main/resources`.

Ajuste o endereço do banco, usuário e senha conforme a configuração do ambiente local.

### 4. Executar a Aplicação

Na pasta raiz do projeto, execute:

```bash
mvn spring-boot:run
```

Após a inicialização, acesse a aplicação pelo navegador no endereço:

```text
http://localhost:8080
```

## 📁 Estrutura do Projeto

O projeto está organizado em pacotes e diretórios que separam as responsabilidades da aplicação.

```text
Concessionaria/
├── database/
│   └── concessionaria_db.sql
├── documentação/
├── src/
│   └── main/
│       ├── java/
│       │   └── br/com/urias/cadastro_veiculos/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── model/
│       │       ├── repository/
│       │       ├── service/
│       │       └── CadastroVeiculosApplication.java
│       └── resources/
│           ├── static/
│           ├── templates/
│           └── application.properties
├── pom.xml
└── README.md
```

### Responsabilidades dos Pacotes

- **controller:** recebe e processa as requisições HTTP.
- **dto:** transporta dados entre componentes da aplicação.
- **model:** representa as entidades do sistema.
- **repository:** realiza operações de persistência utilizando Spring Data JPA.
- **service:** centraliza operações e regras de negócio específicas.
- **templates:** contém as páginas HTML do sistema.
- **static:** contém os recursos estáticos da interface.
- **database:** disponibiliza o script SQL do banco de dados.
- **documentação:** reúne os diagramas UML do projeto.
