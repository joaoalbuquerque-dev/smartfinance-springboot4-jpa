# SmartFinance 💰

## 📌 Sobre o projeto

O **SmartFinance** é uma API REST para gerenciamento de finanças pessoais, desenvolvida com Java e Spring Boot.

O projeto tem como objetivo permitir o controle de contas financeiras, receitas, despesas, categorias e pagamentos parcelados, centralizando essas informações em uma única aplicação.

A proposta é facilitar o acompanhamento das movimentações financeiras e ajudar o usuário a entender como suas receitas e despesas impactam o saldo de suas contas.

Além do gerenciamento financeiro, o projeto busca aplicar conceitos importantes de desenvolvimento Backend, como modelagem de entidades, relacionamentos com JPA/Hibernate, validação de regras de negócio e consistência dos dados.


## 🛠️ Tecnologias utilizadas

- **Java 21** — linguagem de programação utilizada no desenvolvimento.
- **Spring Boot** — framework para construção da aplicação.
- **Spring Web** — desenvolvimento dos endpoints REST.
- **Spring Data JPA** — abstração para acesso e persistência de dados.
- **Hibernate** — implementação do mapeamento objeto-relacional (ORM).
- **Maven** — gerenciamento de dependências e build do projeto.
- **H2 Database** — banco de dados em memória utilizado no ambiente de testes.
- **PostgreSQL** — banco de dados relacional utilizado no projeto.

## ⚙️ Como executar o projeto

### Pré-requisitos

- Java 21 instalado.
- IntelliJ IDEA ou outra IDE compatível com projetos Java e Maven.
- Git instalado para clonar o repositório.

### Executando a aplicação

1. Clone o repositório:

   ```bash
   git clone https://github.com/joaoalbuquerque-dev/smartfinance-springboot4-jpa.git
   ```

2. Abra o projeto na sua IDE e aguarde o Maven baixar e resolver as dependências.

3. Localize a classe `SmartfinanceApplication` e execute o método `main`.

4. Após a inicialização, a API estará disponível em:

   `http://localhost:8080`

### Banco de dados

Atualmente, o ambiente de testes utiliza o banco de dados H2 em memória. Os dados armazenados nesse banco são temporários e podem ser perdidos quando a aplicação é reiniciada.

## ✨ Funcionalidades

### 👤 Gerenciamento de usuários
- Cadastro, consulta, atualização e exclusão de usuários.
- Proteção da senha nas respostas da API.

### 🏦 Gerenciamento de contas
- Cadastro e gerenciamento de contas financeiras.
- Associação de contas aos usuários.
- Atualização do saldo conforme as movimentações financeiras.

### 💸 Movimentações financeiras
- Registro de receitas e despesas.
- Consulta, atualização e exclusão de movimentações.
- Associação de movimentações a contas financeiras.
- Atualização do saldo da conta conforme o tipo de movimentação.

### 🗂️ Categorias
- Cadastro e gerenciamento de categorias financeiras.
- Associação de categorias às movimentações.
- Distribuição do valor de uma movimentação entre diferentes categorias.
- Validação para garantir que a soma dos valores das categorias corresponda ao valor total da movimentação.

### 📅 Parcelamentos
- Criação de movimentações parceladas.
- Cálculo automático dos valores das parcelas, incluindo ajustes de centavos.
- Cálculo automático das datas de vencimento.
- Validação da quantidade, numeração, valores e datas das parcelas.
- Controle do status das parcelas, como pendente e paga.

### 💳 Pagamentos
- Registro de pagamentos de parcelas.
- Prevenção de pagamentos duplicados para uma mesma parcela.
- Registro automático da data do pagamento.
- Atualização do saldo da conta após o pagamento.
- Reversão do saldo e retorno da parcela ao status pendente quando um pagamento é excluído.

### 🛡️ Validações e tratamento de erros
- Validação de valores financeiros.
- Verificação da existência de entidades relacionadas.
- Prevenção de associações duplicadas entre categorias e movimentações.
- Tratamento de recursos não encontrados e dados inválidos.
- Gerenciamento transacional em operações de pagamento.

## 🏗️ Arquitetura do projeto

O SmartFinance utiliza uma arquitetura em camadas, separando as responsabilidades para facilitar a organização, a manutenção e a evolução do código.

- **Resource:** recebe as requisições HTTP e disponibiliza os endpoints da API.
- **Service:** concentra as regras de negócio, validações e operações da aplicação.
- **Repository:** realiza o acesso e a persistência dos dados utilizando Spring Data JPA.
- **Entities:** representam as entidades do domínio e seus relacionamentos com o banco de dados.

### Fluxo de uma requisição

`Cliente HTTP → Resource → Service → Repository → Banco de dados`

Essa separação permite manter as regras de negócio independentes da camada responsável por receber as requisições e da camada de persistência.

## 🔗 Endpoints da API

A API disponibiliza endpoints REST para o gerenciamento dos recursos financeiros do SmartFinance.

### Usuários — `/users`

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca um usuário por ID |
| POST | `/users` | Cadastra um usuário |
| PUT | `/users/{id}` | Atualiza um usuário |
| DELETE | `/users/{id}` | Exclui um usuário |

### Contas — `/accounts`

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| GET | `/accounts` | Lista todas as contas |
| GET | `/accounts/{id}` | Busca uma conta por ID |
| POST | `/accounts` | Cadastra uma conta |
| PUT | `/accounts/{id}` | Atualiza uma conta |
| DELETE | `/accounts/{id}` | Exclui uma conta |

### Categorias — `/categories`

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| GET | `/categories` | Lista todas as categorias |
| GET | `/categories/{id}` | Busca uma categoria por ID |
| POST | `/categories` | Cadastra uma categoria |
| DELETE | `/categories/{id}` | Exclui uma categoria |

### Movimentações — `/movements`

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| GET | `/movements` | Lista todas as movimentações |
| GET | `/movements/{id}` | Busca uma movimentação por ID |
| POST | `/movements` | Cadastra uma movimentação |
| PUT | `/movements/{id}` | Atualiza uma movimentação |
| DELETE | `/movements/{id}` | Exclui uma movimentação |

### Pagamentos — `/payments`

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| GET | `/payments` | Lista todos os pagamentos |
| GET | `/payments/{id}` | Busca um pagamento por ID |
| POST | `/payments` | Registra um pagamento |
| DELETE | `/payments/{id}` | Exclui um pagamento e reverte seus efeitos financeiros, conforme as regras implementadas |

**Observação:** os endpoints de consulta retornam os recursos cadastrados, enquanto os endpoints de escrita executam as operações correspondentes às regras de negócio da aplicação.

## 📍 Status do projeto

O SmartFinance está em desenvolvimento contínuo e faz parte do meu processo de aprendizado em Backend Java.

As funcionalidades implementadas estão sendo desenvolvidas e verificadas durante o desenvolvimento. Novas regras de negócio, melhorias na organização do código e evoluções da API serão incorporadas gradualmente.