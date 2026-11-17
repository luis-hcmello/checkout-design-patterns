package br.com.portfolio.checkout.app;

import br.com.portfolio.checkout.auditoria.RegistroAuditoria;
import br.com.portfolio.checkout.domain.ItemPedido;
import br.com.portfolio.checkout.domain.Pedido;
import br.com.portfolio.checkout.facade.CheckoutFacade;
import br.com.portfolio.checkout.facade.ResultadoCheckout;
import br.com.portfolio.checkout.pagamento.BoletoPagamentoStrategy;
import br.com.portfolio.checkout.pagamento.CartaoCreditoPagamentoStrategy;
import br.com.portfolio.checkout.pagamento.PagamentoStrategy;
import br.com.portfolio.checkout.pagamento.PixPagamentoStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        String formaPagamento = args.length == 0 ? "pix" : args[0].toLowerCase(Locale.ROOT);

        try {
            PagamentoStrategy estrategia = selecionarEstrategia(formaPagamento);
            Pedido pedido = criarPedidoExemplo();

            CheckoutFacade checkout = new CheckoutFacade(estrategia, RegistroAuditoria.INSTANCE);
            ResultadoCheckout resultado = checkout.finalizarCompra(pedido);

            exibirResultado(resultado);
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.err.println("Uso: java -jar target/checkout-design-patterns.jar [pix|cartao|boleto]");
            System.exit(1);
        }
    }

    private static PagamentoStrategy selecionarEstrategia(String formaPagamento) {
        return switch (formaPagamento) {
            case "pix" -> new PixPagamentoStrategy();
            case "cartao" -> new CartaoCreditoPagamentoStrategy();
            case "boleto" -> new BoletoPagamentoStrategy();
            default -> throw new IllegalArgumentException("Forma de pagamento desconhecida: " + formaPagamento);
        };
    }

    private static Pedido criarPedidoExemplo() {
        return new Pedido(
                "PED-2026-001",
                "Cliente Demonstração",
                List.of(
                        new ItemPedido("Teclado mecânico", 1, new BigDecimal("250.00")),
                        new ItemPedido("Mouse", 2, new BigDecimal("75.00"))
                )
        );
    }

    private static String formatarMoeda(BigDecimal valor) {
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
    }

    private static void exibirResultado(ResultadoCheckout resultado) {
        var pedido = resultado.pedido();
        var pagamento = resultado.pagamento();

        System.out.println("========================================");
        System.out.println("       CHECKOUT - DESIGN PATTERNS");
        System.out.println("========================================");
        System.out.println("Pedido:          " + pedido.codigo());
        System.out.println("Cliente:         " + pedido.nomeCliente());
        System.out.println("Itens:           " + pedido.itens().size());
        System.out.println("Valor original:  " + formatarMoeda(pagamento.valorOriginal()));
        System.out.println("Pagamento:       " + pagamento.formaPagamento());
        System.out.println("Desconto:        " + formatarMoeda(pagamento.desconto()));
        System.out.println("Taxa:            " + formatarMoeda(pagamento.taxa()));
        System.out.println("Valor final:     " + formatarMoeda(pagamento.valorFinal()));
        System.out.println("Processado em:   " + resultado.processadoEm());
        System.out.println("----------------------------------------");
        System.out.println("Evento de auditoria registrado: "
                + RegistroAuditoria.INSTANCE.listarEventos().size());
        System.out.println("========================================");
    }
}
