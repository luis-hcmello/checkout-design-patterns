package br.com.portfolio.checkout.pagamento;

import java.math.BigDecimal;

/** Regra demonstrativa: pagamento via PIX recebe 5% de desconto. */
public class PixPagamentoStrategy implements PagamentoStrategy {

    @Override
    public String nome() {
        return "PIX";
    }

    @Override
    public ResultadoPagamento calcular(BigDecimal valorPedido) {
        BigDecimal valor = validarEArredondar(valorPedido);
        BigDecimal desconto = calcularPercentual(valor, "0.05");
        BigDecimal finalAPagar = valor.subtract(desconto);

        return new ResultadoPagamento(nome(), valor, desconto, BigDecimal.ZERO, finalAPagar);
    }
}
