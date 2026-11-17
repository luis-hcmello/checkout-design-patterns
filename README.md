# Checkout Design Patterns

Projeto de estudo e portfólio em **Java puro**, com Maven e testes automatizados. A aplicação simula o fechamento de um pedido e demonstra três padrões de projeto: **Singleton, Strategy e Facade**.

> As regras de desconto e taxa são fictícias e servem somente para demonstrar a troca de estratégias. O projeto não realiza pagamentos reais, não se conecta a bancos e não armazena dados de cartão.

## Objetivo

Consolidar os padrões estudados no curso em uma aplicação pequena, mas integrada. Em vez de implementar cada padrão como um exemplo isolado, o fluxo de checkout usa os três em conjunto.

## Padrões aplicados

### 1. Strategy — regras de pagamento intercambiáveis

A interface `PagamentoStrategy` define o contrato para calcular um pagamento. Cada classe concreta contém uma regra independente:

| Estratégia | Regra demonstrativa |
|---|---:|
| `PixPagamentoStrategy` | 5% de desconto |
| `BoletoPagamentoStrategy` | 2% de desconto |
| `CartaoCreditoPagamentoStrategy` | 2% de taxa |

A classe `CheckoutFacade` recebe uma estratégia pelo construtor. Assim, o fluxo de checkout não precisa conhecer os detalhes de cada cálculo. Para adicionar uma nova forma, basta criar outra implementação de `PagamentoStrategy`.

### 2. Singleton — registro único de auditoria

`RegistroAuditoria` é um `enum` com uma única constante (`INSTANCE`). A JVM garante uma única instância do enum. O registro usa `CopyOnWriteArrayList` para permitir inclusão e leitura concorrentes e entrega uma cópia imutável dos eventos. É uma escolha simples para este exemplo; em uma aplicação real, o histórico não deveria crescer indefinidamente em memória e seria melhor usar uma solução de logging apropriada.

O Singleton é usado porque o exemplo representa um registro compartilhado pela aplicação. Em sistemas maiores, é importante evitar transformar todo serviço em Singleton: estado global pode dificultar testes e acoplar componentes. Aqui a dependência é recebida pelo construtor da Facade para deixar esse vínculo explícito.

### 3. Facade — uma operação para concluir o checkout

`CheckoutFacade.finalizarCompra()` expõe uma operação simples e coordena os passos internos:

1. Recebe e valida o pedido.
2. Calcula o total dos itens.
3. Executa a estratégia de pagamento selecionada.
4. Registra o evento de auditoria.
5. Devolve um `ResultadoCheckout` com o pedido, o cálculo e o horário do processamento.

Quem usa a Facade não precisa coordenar manualmente cada uma dessas etapas.

## Tecnologias e conceitos

- Java 17 ou superior (o projeto também pode ser aberto com JDK 21/25).
- Maven para dependências, compilação e empacotamento.
- JUnit 5 para testes unitários.
- `BigDecimal` para valores monetários, evitando cálculos com `float` ou `double`.
- `record`, `List.copyOf`, `switch` expression e validações com `Objects.requireNonNull`.

## Estrutura do projeto

```text
checkout-design-patterns/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/java/br/com/portfolio/checkout/
    │   ├── app/
    │   │   └── Main.java
    │   ├── auditoria/
    │   │   └── RegistroAuditoria.java
    │   ├── domain/
    │   │   ├── ItemPedido.java
    │   │   └── Pedido.java
    │   ├── facade/
    │   │   ├── CheckoutFacade.java
    │   │   └── ResultadoCheckout.java
    │   └── pagamento/
    │       ├── PagamentoStrategy.java
    │       ├── ResultadoPagamento.java
    │       ├── PixPagamentoStrategy.java
    │       ├── BoletoPagamentoStrategy.java
    │       └── CartaoCreditoPagamentoStrategy.java
    └── test/java/br/com/portfolio/checkout/
        ├── auditoria/RegistroAuditoriaTest.java
        ├── domain/PedidoTest.java
        ├── facade/CheckoutFacadeTest.java
        └── pagamento/PagamentoStrategyTest.java
```

## Como executar

Pré-requisitos: JDK 17 ou superior e Maven instalado e disponível no terminal.

Na pasta que contém o `pom.xml`, rode os testes:

```bash
mvn clean test
```

Gere o arquivo JAR:

```bash
mvn clean package
```

Execute com PIX (forma padrão):

```bash
java -jar target/checkout-design-patterns.jar
```

Ou escolha outra estratégia:

```bash
java -jar target/checkout-design-patterns.jar cartao
java -jar target/checkout-design-patterns.jar boleto
```

Argumentos aceitos: `pix`, `cartao` e `boleto`.

## Exemplo de resultado

Com o pedido de demonstração (um teclado de R$ 250,00 e dois mouses de R$ 75,00), o total original é R$ 400,00. Executando com PIX, o cálculo didático aplica R$ 20,00 de desconto e apresenta R$ 380,00 como valor final.

## Testes incluídos

Os testes verificam:

- O desconto do PIX.
- A taxa do cartão.
- O desconto do boleto.
- A rejeição de valor de pagamento zero ou negativo e de desconto acima do valor original.
- A validação de pedidos vazios e a proteção da lista de itens contra alterações externas.
- A conclusão do checkout e o registro do evento de auditoria.
- O comportamento de instância única do Singleton.

## Como evoluir o projeto

Boas próximas melhorias para transformar o exercício em um projeto mais completo:

1. Criar uma API REST com Spring Boot, expondo endpoints para criar pedidos e finalizar compras.
2. Validar os dados recebidos com Bean Validation.
3. Persistir pedidos e pagamentos em um banco de dados usando Spring Data JPA.
4. Substituir as regras simuladas por integração com um provedor de pagamento em ambiente de testes.
5. Adicionar testes de integração e documentação OpenAPI/Swagger.
6. Externalizar as regras de desconto e taxa para configuração, sem alterar as estratégias existentes.

## Referências de estudo

- [Catálogo de Design Patterns em Java — Refactoring.Guru](https://refactoring.guru/design-patterns/java)
- [Singleton — Refactoring.Guru](https://refactoring.guru/design-patterns/singleton)
- [Strategy — Refactoring.Guru](https://refactoring.guru/design-patterns/strategy)
- [Facade — Refactoring.Guru](https://refactoring.guru/design-patterns/facade)
- [Maven — Getting Started Guide](https://maven.apache.org/guides/getting-started/)

## Publicar no GitHub

1. Crie um repositório chamado `checkout-design-patterns` na sua conta.
2. Abra o terminal na pasta do projeto.
3. Inicialize o Git, faça o primeiro commit e conecte o repositório remoto:

```bash
git init
git add .
git commit -m "feat: implementa checkout com design patterns"
git branch -M main
git remote add origin URL_DO_SEU_REPOSITORIO
git push -u origin main
```

Não inclua a pasta `target/` no commit: ela contém arquivos gerados pelo Maven e já está no `.gitignore`.
