# Checkout Design Patterns

API REST de checkout desenvolvida em Java com Spring Boot. O projeto aplica os padrões de projeto **Singleton**, **Strategy** e **Facade** em um fluxo de finalização de pedidos.

## Visão geral

A API recebe os dados de um pedido e a forma de pagamento desejada, calcula o valor final de acordo com a estratégia selecionada e registra o processamento em uma auditoria em memória.

As regras de pagamento são demonstrativas. A aplicação não realiza cobranças, não se conecta a instituições financeiras e não armazena dados de cartão. Os pedidos e os eventos de auditoria também não são persistidos em banco de dados.

## Padrões de projeto

### Strategy

`PagamentoStrategy` define o contrato para as regras de cálculo. `PixPagamentoStrategy`, `BoletoPagamentoStrategy` e `CartaoCreditoPagamentoStrategy` implementam as modalidades disponíveis.

As implementações são registradas como componentes Spring (`@Component`). `PagamentoStrategyResolver` recebe a lista de estratégias por injeção de dependência e localiza a implementação pelo código informado na requisição. Dessa forma, a seleção da regra não exige condicionais dentro do fluxo de checkout.

| Código | Forma de pagamento | Regra demonstrativa |
| --- | --- | ---: |
| `PIX` | PIX | 5% de desconto |
| `BOLETO` | Boleto | 2% de desconto |
| `CARTAO` | Cartão de crédito | 2% de taxa |

### Singleton

`RegistroAuditoria` utiliza um `enum` com uma única constante (`INSTANCE`), garantindo uma única instância pela JVM. `CheckoutConfiguration` expõe essa instância como bean do Spring, permitindo que os componentes recebam o registro por injeção de dependência.

O Singleton GoF e o escopo singleton padrão do Spring são conceitos relacionados, mas não idênticos: o primeiro garante a instância única na implementação da classe; o segundo define que o container mantém uma única instância de um bean por contexto de aplicação.

### Facade

`CheckoutFacade` é um serviço Spring (`@Service`) que oferece uma operação de alto nível para concluir a compra. Ele coordena a validação do pedido, a resolução da estratégia de pagamento, o cálculo e o registro da auditoria.

O controller REST chama a Facade sem precisar conhecer os detalhes internos dessas etapas.

## Spring Framework

A aplicação utiliza abstrações do Spring para estruturar e disponibilizar o fluxo:

- `@SpringBootApplication` inicializa a aplicação e configura a component scan.
- `@RestController`, `@PostMapping`, `@GetMapping` e `@RequestMapping` expõem os recursos HTTP.
- `@Service` e `@Component` registram serviços e estratégias no container.
- Injeção de dependência por construtor conecta os componentes.
- `@Configuration` e `@Bean` integram o Singleton de auditoria ao container.
- `@Valid` e Jakarta Bean Validation validam os dados recebidos.
- `@RestControllerAdvice` centraliza respostas de erro da API.

## Endpoints

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/api/checkout` | Finaliza um pedido usando a forma de pagamento informada. |
| `GET` | `/api/pagamentos` | Lista as formas de pagamento disponíveis. |
| `GET` | `/api/auditoria` | Lista os eventos registrados durante a execução atual. |

### Exemplo de requisição

`POST /api/checkout`

```json
{
  "codigo": "PED-2026-001",
  "nomeCliente": "Cliente Demonstração",
  "formaPagamento": "PIX",
  "itens": [
    {
      "descricao": "Teclado mecânico",
      "quantidade": 1,
      "precoUnitario": 250.00
    },
    {
      "descricao": "Mouse",
      "quantidade": 2,
      "precoUnitario": 75.00
    }
  ]
}
```

### Exemplo de resposta

```json
{
  "codigoPedido": "PED-2026-001",
  "nomeCliente": "Cliente Demonstração",
  "quantidadeItens": 2,
  "valorOriginal": 400.00,
  "formaPagamento": "PIX",
  "desconto": 20.00,
  "taxa": 0.00,
  "valorFinal": 380.00,
  "processadoEm": "2026-11-17T12:00:00Z"
}
```

O campo `processadoEm` é gerado no momento da execução; o horário acima é apenas ilustrativo.

Requisições inválidas ou formas de pagamento desconhecidas retornam HTTP `400 Bad Request`, acompanhadas de uma mensagem explicativa.

## Tecnologias

- Java 17 ou superior
- Spring Boot 4.1.2
- Spring MVC
- Jakarta Bean Validation
- Maven
- JUnit Jupiter e Spring Boot Test
- `BigDecimal` para cálculos monetários

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/br/com/portfolio/checkout/
│   │   ├── api/
│   │   │   ├── controller/       # Endpoints REST
│   │   │   ├── dto/               # Objetos de requisição e resposta
│   │   │   └── exception/         # Tratamento de erros HTTP
│   │   ├── auditoria/             # Registro Singleton
│   │   ├── config/                # Configuração de beans Spring
│   │   ├── domain/                # Pedido e itens
│   │   ├── facade/                # Orquestração do checkout
│   │   └── pagamento/             # Strategy e resolução das estratégias
│   └── resources/
│       └── application.properties
└── test/java/br/com/portfolio/checkout/
    ├── api/                       # Testes da API REST
    ├── auditoria/
    ├── domain/
    ├── facade/
    └── pagamento/
```

## Executar localmente

Requisitos: JDK 17 ou superior e Maven 3.6.3 ou superior.

Executar os testes:

```bash
mvn clean test
```

Iniciar a API:

```bash
mvn spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

Gerar o pacote executável:

```bash
mvn clean package
```

Executar o JAR:

```bash
java -jar target/checkout-design-patterns.jar
```

### Testar no PowerShell

Listar as formas de pagamento:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/pagamentos" -Method Get
```

Enviar um pedido:

```powershell
$body = @{
    codigo = "PED-2026-002"
    nomeCliente = "Cliente Teste"
    formaPagamento = "PIX"
    itens = @(
        @{
            descricao = "Livro"
            quantidade = 2
            precoUnitario = 50.00
        }
    )
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Uri "http://localhost:8080/api/checkout" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

Consultar a auditoria:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auditoria" -Method Get
```

## Testes

Os testes cobrem as regras de cálculo, as validações do domínio, o comportamento do Singleton, o fluxo coordenado pela Facade e os principais cenários da API REST, incluindo checkout válido, validação de requisição e listagem das formas de pagamento.

## Referências

- [Spring Boot — documentação oficial](https://docs.spring.io/spring-boot/reference/)
- [Spring Boot — requisitos do sistema](https://docs.spring.io/spring-boot/system-requirements.html)
- [Spring Framework — documentação oficial](https://docs.spring.io/spring-framework/reference/)
- [Catálogo de padrões de projeto em Java — Refactoring.Guru](https://refactoring.guru/design-patterns/java)
- [Maven — documentação oficial](https://maven.apache.org/guides/)
