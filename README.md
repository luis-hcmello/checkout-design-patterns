# Checkout Design Patterns

Aplicação de linha de comando que simula a finalização de um pedido. O projeto foi desenvolvido para praticar padrões de projeto em Java, integrando **Singleton**, **Strategy** e **Facade** em um único fluxo de checkout.

## Sobre a aplicação

A aplicação recebe um pedido com seus itens, calcula o valor total e aplica a regra correspondente à forma de pagamento escolhida. Ao final, registra o processamento em um registro de auditoria e apresenta o resultado do checkout.

As condições de pagamento são apenas demonstrativas: não há cobrança real, integração com instituições financeiras nem armazenamento de dados de cartão.

## Padrões de projeto

### Strategy

A interface `PagamentoStrategy` define o contrato para as regras de pagamento. As implementações `PixPagamentoStrategy`, `BoletoPagamentoStrategy` e `CartaoCreditoPagamentoStrategy` encapsulam os cálculos de cada modalidade.

| Forma de pagamento | Regra aplicada |
| --- | ---: |
| PIX | 5% de desconto |
| Boleto | 2% de desconto |
| Cartão de crédito | 2% de acréscimo |

A estratégia é fornecida ao checkout, permitindo trocar a regra de cálculo sem alterar o fluxo principal.

### Singleton

`RegistroAuditoria` utiliza um `enum` com uma única constante (`INSTANCE`) para disponibilizar uma instância compartilhada. O registro armazena os eventos do checkout e permite consultar uma cópia imutável da lista.

Essa implementação demonstra o padrão em um contexto pequeno. Em uma aplicação de produção, seria necessário avaliar a persistência dos eventos, o volume de dados e o uso de uma solução de logging apropriada.

### Facade

`CheckoutFacade` reúne as etapas necessárias para concluir uma compra: valida o pedido, obtém o total, executa a estratégia de pagamento e registra o evento de auditoria. O resultado é devolvido em um `ResultadoCheckout`.

Assim, a classe que inicia o checkout utiliza uma operação de alto nível, sem precisar coordenar diretamente cada componente envolvido.

## Tecnologias

- Java 17 ou superior
- Maven
- JUnit 5
- `BigDecimal` para operações com valores monetários

## Organização do código

```text
src/
├── main/java/br/com/portfolio/checkout/
│   ├── app/          # Ponto de entrada da aplicação
│   ├── auditoria/    # Registro de eventos (Singleton)
│   ├── domain/       # Pedido e itens do pedido
│   ├── facade/       # Orquestração do checkout (Facade)
│   └── pagamento/    # Estratégias e resultados de pagamento (Strategy)
└── test/java/br/com/portfolio/checkout/
    ├── auditoria/
    ├── domain/
    ├── facade/
    └── pagamento/
```

## Executar

É necessário ter o JDK 17 ou superior e o Maven instalados.

Executar os testes:

```bash
mvn clean test
```

Gerar o pacote:

```bash
mvn clean package
```

Executar a demonstração com PIX:

```bash
java -jar target/checkout-design-patterns.jar
```

Também é possível selecionar outra forma de pagamento como argumento:

```bash
java -jar target/checkout-design-patterns.jar cartao
java -jar target/checkout-design-patterns.jar boleto
```

Argumentos aceitos: `pix`, `cartao` e `boleto`.

## Testes

Os testes unitários cobrem as regras de cálculo das formas de pagamento, validações de pedido e valores, comportamento do registro de auditoria e fluxo de finalização do checkout.

## Referências

- [Catálogo de padrões de projeto em Java — Refactoring.Guru](https://refactoring.guru/design-patterns/java)
- [Singleton — Refactoring.Guru](https://refactoring.guru/design-patterns/singleton)
- [Strategy — Refactoring.Guru](https://refactoring.guru/design-patterns/strategy)
- [Facade — Refactoring.Guru](https://refactoring.guru/design-patterns/facade)
- [Maven — Getting Started Guide](https://maven.apache.org/guides/getting-started/)
