package br.com.portfolio.checkout.pagamento;

import java.math.BigDecimal;

/** Regra demonstrativa: pagamento com cartão recebe uma taxa de 2%. */
public class CartaoCreditoPagamentoStrategy implements PagamentoStrategy {

    @Override
    public String nome() {
        return "Cartão de crédito";
    }

    @Override
    public ResultadoPagamento calcular(BigDecimal valorPedido) {
        BigDecimal valor = validarEArredondar(valorPedido);
        BigDecimal taxa = calcularPercentual(valor, "0.02");
        BigDecimal finalAPagar = valor.add(taxa);

        return new ResultadoPagamento(nome(), valor, BigDecimal.ZERO, taxa, finalAPagar);
    }
}
