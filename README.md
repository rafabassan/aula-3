# Aula 3 - Derivação x Raw Query 🔍

## Contexto

A "Vende Tudo Ltda" gostou tanto da busca por centro de distribuição que agora o time de produto quer mais formas de consultar o catálogo — só que dessa vez, o objetivo não é só *fazer funcionar*, e sim **entender as diferentes formas de escrever uma consulta no Spring Data JPA** e quando usar cada uma.

Hoje em dia, no `ProductRepository`, só existem dois jeitos de buscar dados: os métodos derivados simples (`findAllByActiveTrue`, `findAllByActiveTrueAndDistributionCenter`) e a projeção customizada da contagem por centro de distribuição. Chegou a hora de explorar o espectro completo:

- **Derived Query Methods** — o Spring Data escreve o SQL pra você, a partir do nome do método (`findByCategoryAndActiveTrue`, `findByPriceGreaterThan`, etc.).
- **Raw Query** — você escreve a consulta manualmente com `@Query`, seja em JPQL (orientado a entidades) ou em SQL nativo (orientado a tabelas/colunas de verdade).

Cada abordagem tem seu lugar. A tarefa de hoje é implementar 5 endpoints que exercitam as duas formas, incluindo um desafio extra onde **nem uma nem outra pode ser usada**.

O código-base desta aula é o resultado da **Aula 2 já resolvida** (com o `distributionCenter` implementado). Todo mundo parte dali.

## Objetivo

Implementar 5 endpoints novos em `ProductController`, cobrindo Derived Query Methods, Raw Query (JPQL e SQL nativo) e lógica de negócio pura em uma classe de modelo.

## Os 5 endpoints

### 1. Filtrar por categoria — Derived Query Method

```
GET /product?category={category}
```

Retorna a lista de produtos ativos de uma categoria específica.

**Regra:** o filtro tem que acontecer no banco, via um Derived Query Method no `ProductRepository` (ex: algo no estilo `findAllByActiveTrueAndCategory`).

**PROIBIDO:** carregar todos os produtos e filtrar a lista em Java (é literalmente o que o endpoint `/product/category/{categoryAsPath}` da Aula 1 faz — e é feio de propósito. Aqui é pra fazer certo).

### 2. Buscar produto por ID — Derived Query Method

```
GET /product/{id}
```

Retorna um único produto (200) ou 404 se não existir ou estiver inativo.

**Regra:** a busca precisa ir direto no banco pelo ID informado no path, considerando também `active = true`. Dica: dá pra combinar duas condições no nome do método (`findBy...And...`).

### 3. Filtrar por preço mínimo — Derived Query Method com operador

```
GET /product/price/above/{value}
```

Retorna produtos ativos com `price` maior que o valor informado.

**Regra:** usar um Derived Query Method com o keyword de comparação do Spring Data (`GreaterThan`). Sem loop em Java, sem `@Query` — só o nome do método fazendo o trabalho.

### 4. Buscar por nome — Raw Query (JPQL)

```
GET /product/search?term={term}
```

Retorna produtos ativos cujo nome contenha o termo pesquisado (case-insensitive).

**Regra:** implementar com `@Query` em JPQL, usando `LIKE` (e `LOWER` ou equivalente para ignorar caixa). Essa é a primeira vez que vocês escrevem a query na mão — reparem a diferença de controle comparado ao Derived Query Method.

### 5. EXTRA!! 🌶️ — Top 3 produtos mais caros — lógica em classe de modelo

```
GET /product/top-expensive
```

Retorna os 3 produtos ativos de maior `price` no catálogo.

**PROIBIDO:** resolver isso via query — nem Derived Query Method, nem `@Query`, nem `ORDER BY` + `LIMIT` de qualquer tipo. A lógica de encontrar os 3 mais caros precisa estar em **uma classe de modelo** (ex: uma classe `TopExpensiveProducts` ou similar), que recebe a lista de produtos ativos (usando o método que já existe, `findAllByActiveTrue`) e calcula o top 3 em Java puro.

A ideia aqui é treinar um raciocínio diferente: nem toda lógica de negócio precisa (ou deve) virar SQL. Às vezes o lugar certo é o modelo de domínio.

## Critérios de aceite

- Os endpoints 1, 2 e 3 não têm nenhum `@Query` — só Derived Query Methods.
- O endpoint 4 usa `@Query` com JPQL.
- O endpoint 5 não tem nenhuma ordenação/filtro feito no banco — a lista completa de ativos vem do repository e o cálculo do top 3 acontece numa classe de modelo dedicada.
- Todos os endpoints respeitam o soft delete (`active = true`).
- `CrudApplicationTests` continua compilando e passando.

## Dicas

- Deem uma olhada na [documentação de Query Methods do Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html) pra ver a lista de keywords suportadas (`GreaterThan`, `Containing`, `And`, etc.) — muita coisa que parece exigir `@Query` na verdade tem um Derived Query Method pronto.
- No endpoint 4, pensem em como fica a mesma busca se fosse feita como Derived Query Method (`findAllByActiveTrueAndNameContainingIgnoreCase`, por exemplo) — vale comparar as duas formas de resolver o mesmo problema.
- Testem cada endpoint via Postman/coleção antes de seguir pro próximo.

---

**Boa aula!**
