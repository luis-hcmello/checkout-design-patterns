package br.com.portfolio.checkout.auditoria;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton do projeto: mantém um único registro de eventos da aplicação.
 * O enum fornece uma instância única e inicialização segura pela JVM.
 */
public enum RegistroAuditoria {
    INSTANCE;

    private final List<String> eventos = new CopyOnWriteArrayList<>();

    public void registrar(String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("A mensagem de auditoria não pode ser vazia.");
        }
        eventos.add(Instant.now() + " | " + mensagem);
    }

    /** Retorna uma cópia imutável, sem expor a lista interna. */
    public List<String> listarEventos() {
        return List.copyOf(eventos);
    }
}
