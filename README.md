# Aula 2 - Distribution Center 📦

## Contexto

A aplicação de CRUD de produtos que vocês construíram na Aula 1 está fazendo sucesso, e a empresa fictícia **"Vende Tudo Ltda"** decidiu expandir as operações para além de um único depósito. Agora, cada produto cadastrado no sistema precisa informar **de qual centro de distribuição ele é despachado**.

A empresa opera em três centros de distribuição:

- **RJ** — Rio de Janeiro
- **MG** — Minas Gerais
- **SP** — São Paulo

O time de logística pediu duas coisas para o time de desenvolvimento (vocês):

1. Que o cadastro de produto passe a exigir um centro de distribuição válido (um dos três acima — nada de aceitar qualquer string solta).
2. Que exista uma forma de consultar rapidamente **quais produtos estão em um determinado centro de distribuição**, para ajudar o time a organizar o estoque local.

O código-base desta aula é **exatamente o mesmo** da Aula 1 (`aula-1-crud-java-basico`). O trabalho de vocês é evoluí-lo.

## Objetivo

Adicionar o campo `distributionCenter` ao domínio de `Product`, modelado como um **enum Java** com exatamente três valores possíveis: `RJ`, `MG`, `SP`.

## O que precisa ser feito

### 1. Criar o enum `DistributionCenter`

Um novo enum, com os três valores fixos: `RJ`, `MG`, `SP`. Pensem em onde ele deve morar no pacote do domínio de produto.

### 2. Adicionar o campo na entidade `Product`

A entidade `Product` (JPA) precisa ganhar um novo atributo `distributionCenter`, do tipo do enum criado, persistido no banco. Vale revisar como o JPA mapeia enums (`@Enumerated`) e decidir — e justificar — se a persistência deve ser por `STRING` ou por `ORDINAL`.

### 3. Migration do banco

Como o schema é versionado com Flyway, será necessário criar uma **nova migration** (`V5__...`) que:
- Adicione a coluna `distribution_center` na tabela `product`;
- Popule os produtos já existentes (seed da Aula 1) com um valor válido de centro de distribuição, para não deixar dado inconsistente pra trás.

### 4. Atualizar o `RequestProduct`

O record usado para criar/atualizar produtos precisa aceitar o novo campo, com a validação adequada (afinal, é um campo obrigatório — pensem em qual anotação de bean validation faz sentido para um campo que não pode vir nulo).

### 5. Atualizar o construtor/mapeamento de `Product`

O construtor que recebe um `RequestProduct` (e qualquer outro ponto que monte um `Product` a partir da requisição) precisa passar a propagar o `distributionCenter`.

### 6. Novo endpoint de consulta

Criar um endpoint em `ProductController` para buscar produtos por centro de distribuição, por exemplo:

```
GET /product/distribution-center/{distributionCenter}
```

Esse endpoint deve retornar apenas os produtos ativos (`active = true`) associados ao centro de distribuição informado. Pensem em como o `ProductRepository` precisa mudar para suportar essa busca de forma eficiente (sem repetir o padrão de trazer tudo e filtrar em memória, como foi feito propositalmente — e de forma exagerada — no endpoint de categoria da Aula 1).

## Critérios de aceite

- O enum `DistributionCenter` existe e tem somente os valores `RJ`, `MG`, `SP`.
- Um `POST /product` sem `distributionCenter`, ou com um valor que não seja um dos três válidos, deve ser rejeitado com erro de validação.
- Um `POST /product` com um `distributionCenter` válido persiste corretamente e é possível ver isso no banco.
- A migration V5 roda sem erro em um banco já populado pela V4 (sem quebrar o histórico do Flyway).
- `GET /product/distribution-center/RJ` (ou MG/SP) retorna apenas os produtos ativos daquele centro.
- `CrudApplicationTests` continua compilando e passando.

## Dicas

- Reaproveitem o padrão de código que já existe no projeto (records para request, `@RestControllerAdvice` para erros, etc.) — o objetivo aqui é estender, não reinventar.
- Testem cada mudança no Postman antes de seguir pra próxima, igual fizemos na Aula 1.
- Se dropar o schema local pra testar a migration do zero, cuidado com o `flyway_schema_history` — mesma pegadinha que tivemos na Aula 1 com o checksum.

## Desafio bônus (opcional)

Adicionar também um endpoint que retorne uma contagem de produtos ativos por centro de distribuição (ex.: `{"RJ": 5, "MG": 3, "SP": 7}`), útil para um futuro dashboard de estoque.

---

**Boa aula!**
