package br.com.portfolio.checkout.pagamento;

import java.math.BigDecimal;

/** Regra demonstrativa: pagamento via boleto recebe 2% de desconto. */
public class BoletoPagamentoStrategy implements PagamentoStrategy {

    @Override
    public String nome() {
        return "Boleto";
    }

    @Override
    public ResultadoPagamento calcular(BigDecimal valorPedido) {
        BigDecimal valor = validarEArredondar(valorPedido);
        BigDecimal desconto = calcularPercentual(valor, "0.02");
        BigDecimal finalAPagar = valor.subtract(desconto);

        return new ResultadoPagamento(nome(), valor, desconto, BigDecimal.ZERO, finalAPagar);
    }
}
