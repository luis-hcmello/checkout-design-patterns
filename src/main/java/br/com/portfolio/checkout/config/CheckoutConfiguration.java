package br.com.portfolio.checkout.config;

import br.com.portfolio.checkout.auditoria.RegistroAuditoria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CheckoutConfiguration {

    /**
     * Expõe o Singleton GoF existente como um bean do Spring.
     * O container também mantém beans como singleton por padrão.
     */
    @Bean
    public RegistroAuditoria registroAuditoria() {
        return RegistroAuditoria.INSTANCE;
    }
}
