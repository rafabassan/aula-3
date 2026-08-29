# Postman Collection - Aula 2 (Resolvido)

Importe os dois arquivos desta pasta no Postman:

1. `aula-2-resolvido.postman_collection.json` — a collection com os requests.
2. `local.postman_environment.json` — o environment com a variável `baseUrl` (`http://localhost:8080`).

## Fluxo sugerido

1. Rode a aplicação localmente (`mvn spring-boot:run`).
2. No Postman, selecione o environment **Local**.
3. Rode "Criar produto" e copie o `id` do produto retornado (verifique no banco ou no `GET /product`) para a variável `productId`, se quiser testar update/delete.
4. Rode os requests da pasta **Distribution Center** para validar a busca por RJ/MG/SP e a contagem bônus.

Os requests marcados como "invalido" servem para confirmar que valores fora do enum (`RJ`, `MG`, `SP`) retornam `400` com uma mensagem de erro amigável.
