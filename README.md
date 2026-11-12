# API de Pedidos - Bilheteria

## Sobre o projeto

API REST desenvolvida em Java com Spring Boot para simular o gerenciamento de pedidos de um sistema de bilhetagem.

Permite cadastrar produtos, criar pedidos com itens adicionais, calcular o valor total e controlar o status do pagamento.

## Tecnologias utilizadas

- Java 21
- Spring Boot 4
- Maven
- PostgreSQL
- Spring Data JPA
- Lombok
- JUnit e Mockito

## Funcionalidades

- Cadastrar e consultar produtos.
- Criar pedidos com um ou mais produtos.
- Calcular automaticamente o valor total.
- Consultar e listar pedidos.
- Atualizar o status do pedido.
- Validar produtos e quantidades.
- Tratar erros e regras de negócio.

## Endpoints

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/produtos` | Cadastrar produto |
| GET | `/api/v1/produtos` | Listar produtos |
| GET | `/api/v1/produtos/{id}` | Buscar produto por ID |

### Pedidos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/pedidos` | Criar pedido |
| GET | `/api/v1/pedidos` | Listar pedidos |
| GET | `/api/v1/pedidos/{id}` | Buscar pedido por ID |
| PATCH | `/api/v1/pedidos/{id}/status` | Atualizar status |

## Status do pedido

- **AGUARDANDO_PAGAMENTO:** pedido criado, aguardando pagamento.
- **PAGO:** pagamento confirmado.
- **CANCELADO:** pedido cancelado.

Somente pedidos aguardando pagamento podem ter o status alterado para PAGO ou CANCELADO.

## Exemplos de uso da API

URL base: `http://localhost:8080`

### 1. Cadastrar produto

**POST** `/api/v1/produtos`

Requisição:

```json
{
  "nome": "Pipoca",
  "descricao": "Pipoca grande",
  "preco": 20.00,
  "tipo": "ALIMENTO",
  "ativo": true
}
```

Resposta esperada: `201 Created`

O produto é cadastrado no banco de dados.

### 2. Listar produtos

**GET** `/api/v1/produtos`

Resposta esperada: `200 OK`

Retorna todos os produtos cadastrados.

### 3. Buscar produto por ID

**GET** `/api/v1/produtos/1`

Resposta esperada: `200 OK`

Retorna o produto correspondente ao ID informado.

### 4. Criar pedido

**POST** `/api/v1/pedidos`

Requisição:

```json
{
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
```

Resposta esperada: `201 Created`

O pedido é criado com status `AGUARDANDO_PAGAMENTO`.

O valor total é calculado automaticamente com base nos produtos e quantidades.

### 5. Listar pedidos

**GET** `/api/v1/pedidos`

Resposta esperada: `200 OK`

Retorna todos os pedidos cadastrados.

### 6. Buscar pedido por ID

**GET** `/api/v1/pedidos/1`

Resposta esperada: `200 OK`

Retorna os dados do pedido, incluindo itens, valor total e status.

### 7. Atualizar status do pedido

**PATCH** `/api/v1/pedidos/1/status`

Requisição:

```json
{
  "status": "PAGO"
}
```

Resposta esperada: `200 OK`

Atualiza o status do pedido para `PAGO`.

Também é possível informar `CANCELADO`, desde que o pedido esteja aguardando pagamento.

### Exemplo de erro

Ao tentar alterar um pedido já pago, a API retorna `400 Bad Request`:

```json
{
  "status": 400,
  "mensagem": "Somente pedidos aguardando pagamento podem ter o status alterado"
}
```

## Como executar

1. Ter Java 21, Maven e PostgreSQL instalados.
2. Criar o banco de dados PostgreSQL.
3. Configurar a conexão no `application.properties`.
4. Executar a aplicação pelo IntelliJ ou Maven.
5. Testar os endpoints pelo Postman ou outra ferramenta de sua preferência.

## Testes

Os testes verificam as regras de negócio, os endpoints e o tratamento de exceções.

Para executar:

```bash
mvn clean test
```

## Observação

Esta API representa uma implementação simplificada do gerenciamento de pedidos de uma bilheteria.

A arquitetura AWS proposta  contempla uma solução mais completa, incluindo solicitação, reserva e compra de ingressos.