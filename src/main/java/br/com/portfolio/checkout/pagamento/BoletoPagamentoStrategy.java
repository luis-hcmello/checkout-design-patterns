package br.com.portfolio.checkout.pagamento;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Regra demonstrativa: pagamento via boleto recebe 2% de desconto. */
@Component
public class BoletoPagamentoStrategy implements PagamentoStrategy {

    @Override
    public String codigo() {
        return "BOLETO";
    }

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
